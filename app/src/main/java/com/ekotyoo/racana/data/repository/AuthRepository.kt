package com.ekotyoo.racana.data.repository

import com.ekotyoo.racana.data.datasource.local.UserPreferencesDataStore
import com.ekotyoo.racana.data.datasource.remote.AuthApi
import com.ekotyoo.racana.data.model.UserModel
import dagger.hilt.android.scopes.ViewModelScoped
import com.ekotyoo.racana.data.Result
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
) {
    val userData = userPreferencesDataStore.getUserData()

    suspend fun login(email: String, password: String): Result<UserModel> {
        try {
            val response = authApi.login(email, password)
            val body = response.body()
            return if (response.isSuccessful && body != null) {
                val user = UserModel(
                    body.user.id,
                    body.user.name,
                    body.user.email,
                    body.token
                )
                userPreferencesDataStore.saveUserData(user)
                Timber.d("Success: $body")
                Result.Success(user)
            } else {
                Result.Error("Login Failed", null)
            }
        } catch (e: IOException) {
            Timber.d("Error: " + e.message)
            return Result.Error("Terjadi kesalahan, coba lagi nanti.", null)
        } catch (e: HttpException) {
            Timber.d("Error: " + e.message)
            return Result.Error("Email atau Password harus sesuai.", null)
        }
    }
}