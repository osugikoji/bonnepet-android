package br.com.bonnepet.view.menu.language

import Language
import Prefs
import SharedPreferencesUtil
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import br.com.bonnepet.R
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_language.*
import java.util.*


class LanguageActivity : BaseActivity() {
    override val layoutResource = br.com.bonnepet.R.layout.activity_language
    override val activityTitle = R.string.title_activity_language

    private val portugueseRadio by lazy { portuguese_radio_button }
    private val englishRadio by lazy { english_radio_button }

    private val btnConfirm by lazy { btn_confirm }

    private var language: String? = null

    override fun onPrepareActivity(state: Bundle?) {
        language = currentLanguage()

        if (language == Language.PORTUGUESE) portugueseRadio.isChecked = true
        if (language == Language.ENGLISH) englishRadio.isChecked = true

        portugueseRadio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) englishRadio.isChecked = false
        }
        englishRadio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) portugueseRadio.isChecked = false
        }

        btnConfirm.setOnClickListener { switchLanguage() }
    }

    private fun currentLanguage(): String? {
        return SharedPreferencesUtil.getString(Prefs.SELECTED_LANGUAGE, Locale.getDefault().language)
    }

    private fun switchLanguage() {
        if (portugueseRadio.isChecked) {
            SharedPreferencesUtil.putString(Prefs.SELECTED_LANGUAGE, Language.PORTUGUESE)
        } else if (englishRadio.isChecked) {
            SharedPreferencesUtil.putString(Prefs.SELECTED_LANGUAGE, Language.ENGLISH)
        }
        restartApp()
    }

    private fun restartApp() {
        val intent = Intent(this, SplashActivity::class.java)
        this.startActivity(intent)
        this.finishAffinity()
    }

    /**
     *  Acao do botao de voltar da actionBar
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
