package br.com.lardopet.session

import Prefs
import SharedPreferencesUtil
import br.com.lardopet.util.getTokenData

object SessionManager {

    fun createUserSession(token: String) {
        SharedPreferencesUtil.putBoolean(Prefs.PREFERENCE_IS_LOGGED_IN, true)
        SharedPreferencesUtil.putString(Prefs.PREFERENCE_TOKEN, token)
    }

    fun isLoggedIn(): Boolean =
        SharedPreferencesUtil.getBoolean(Prefs.PREFERENCE_IS_LOGGED_IN)

    fun clearUserSession() {
        SharedPreferencesUtil.resetSharedPreference(Prefs.PREFERENCE_TOKEN)
        SharedPreferencesUtil.resetSharedPreference(Prefs.PREFERENCE_IS_LOGGED_IN)

//        val intent = Intent(context, LoginActivity::class.java).apply {
//            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        }
//        context.startActivity(intent)
    }

    fun getUserDetails(): UserDetails? {
        val token = SharedPreferencesUtil.getString(Prefs.PREFERENCE_TOKEN) ?: return null

        val tokenData = token.getTokenData()

        val id = tokenData.getInt("id")
        val email = tokenData.getString("email")
        val profile = tokenData.getString("profile")

        return UserDetails(id, email, profile)
    }
}