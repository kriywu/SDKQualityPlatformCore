package com.wurengao.android

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import okhttp3.WebSocket

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
            WebSocketClient.disconnect()
        }
    }

    private fun smoothScrollToTopPosition(logItem: LogItem) {
        logView.post {
            dataList.add(0, logItem)
            adapter.notifyItemInserted(0)
            logView.smoothScrollToPosition(0)
        }
    }

    override fun onOpen(webSocket: WebSocket, message: String?, isAlreadyConnected: Boolean) {
        smoothScrollToTopPosition(LogItem("onOpen $message isAlreadyConnected=$isAlreadyConnected"))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        smoothScrollToTopPosition(LogItem("onClosing code=$code, reason=$reason"))
    }
}