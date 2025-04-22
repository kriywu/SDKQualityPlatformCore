package com.wurengao.android.handler

import com.wurengao.android.proto.APICommand
import com.wurengao.android.proto.Result

/**
 * Created by wurengao on 2025/4/22
 * @author wurengao@bytedance.com
 */
class Plus: CommandHandler() {
    override val key = "Plus".lowercase()

    override fun handleCommand(command: APICommand): Result {
        val left = command.params.getInt("leftValue")
        val right = command.params.getInt("rightValue")
        val result = left + right
        return Result(0, "success", result, command.device_id)
    }
}