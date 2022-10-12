package com.geidea.task.viewmodel.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.geidea.task.data.model.Result
import com.geidea.task.data.model.User
import com.geidea.task.data.model.UsersResponse
import com.geidea.task.repo.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repo: UsersRepo,
) : ViewModel() {

    fun getUsers(
        page: Int,
        isNetworkAvailable: Boolean,
    ) = liveData(Dispatchers.IO) {

        emit(
            Result.loading(
                message = "Request Status Is Loading"
            )
        )
// check connection to check get data from api server or room db
        if (isNetworkAvailable) {
            try {
                val response = repo.getUsers(page, 8)

                val responseBody = response.body()
                val statusCode = response.code()

                if (response.isSuccessful && responseBody != null) {
                    emit(
                        Result.complete(
                            data = responseBody,
                            statusCode = statusCode,
                            message = "Request Status Is Complete",
                        )
                    )
                } else {
                    emit(
                        Result.error(
                            statusCode = statusCode,
                            message = "Request Status Is Error with response message: ${response.message()}",
                        )
                    )
                }
            } catch (exception: Exception) {
                emit(
                    Result.error(
                        statusCode = null,
                        message = "Request Status Is Error with exception message: ${exception.message}",
                    )
                )
            }
        } else {
            try {
                // make map to make online and offline data same response
                val users = repo.getUsersOffline()
                val usersResponse = UsersResponse(0, 0, 0, 0, users)

                emit(
                    Result.complete(
                        data = usersResponse,
                        statusCode = 200,
                        message = "Request Status Is Complete",
                    )
                )
            } catch (exception: Exception) {
                emit(
                    Result.error(
                        statusCode = null,
                        message = "Request Status Is Error with exception message: ${exception.message}",
                    )
                )
            }
        }
    }

    fun insertUsers(users: List<User>) {
        viewModelScope.launch {
            repo.insertUsers(users)
        }
    }

}