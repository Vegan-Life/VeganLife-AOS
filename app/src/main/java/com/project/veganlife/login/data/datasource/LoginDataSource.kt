package com.project.veganlife.login.data.datasource

interface LoginDataSource {
    suspend fun login(): String

    fun logout(): String

    fun getUserInfo()
}