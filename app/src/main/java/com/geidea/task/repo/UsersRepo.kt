package com.geidea.task.repo

import com.geidea.task.data.api.UsersApi
import com.geidea.task.data.db.AppDatabase
import com.geidea.task.data.model.User
import javax.inject.Inject

/*used to request model data from api or from database */
class UsersRepo @Inject constructor(
    private val api: UsersApi,
    private val appDatabase: AppDatabase,
) {

    suspend fun getUsers(
        page: Int,
        perPage: Int,
    ) = api.getUsers(page, perPage)

    suspend fun getUser(
        id: Long,
    ) = api.getUser(id)

    suspend fun getUsersOffline() = appDatabase.userDao().getAllUsers()

    suspend fun getUserOffline(
        id: Long,
    ) = appDatabase.userDao().getUserById(id)

    suspend fun insertUsers(users: List<User>) = appDatabase.userDao().insertUsers(users)
}