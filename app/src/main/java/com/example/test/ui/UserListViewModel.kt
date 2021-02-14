package com.example.test.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.api.ApiInterface
import com.example.test.model.User
import com.example.test.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UserListViewModel(
    private val api: ApiInterface
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _error = SingleLiveEvent<Exception>()
    val error: LiveData<Exception> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        loadUsers()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun loadUsers() {
        viewModelScope.launch {
            try {
                _loading.value = true

                val initData = api.getAllUsers()

                val lastPage = initData.meta.pagination.pages

                val result = api.getAllUsers(page = lastPage)

                _users.value = result.data

            } catch (e: Exception) {
                _error.value = e
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            try {
                _loading.value = true

                api.deleteUser(user.id)

                val allUsers = _users.value?.toMutableList() ?: return@launch

                allUsers.remove(user)

                _users.value = allUsers
            } catch (e: Exception) {
                _error.value = e
            } finally {
                _loading.value = false
            }
        }
    }

    fun createNewUser(name: String, email: String) {
        viewModelScope.launch {

            val newUser = User(
                name = name,
                email = email,
                created_at = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss").format(Calendar.getInstance().time),
                gender = "Male",
                status = "Active"
            )

            try {
                _loading.value = true

                api.createUser(newUser)

                val allUsers = _users.value?.toMutableList() ?: mutableListOf()

                allUsers.add(newUser)

                _users.value = allUsers
            } catch (e: Exception) {
                _error.value = e
            } finally {
                _loading.value = false
            }
        }
    }
}