package com.geidea.task.view.base_views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.geidea.task.utils.LanguageContextWrapper
import com.geidea.task.utils.PrefManager
import java.util.*

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        // .. create or get your new Locale object here.

        newBase?.let {
            val prefManager = PrefManager(it)

            val newLocale = Locale(prefManager.getLanguageCode())

            val context = LanguageContextWrapper(it).wrap(newLocale)

            super.attachBaseContext(context)
        }
    }
}