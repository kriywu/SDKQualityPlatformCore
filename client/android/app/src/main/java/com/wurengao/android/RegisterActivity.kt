package com.wurengao.android

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.wurengao.android.dispatcher.CommandDispatcher
import com.wurengao.android.proto.APICommand
import okhttp3.WebSocket
import org.json.JSONObject

class RegisterActivity : AppCompatActivity(), IWebSocketListener {

    private lateinit var osEditText: TextInputEditText
    private lateinit var deviceIDEditText: TextInputEditText
    private lateinit var hostEditText: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var unregisterButton: Button
    private lateinit var logView: RecyclerView
    private lateinit var adapter: LogAdapter
    private val dataList: MutableList<LogItem> = mutableListOf<LogItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        osEditText = findViewById(R.id.text_input_os)
        deviceIDEditText = findViewById(R.id.text_input_device_id)
        hostEditText = findViewById(R.id.text_input_host)
        registerButton = findViewById(R.id.btn_register)
        unregisterButton = findViewById(R.id.btn_unregister)
        logView = findViewById(R.id.recyclerview)
        logView.layoutManager = LinearLayoutManager(this)
        adapter = LogAdapter(dataList)
        logView.adapter = adapter


        registerButton.setOnClickListener {
            val os = osEditText.text.toString()
            val deviceID = deviceIDEditText.text.toString()
            val host = hostEditText.text.toString()
            WebSocketClient.connect(host, deviceID, os, this)
        }

        unregisterButton.setOnClickListener {
            val unregisterJson = """
                {
                    "event": "unregister",
                    "data": {
                        "os": "${osEditText.text}",
                        "device_id": "${deviceIDEditText.text}"
                    }
                }
            """.trimIndent()
            WebSocketClient.sendMessage(unregisterJson)
            WebSocketClient.disconnect()
        }
    }

    private fun smoothScrollToTopPosition(logItem: LogItem) {
        logView.post {
            dataList.add(logItem)
            adapter.notifyItemInserted(dataList.size - 1)
            logView.smoothScrollToPosition(dataList.size - 1)
        }
    }

    override fun onOpen(webSocket: WebSocket, message: String?, isAlreadyConnected: Boolean) {
        smoothScrollToTopPosition(LogItem("onOpen $message isAlreadyConnected=$isAlreadyConnected"))

        val json = """
            {
                "event": "register",
                "data": {
                    "os": "${osEditText.text}",
                    "device_id": "${deviceIDEditText.text}"
                }
            }
        """.trimIndent()
        webSocket.send(json)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val json = JSONObject(text)

        if (json.has("event") && json["event"] == "command") {

            val command = APICommand(
                clz = json["clz"].toString(),
                instance = json["instance"].toString(),
                api_name = json["api_name"].toString(),
                params = json["params"] as JSONObject,
                return_value = json["return_value"].toString(),
                thread_id = json["thread_id"].toString(),
                device_id = json["device_id"].toString()
            )

            smoothScrollToTopPosition(LogItem("call api=${command.api_name} params=${command.params}"))

            val result = CommandDispatcher.dispatch(command)

            smoothScrollToTopPosition(LogItem("result ${result?.returnValue}"))

            if (result!= null) {
                webSocket.send(result.toJSON())
            }
        } else {
//            smoothScrollToTopPosition(LogItem("onMessage $text"))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        smoothScrollToTopPosition(LogItem("onClosing code=$code, reason=$reason"))
    }
}