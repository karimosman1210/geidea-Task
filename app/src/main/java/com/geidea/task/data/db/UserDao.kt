package com.geidea.task.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geidea.task.data.model.User

@Dao
interface UserDao {
    /*Query all users*/
    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>

    /*Query single user*/
    @Query("SELECT * FROM user WHERE id=:id ")
    suspend fun getUserById(id: Long): User

    /*insert list of users*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)
}