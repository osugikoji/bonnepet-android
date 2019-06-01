package br.com.bonnepet.view.splash

import Data
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.data.model.ProfileDTO
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.main.MainActivity

class SplashActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_splash
    override val activityTitle: Nothing? = null
    private lateinit var viewModel: SplashViewModel

    companion object {
        private const val SPLASH_TIME_DELAY = 2000
    }

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        showSplashScreen()

        viewModel.sessionExpired().observe(this, Observer {
            if (it) startNextActivity(null)
        })

        viewModel.errorMessage().observe(this, Observer {
            showToast(it)
            startNextActivity(null)
        })
    }

    private fun showSplashScreen() {
        Handler().postDelayed({
            checkUserAuthentication()
        }, SPLASH_TIME_DELAY.toLong())
    }

    private fun checkUserAuthentication() {
        if (viewModel.isUserAuthenticated()) {
            viewModel.userProfile()
            viewModel.onUserProfile.observe(this, Observer { profileDTO ->
                startNextActivity(profileDTO)
            })
        } else startNextActivity(null)
    }

    private fun startNextActivity(profileDTO: ProfileDTO?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(Data.PROFILE_DTO, profileDTO)
        }
        startActivity(intent)
        finish()
    }
}
