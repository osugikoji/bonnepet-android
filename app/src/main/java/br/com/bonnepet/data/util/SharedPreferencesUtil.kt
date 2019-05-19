import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {

    private const val FILE_NAME = "br.com.lardopet.prefs"

    private const val MODE = Context.MODE_PRIVATE

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(FILE_NAME, MODE)
    }

    fun getString(key: String, defaultValue: String? = ""): String? =
        preferences.getString(key, defaultValue)

    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getBoolean(key: String): Boolean =
        preferences.getBoolean(key, false)

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun resetSharedPreference(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun resetAllSharedPreference() {
        preferences.edit().clear().apply()
    }
}