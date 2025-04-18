package com.wurengao.android

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

/**
 * Created by wurengao on 2025/4/18
 * @author wurengao@bytedance.com
 */


object WebSocketClient {
    private const val TAG = "WebSocketClient"
    private lateinit var webSocket: WebSocket
    private val client = OkHttpClient()
    private var isConnected = false

    fun connect(host: String, did: String, os: String) {
        if (isConnected) {
            Log.d(TAG, "connect: already connected")
            return
        }
        val url = "ws://$host/websocket"
        Log.d(TAG, "connect: $url")
        val request: Request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket Opened")
                isConnected = true
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Received message: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d(TAG, "Received bytes: " + bytes.hex())
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket Closing: $reason")
                webSocket.close(1000, null)
                isConnected = false
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket Error: " + t.message)
            }
        })
        client.dispatcher.executorService.shutdown()
    }

    fun sendMessage(message: String?) {
        webSocket.send(message!!)
    }

    fun disconnect() {
        webSocket.close(1000, "Disconnect")
    }
}