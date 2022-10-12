package com.geidea.task.viewmodel.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geidea.task.repo.UsersRepo

class UsersViewModelFactory(
    private val repo: UsersRepo
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            return UsersViewModel(repo) as T
        }
        throw IllegalArgumentException("UsersViewModel can't be created")
    }
}