package com.geidea.task.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
data class User(
    @PrimaryKey
    @SerializedName("id")
    val id: Long,

    @ColumnInfo(name = "email")
    @SerializedName("email")
    val email: String? = null,

    @ColumnInfo(name = "first_name")
    @SerializedName("first_name")
    val firstName: String? = null,

    @ColumnInfo(name = "last_name")
    @SerializedName("last_name")
    val lastName: String? = null,

    @ColumnInfo(name = "avatar")
    @SerializedName("avatar")
    val avatar: String? = null,
) : Parcelable
