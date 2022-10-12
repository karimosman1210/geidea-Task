package com.geidea.task.view.users

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.geidea.task.R
import com.geidea.task.data.model.Status
import com.geidea.task.data.model.User
import com.geidea.task.databinding.ActivityUserDetailsBinding
import com.geidea.task.services.CountDownService
import com.geidea.task.utils.AppUtils.Companion.KEY_USER_ID
import com.geidea.task.utils.AppUtils.Companion.createProgressDialog
import com.geidea.task.utils.NetworkHelper
import com.geidea.task.view.base_views.BaseActivity
import com.geidea.task.viewmodel.user_details.UserDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class UserDetailsActivity : BaseActivity() {

    companion object {
        private const val TAG = "UserDtlsAct"
    }

    private lateinit var viewBinding: ActivityUserDetailsBinding

    private lateinit var progressDialog: AlertDialog

    private val userDetailsViewModel: UserDetailsViewModel by viewModels()

    private var userId: Long = -1
    private lateinit var user: User

    private val animationTimeMillis: Long = 2000

    private var mBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as CountDownService.LocalBinder
            binder.getCounterState().observe(this@UserDetailsActivity) {
                Log.d(TAG, "onServiceConnected: $it")
                mBound = true

                viewBinding.tvCounter.text = it.toString()

                if (it <= 0) {
                    finish()
                }
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.i(TAG, "onServiceDisconnected: ")
            mBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mBound) {
            unbindService(connection)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // instance of dialog
        progressDialog = createProgressDialog()

        userId = intent.getLongExtra(KEY_USER_ID, -1)
        if (userId != -1L) {
            getUser(userId)
        } else {
            finish()
            Toasty.error(
                this,
                getString(R.string.something_wrong),
                Toast.LENGTH_SHORT,
                true
            ).show()
        }

    }
    // observe from vm
    private fun getUser(id: Long) {
        val isNetworkAvailable = NetworkHelper(applicationContext).isNetworkAvailable()

        userDetailsViewModel.getUser(id, isNetworkAvailable).observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.show()
                    Log.d(TAG, "getUser: LOADING...")
                    Log.d(TAG, "getUser: ${it.message}")
                }
                Status.COMPLETE -> {
                    progressDialog.dismiss()
                    Log.d(TAG, "getUser: COMPLETE")
                    Log.d(TAG, "getUser: ${it.statusCode}")
                    Log.d(TAG, "getUser: ${it.message}")

                    it.data?.let { apiResponse ->
                        user = apiResponse.user
                        initViews()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    Log.e(TAG, "getUser: ERROR")
                    Log.e(TAG, "getUser: ${it.statusCode}")
                    Log.e(TAG, "getUser: ${it.message}")

                    Toasty.error(
                        this,
                        getString(R.string.something_wrong),
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                    finish()

                }
            }
        }
    }

    private fun initViews() {
        viewBinding.parentLayout.visibility = View.VISIBLE

        viewBinding.ivBack.setOnClickListener {
            finish()
        }
        // used to hide animation image after 2 sec
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            viewBinding.animationView.visibility = View.GONE
        }
        handler.postDelayed(runnable, animationTimeMillis)


        Glide.with(this)
            .load(user.avatar)
            .placeholder(R.drawable.ic_logo)
            .error(R.drawable.ic_logo)
            .into(viewBinding.ivUserImage)

        viewBinding.tvUserId.text = user.id.toString()
        val name = "${user.firstName} ${user.lastName}"
        viewBinding.tvUserName.text = name
        viewBinding.tvUserEmail.text = user.email


        // Bind to LocalService
        Intent(this, CountDownService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }
}