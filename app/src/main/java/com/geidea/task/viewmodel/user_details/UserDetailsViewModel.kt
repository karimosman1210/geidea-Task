package com.geidea.task.viewmodel.user_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.geidea.task.data.model.Result
import com.geidea.task.data.model.UserResponse
import com.geidea.task.repo.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val repo: UsersRepo
) : ViewModel() {

    fun getUser(
        id: Long,
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
                val response = repo.getUser(id)

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

                val user = repo.getUserOffline(id)
                val userResponse = UserResponse(user)

                emit(
                    Result.complete(
                        data = userResponse,
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

}