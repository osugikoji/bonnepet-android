package br.com.bonnepet.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import br.com.bonnepet.R
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.main.MainActivity

class SplashActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_splash
    override val activityTitle: Nothing? = null

    companion object {
        private const val SPLASH_TIME_OUT = 2000
    }

    override fun onPrepareActivity(state: Bundle?) {
        showSplashScreen()
    }

    private fun showSplashScreen() {
        Handler().postDelayed({
            startNextActivity()
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

    private fun startNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
