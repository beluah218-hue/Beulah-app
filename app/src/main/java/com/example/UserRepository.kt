package com.example

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val userFlow: Flow<UserEntity?> = userDao.getUserFlow()

    suspend fun getUserOnce(): UserEntity? {
        return userDao.getUserOnce()
    }

    suspend fun saveUser(user: UserEntity) {
        userDao.insertOrUpdateUser(user)
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        userDao.setLoggedInState(isLoggedIn)
    }

    suspend fun logout() {
        userDao.setLoggedInState(false)
    }
}
