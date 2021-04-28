package com.example.secretkeeper.data

import com.example.secretkeeper.MainApplication.Companion.applicationContext
import com.example.secretkeeper.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(password: String): Result<LoggedInUser> {
        try {
            val savedPassword = SharedPreferencesHelper.loadPassword(applicationContext())

            if (savedPassword != "" && savedPassword != password) {
                return Result.Error(Exception("Wrong Password"))
            }

            SharedPreferencesHelper.savePassword(applicationContext(), password)

            val user = LoggedInUser(password)
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}