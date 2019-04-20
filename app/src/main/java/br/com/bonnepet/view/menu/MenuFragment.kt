package br.com.bonnepet.view.menu

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.menu.language.LanguageActivity
import br.com.bonnepet.view.splash.SplashActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import kotlinx.android.synthetic.main.fragment_menu.*


class MenuFragment : BaseFragment() {
    override val layoutResource: Int = R.layout.fragment_menu
    override val fragmentTitle: Int = R.string.menu

    private val profileImage by lazy { profile_image }

    private val userNameTextView by lazy { user_name }

    private val changeLanguageMenu by lazy { change_language }

    private lateinit var viewModel: MenuViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)
        hideActionBarDisplayHome()

        changeLanguageMenu.setOnClickListener { nextActivity() }
        exit.setOnClickListener { logout() }

        viewModel.userProfile()
        viewModel.onUserProfile.observe(this, Observer {
            userNameTextView.text = it.userName
            setProfileImage(it.profileURL)
        })
    }

    private fun setProfileImage(profileURL: String?) {
        Glide.with(this)
            .load(profileURL)
            .error(R.drawable.ic_account_circle)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(profileImage)
    }

    private fun nextActivity() {
        startActivity(Intent(context, LanguageActivity::class.java))
    }

    private fun logout() {
        viewModel.clearUserSession()

        val intent = Intent(activity, SplashActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}
