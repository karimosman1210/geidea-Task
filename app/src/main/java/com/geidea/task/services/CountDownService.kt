package com.geidea.task.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData

/* background service (bound) to start a count down timer and notify view using livedata */
class CountDownService : Service() {

    companion object {
        private const val TAG = "ContDwnSrvc"
    }

    // add 1000 milli sec to passed first sec destroy
    private val countDownTimer = object : CountDownTimer(5000 + 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            counterState.value = millisUntilFinished.toInt() / 1000
            Log.d(TAG, "onTick: ${counterState.value}")
        }

        override fun onFinish() {
            Log.d(TAG, "onFinish: ")
        }

    }

    val counterState: MutableLiveData<Int> = MutableLiveData()

    // Binder given to clients
    private val binder = LocalBinder()

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getCounterState() = counterState
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        countDownTimer.start()
        Log.d(TAG, "onCreate: ")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        countDownTimer.cancel()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        countDownTimer.cancel()
        super.onDestroy()
    }
}