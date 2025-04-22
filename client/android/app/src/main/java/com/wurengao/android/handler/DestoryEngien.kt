package com.wurengao.android.handler

import com.wurengao.android.proto.APICommand
import com.wurengao.android.proto.Result
import com.wurengao.android.storage.InstanceStorage
import com.wurengao.android.targetsdk.MathSDK

/**
 * Created by wurengao on 2025/4/22
 * @author wurengao@bytedance.com
 */
class DestroyEngine: CommandHandler() {
    override val key = "DestroyEngine".lowercase()
    override fun handleCommand(command: APICommand): Result {
        val key = command.instance
        val engine = (InstanceStorage.remove(key) as? MathSDK)
        val code = engine?.destroyEngine() ?: -1
        return Result(code, "success", code, command.device_id)
    }

}