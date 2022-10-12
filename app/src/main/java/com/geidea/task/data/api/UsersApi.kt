package com.geidea.task.data.api

import com.geidea.task.data.model.UserResponse
import com.geidea.task.data.model.UsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {
    /* endpoint for users */
    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<UsersResponse>

    /*endpoint for single user */
    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Long,
    ): Response<UserResponse>
}