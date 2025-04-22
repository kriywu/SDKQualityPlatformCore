package com.wurengao.android.handler

import com.wurengao.android.proto.APICommand
import com.wurengao.android.proto.Result

/**
 * Created by wurengao on 2025/4/22
 * @author wurengao@bytedance.com
 */
abstract class CommandHandler {
    open val key = ""

    abstract fun handleCommand(command: APICommand): Result
}





