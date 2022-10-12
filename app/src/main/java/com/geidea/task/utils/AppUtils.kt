package com.geidea.task.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.geidea.task.R

class AppUtils {

    companion object {

        const val BASE_URL = "https://reqres.in/api/"
        const val DATABASE_NAME = "app_database"
        const val KEY_USER_ID = "UserId"

        // extension function for any activity to create progress dialog
        fun Activity.createProgressDialog(): AlertDialog {
            val builder = AlertDialog.Builder(this, R.style.WrapContentDialogStyle)
            val dialogView = layoutInflater.inflate(R.layout.dialog_progress, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            return builder.create()
        }
    }
}