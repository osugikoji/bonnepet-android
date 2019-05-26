package br.com.bonnepet.view.menu

import Data
import RequestCode
import Time
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.data.model.EditHostDTO
import br.com.bonnepet.util.extension.checkWriteExternalPermission
import br.com.bonnepet.util.extension.imageUrl
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.component.CircularProgressBar
import br.com.bonnepet.view.component.ImageRotationHelper
import br.com.bonnepet.view.menu.beHost.BeHostActivity
import br.com.bonnepet.view.menu.beHost.EditHostActivity
import br.com.bonnepet.view.menu.editUser.EditProfileActivity
import br.com.bonnepet.view.menu.language.LanguageActivity
import br.com.bonnepet.view.splash.SplashActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.fragment_menu.*
import java.io.File


class MenuFragment : BaseFragment() {
    override val layoutResource: Int = R.layout.fragment_menu
    override val fragmentTitle: Int = R.string.menu
    private lateinit var viewModel: MenuViewModel

    private val profileImage by lazy { profile_image }
    private val userNameTextView by lazy { user_name }

    private val beHostMenu by lazy { be_host }

    private val beHostTextMenu by lazy { text_be_host }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)
        hideActionBarDisplayHome()

        profileImage.setOnClickListener { openGallery() }
        edit_profile.setOnClickListener { startEditProfileActivity() }
        change_language.setOnClickListener { startActivity(Intent(context, LanguageActivity::class.java)) }
        exit.setOnClickListener { logout() }

        viewModel.userProfile()

        viewModel.onUserProfile.observe(this, Observer { profileDTO ->
            userNameTextView.text = profileDTO.userName
            setProfileImage(profileDTO.profileImageURL)
            setHostMenu(profileDTO.editHostDTO)
        })

        viewModel.errorMessage().observe(this, Observer { message ->
            showToast(message)
        })

    }

    private fun setHostMenu(editHostDTO: EditHostDTO?) {
        if (editHostDTO == null) {
            beHostTextMenu.text = getString(R.string.be_host)
            beHostMenu.setOnClickListener { startActivity(Intent(context, BeHostActivity::class.java)) }
        } else {
            beHostTextMenu.text = getString(R.string.host_edit_title)
            val intent = Intent(context, EditHostActivity::class.java).apply {
                putExtra(Data.EDIT_HOST_DTO, editHostDTO)
            }
            beHostMenu.setOnClickListener { startActivityForResult(intent, RequestCode.REFRESH_DATA) }
        }
    }

    /**
     *  Abre o menu de opcoes da galeria
     */
    private fun openGallery() {
        if (!activity!!.checkWriteExternalPermission()) return

        val mimeTypes = arrayOf("image/jpeg", "image/png")
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        startActivityForResult(intent, RequestCode.GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.GALLERY -> {
                    val selectedImage = ImageRotationHelper.getCorrectImageRotation(intent?.data?.imageUrl(context))
                    updateProfilePicture(selectedImage)
                }
                RequestCode.REFRESH_DATA -> {
                    viewModel.userProfile()
                }
            }
        }
    }

    private fun updateProfilePicture(selectedImage: File?) {
        val circularProgressDrawable = CircularProgressBar(activity!!)
        circularProgressDrawable.start()

        viewModel.updateProfilePicture(selectedImage)
        viewModel.onProfilePictureUpload.observe(this, Observer { pictureDTO ->
            Glide.with(this)
                .load(pictureDTO.imageURL)
                .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
                .placeholder(circularProgressDrawable)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profileImage)
        })
    }

    private fun startEditProfileActivity() {
        if (viewModel.isProfileDTOEmpty()) {
            showToast(getString(R.string.generic_request_error))
            return
        }

        val intent = Intent(context, EditProfileActivity::class.java).apply {
            putExtra(Data.PROFILE_DTO, viewModel.getProfileDTO())
        }
        startActivityForResult(intent, RequestCode.REFRESH_DATA)
    }

    private fun setProfileImage(profileURL: String?) {
        Glide.with(this)
            .load(profileURL)
            .error(R.drawable.ic_account_circle)
            .placeholder(R.drawable.ic_account_circle)
            .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .circleCrop()
            .into(profileImage)
    }

    private fun logout() {
        viewModel.clearUserSession()

        val intent = Intent(activity, SplashActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        activity?.finish()
    }
}
