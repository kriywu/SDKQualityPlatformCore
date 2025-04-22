package com.wurengao.android.dispatcher

import com.wurengao.android.handler.CommandHandler
import com.wurengao.android.handler.CreateEngine
import com.wurengao.android.handler.DestroyEngine
import com.wurengao.android.handler.Plus
import com.wurengao.android.proto.APICommand
import com.wurengao.android.proto.Result

/**
 * Created by wurengao on 2025/4/22
 * @author wurengao@bytedance.com
 */
object CommandDispatcher {

    private val dispatcher = mutableMapOf<String, CommandHandler>()
    init {
        CreateEngine().apply { dispatcher[key] = this }
        Plus().apply { dispatcher[key] = this }
        DestroyEngine().apply { dispatcher[key] = this }
    }

    fun dispatch(command: APICommand): Result? {
        return dispatcher[command.api_name.lowercase()]?.handleCommand(command)
    }
}