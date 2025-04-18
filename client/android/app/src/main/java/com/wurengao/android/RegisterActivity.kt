package com.wurengao.android

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var osEditText: TextInputEditText
    private lateinit var deviceIDEditText: TextInputEditText
    private lateinit var hostEditText: TextInputEditText
    private lateinit var registerButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        osEditText = findViewById(R.id.text_input_os)
        deviceIDEditText = findViewById(R.id.text_input_device_id)
        hostEditText = findViewById(R.id.text_input_host)
        registerButton = findViewById(R.id.btn_register)

        registerButton.setOnClickListener {
            val os = osEditText.text.toString()
            val deviceID = deviceIDEditText.text.toString()
            val host = hostEditText.text.toString()
            WebSocketClient.connect(host, deviceID, os)
        }

    }
}