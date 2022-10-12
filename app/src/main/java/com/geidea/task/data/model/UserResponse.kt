package com.geidea.task.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("data") val user: User,
)
