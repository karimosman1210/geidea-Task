package com.geidea.task.viewmodel.user_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geidea.task.repo.UsersRepo

class UserDetailsViewModelFactory(
    private val repo: UsersRepo
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailsViewModel::class.java)) {
            return UserDetailsViewModel(repo) as T
        }
        throw IllegalArgumentException("UserDetailsViewModel can't be created")
    }
}