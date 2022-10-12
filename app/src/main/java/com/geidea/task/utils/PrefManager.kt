package com.geidea.task.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

/* SharedPreference to save localization select */
class PrefManager @Inject constructor(context: Context) {

    companion object {
        private const val TAG = "PrfMangr"
        private const val prefName = "geidea"
        private const val keyLanguageCode = "LanguageCode"
        const val ar = "ar"
        const val en = "en"
    }

    private val pref = context.getSharedPreferences(prefName, MODE_PRIVATE)

    fun getLanguageCode(): String {
        return pref.getString(keyLanguageCode, en) ?: en
    }

    fun setLanguageCode(languageCode: String) {
        pref.edit().putString(keyLanguageCode, languageCode).apply()
    }
}