package com.geidea.task.view.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geidea.task.R
import com.geidea.task.adapter.UsersAdapter
import com.geidea.task.data.model.Status
import com.geidea.task.data.model.User
import com.geidea.task.databinding.ActivityUsersBinding
import com.geidea.task.utils.AppUtils.Companion.KEY_USER_ID
import com.geidea.task.utils.AppUtils.Companion.createProgressDialog
import com.geidea.task.utils.NetworkHelper
import com.geidea.task.utils.PrefManager
import com.geidea.task.utils.PrefManager.Companion.ar
import com.geidea.task.utils.VerticalListItemMargin
import com.geidea.task.view.base_views.BaseActivity
import com.geidea.task.view.language.ChooseLanguageSheet
import com.geidea.task.viewmodel.users.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import javax.inject.Inject

@AndroidEntryPoint
class UsersActivity : BaseActivity(),
    UsersAdapter.OnUserClickListener {

    companion object {
        private const val TAG = "UsersActivity"
    }

    private lateinit var viewBinding: ActivityUsersBinding

    private lateinit var progressDialog: AlertDialog

    @Inject
    lateinit var prefManager: PrefManager

    private val usersViewModel: UsersViewModel by viewModels()

    private lateinit var users: MutableList<User>
    private lateinit var usersAdapter: UsersAdapter

    private var pageNumber = 1
    private var isLastPage = false
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        // instance of dialog
        progressDialog = createProgressDialog()

        initViews()
    }

    // restart app after change language
    private fun changeLanguage(languageCode: String) {
        prefManager.setLanguageCode(languageCode)

        val intent = Intent(this, UsersActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun initViews() {
        viewBinding.ivChangeLanguage.setOnClickListener {
            val languageFragment = ChooseLanguageSheet()
            languageFragment.setLanguageSelectListener(object :
                ChooseLanguageSheet.OnLanguageSelectListener {
                override fun onLanguageSelect(languageCode: String) {
                    changeLanguage(languageCode)
                }
            })

            languageFragment.show(
                supportFragmentManager,
                languageFragment::class.java.simpleName
            )
        }

        pageNumber = 1
        users = mutableListOf()

        usersAdapter = UsersAdapter(
            this,
            mutableListOf(),
            this
        )

        val numOfColumns = 1
        val itemMargin = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._15sdp)

        viewBinding.rvUsers.addItemDecoration(
            VerticalListItemMargin(
                numOfColumns,
                itemMargin,
                prefManager.getLanguageCode() == ar
            )
        )

        viewBinding.rvUsers.layoutManager =
            LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )

        viewBinding.rvUsers.adapter = usersAdapter

        viewBinding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading && !isLastPage && users.isNotEmpty()) {
                    if (layoutManager?.findLastCompletelyVisibleItemPosition() == users.size - 1) {
                        pageNumber += 1
                        getUsers()
                    }
                }
            }
        })


        viewBinding.swipeRefresh.setOnRefreshListener {
            pageNumber = 1
            isLastPage = false
            isLoading = false

            users.clear()
            usersAdapter.clearList()

            getUsers()

            viewBinding.swipeRefresh.isRefreshing = false
        }

        getUsers()
    }

    // observe from vm
    private fun getUsers() {
        val isNetworkAvailable = NetworkHelper(applicationContext).isNetworkAvailable()

        usersViewModel.getUsers(pageNumber, isNetworkAvailable).observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.show()
                    isLoading = true
                    Log.d(TAG, "getUsers: LOADING...")
                    Log.d(TAG, "getUsers: ${it.message}")
                }
                Status.COMPLETE -> {
                    progressDialog.dismiss()
                    isLoading = false
                    Log.d(TAG, "getUsers: COMPLETE")
                    Log.d(TAG, "getUsers: ${it.statusCode}")
                    Log.d(TAG, "getUsers: ${it.message}")

                    it.data?.let { apiResponse ->
                        if (isNetworkAvailable && apiResponse.total > 0) {
                            if (apiResponse.data.isNotEmpty()) {
                                users.addAll(apiResponse.data)
                                usersAdapter.addItems(apiResponse.data)

                                usersViewModel.insertUsers(apiResponse.data)
                            }

                            if (apiResponse.totalPages == pageNumber) {
                                isLastPage = true
                            }
                        } else if (!isNetworkAvailable && apiResponse.data.isNotEmpty()) {
                            users.clear()
                            usersAdapter.clearList()

                            users.addAll(apiResponse.data)
                            usersAdapter.addItems(apiResponse.data)

                            isLastPage = true
                        } else {
                            Toasty.info(
                                this,
                                getString(R.string.no_users_msg),
                                Toast.LENGTH_SHORT,
                                true
                            ).show()
                        }
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    isLoading = false
                    Log.e(TAG, "getUsers: ERROR")
                    Log.e(TAG, "getUsers: ${it.statusCode}")
                    Log.e(TAG, "getUsers: ${it.message}")

                    Toasty.error(
                        this,
                        getString(R.string.something_wrong),
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }
        }
    }

    override fun onUserClick(item: User) {
        val intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra(KEY_USER_ID, item.id)
        startActivity(intent)
    }

}