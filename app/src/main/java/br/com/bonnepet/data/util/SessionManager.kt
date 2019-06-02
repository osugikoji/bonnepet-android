package br.com.bonnepet.data.util

import Prefs
import SharedPreferencesUtil
import br.com.bonnepet.util.extension.getTokenData
import org.joda.time.DateTime

object SessionManager {

    fun createUserSession(tokenHeader: String?) {
        if (tokenHeader != null) {
            val token = tokenHeader.substringAfterLast(" ")
            SharedPreferencesUtil.putBoolean(Prefs.IS_LOGGED_IN, true)
            SharedPreferencesUtil.putString(Prefs.TOKEN, token)
        }
    }

    fun isUserAuthenticated(): Boolean {
        val token = SharedPreferencesUtil.getString(Prefs.TOKEN, null) ?: return false
        val tokenData = token.getTokenData()
        val isTokenExpired = tokenData.getLong("exp") * 1000 <= DateTime.now().millis
        if (isTokenExpired) {
            clearUserSession()
            return false
        }
        return true
    }

    fun isLoggedIn(): Boolean =
        SharedPreferencesUtil.getBoolean(Prefs.IS_LOGGED_IN)

    fun clearUserSession() {
        SharedPreferencesUtil.resetSharedPreference(Prefs.TOKEN)
        SharedPreferencesUtil.resetSharedPreference(Prefs.IS_LOGGED_IN)
    }

    fun getUserId(): Int? {
        val token = SharedPreferencesUtil.getString(Prefs.TOKEN, null) ?: return null

        val tokenData = token.getTokenData()

        return tokenData.getInt("id")
    }

    fun getAuthorizationHeader(): String? {
        val token = SharedPreferencesUtil.getString(Prefs.TOKEN) ?: return null
        return "Bearer $token"
    }
}