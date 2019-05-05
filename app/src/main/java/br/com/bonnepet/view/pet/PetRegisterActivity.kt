package br.com.bonnepet.view.pet

import Time
import android.os.Bundle
import android.view.MenuItem
import br.com.bonnepet.R
import br.com.bonnepet.view.base.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.activity_pet_register.*

class PetRegisterActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_pet_register

    override val activityTitle = R.string.register_pet_title

    private val petImage by lazy { image_pet }

    private val btnAddImage by lazy { btn_add_image }

    override fun onPrepareActivity(state: Bundle?) {
        btnAddImage.setOnClickListener { setProfileImage(null) }
    }

    private fun setProfileImage(profileURL: String?) {
        petImage.setPadding(0,0,0,0)
        Glide.with(this)
            .load(R.drawable.splash)
            .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .circleCrop()
            .into(petImage)
    }


    /**
     *  Ação do botão de voltar da actionBar
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
