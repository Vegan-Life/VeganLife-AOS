package com.project.veganlife.login.data.datasource

interface SnsLoginDataSource {
    suspend fun login(): String

    suspend fun logout(): String

    fun getUserInfo()
}