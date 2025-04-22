package com.wurengao.android.proto

import org.json.JSONObject

/**
 * Created by wurengao on 2025/4/22
 * @author wurengao@bytedance.com
 */
data class APICommand(
    val clz: String,
    val instance: String,
    val api_name: String,
    val params: JSONObject,
    val return_value: String,
    val thread_id: String,
    val device_id: String
) {

}

data class Result(
    val code: Int,
    val message: String,
    val returnValue: Any,
    val device_id: String
) {
    private val event = "command"
    fun toJSON(): String {
        return "{" +
                "\"code\":$code," +
                "\"message\":\"$message\"," +
                "\"event\":\"$event\"," +
                "\"data\":\"$returnValue\"," +
                "\"device_id\":\"$device_id\"" +
                "}"
    }
}