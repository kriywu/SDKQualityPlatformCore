package com.wurengao.android

/**
 * Created by wurengao on 2025/4/21
 * @author wurengao@bytedance.com
 */
data class LogItem(val message: String) {
    private var time: String = ""

    init {
        val currentTime = System.currentTimeMillis()
        val date = java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Date(currentTime))
        time = date
    }

    override fun toString(): String {
        return "$time $message"
    }
}