package com.geidea.task.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.*

class LanguageContextWrapper(private val context: Context) : ContextWrapper(context) {

    fun wrap(newLocale: Locale): ContextWrapper {
        val resources = context.resources
        val configuration = resources.configuration

        val mContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale)
            val localeList = LocaleList(newLocale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            context.createConfigurationContext(configuration)
        } else {
            configuration.setLocale(newLocale)
            context.createConfigurationContext(configuration)
        }

        return ContextWrapper(mContext)
    }

}