package com.wurengao.android.handler

import com.wurengao.android.proto.APICommand
import com.wurengao.android.proto.Result
import com.wurengao.android.storage.InstanceStorage
import com.wurengao.android.targetsdk.MathSDK

/**
 * Created by wurengao on 2025/4/22
 * @author wurengao@bytedance.com
 */
class CreateEngine: CommandHandler() {
    override val key = "CreateEngine".lowercase()

    override fun handleCommand(command: APICommand): Result {
        val engine = MathSDK.createEngine()
        InstanceStorage.add(command.return_value, engine)
        return Result(0, "success", command.return_value, command.device_id)
    }
}