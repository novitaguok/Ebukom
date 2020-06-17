package com.ebukom.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.reflect.KClass

class PreferenceUtils {
    private var sp: SharedPreferences

    constructor(context: Context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context)
    }

    constructor(context: Context, name: String) : this(context) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun set(key: String, value: Any?) {
        sp[key] = value
    }

    fun remove(key: String) {
        sp.edit().remove(key).apply()
    }

    fun clear() {
        sp.edit().clear().apply()
    }

    /**
     * how to use: get(String::class, key, default)
     */
    fun get(kClass: KClass<*>, key: String, defaultValue: Any? = null): Any? {
        return when (kClass) {
            String::class -> sp.getString(key, defaultValue as? String)
            Int::class -> sp.getInt(key, defaultValue as? Int ?: -1)
            Boolean::class -> sp.getBoolean(key, defaultValue as? Boolean ?: false)
            Float::class -> sp.getFloat(key, defaultValue as? Float ?: -1f)
            Long::class -> sp.getLong(key, defaultValue as? Long ?: -1)
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    private operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}
