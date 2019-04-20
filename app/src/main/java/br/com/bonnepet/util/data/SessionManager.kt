package br.com.bonnepet.util.data

import Prefs
import SharedPreferencesUtil
import br.com.bonnepet.data.model.UserDetails
import br.com.bonnepet.util.extension.getTokenData

object SessionManager {

    fun createUserSession(tokenHeader: String?) {
        if (tokenHeader != null) {
            val token = tokenHeader.substringAfterLast(" ")
            SharedPreferencesUtil.putBoolean(Prefs.IS_LOGGED_IN, true)
            SharedPreferencesUtil.putString(Prefs.TOKEN, token)
        }
    }

    fun isLoggedIn(): Boolean =
        SharedPreferencesUtil.getBoolean(Prefs.IS_LOGGED_IN)

    fun clearUserSession() {
        SharedPreferencesUtil.resetSharedPreference(Prefs.TOKEN)
        SharedPreferencesUtil.resetSharedPreference(Prefs.IS_LOGGED_IN)
    }

    fun getUserDetails(): UserDetails? {
        val token = SharedPreferencesUtil.getString(Prefs.TOKEN) ?: return null

        val tokenData = token.getTokenData()

        val id = tokenData.getInt("id")
        val email = tokenData.getString("email")
        val profile = tokenData.getString("profile")

        return UserDetails(id, email, profile)
    }

    fun getAuthorizationHeader(): String? {
        val token = SharedPreferencesUtil.getString(Prefs.TOKEN) ?: return null
        return "Bearer $token"
    }
}