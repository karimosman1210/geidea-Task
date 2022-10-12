package com.geidea.task

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/* required by hilt */
@HiltAndroidApp
class BaseApp : Application()