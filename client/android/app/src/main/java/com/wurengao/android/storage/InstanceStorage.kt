package com.wurengao.android.storage

/**
 * Created by wurengao on 2025/4/22
 * @author wurengao@bytedance.com
 */
object InstanceStorage {

    private val instanceMap: MutableMap<String, Any> = mutableMapOf<String, Any>()

    public fun add(key: String, instance: Any) {
        instanceMap[key] = instance
    }

    public fun get(key: String): Any? {
        return instanceMap[key]
    }

    public fun remove(key: String): Any? {
        return instanceMap.remove(key)
    }
}