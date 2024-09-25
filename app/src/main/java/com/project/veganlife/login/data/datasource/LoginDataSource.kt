package com.project.veganlife.login.data.datasource

interface LoginDataSource {
    suspend fun login(): String

    suspend fun logout(): String

    fun getUserInfo()
}