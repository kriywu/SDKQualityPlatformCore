package com.wurengao.android

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import okhttp3.Response
import okhttp3.WebSocket

class RegisterActivity : AppCompatActivity() {

    private lateinit var osEditText: TextInputEditText
    private lateinit var deviceIDEditText: TextInputEditText
    private lateinit var hostEditText: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var logView: RecyclerView
    private val dataList: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        osEditText = findViewById(R.id.text_input_os)
        deviceIDEditText = findViewById(R.id.text_input_device_id)
        hostEditText = findViewById(R.id.text_input_host)
        registerButton = findViewById(R.id.btn_register)
        logView = findViewById(R.id.recyclerview)
        logView.setLayoutManager(LinearLayoutManager(this));

        logView.adapter = LogAdapter(dataList)


        registerButton.setOnClickListener {
            val os = osEditText.text.toString()
            val deviceID = deviceIDEditText.text.toString()
            val host = hostEditText.text.toString()
            WebSocketClient.connect(host, deviceID, os, object : IWebSocketListener {
                override fun onOpen(webSocket: WebSocket, response: Response) {

                }

                override fun onMessage(webSocket: WebSocket, text: String) {

                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {

                }

            })
        }

    }
}