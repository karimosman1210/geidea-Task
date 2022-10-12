package com.geidea.task.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geidea.task.data.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}