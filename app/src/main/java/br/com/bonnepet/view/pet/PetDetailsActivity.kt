package br.com.bonnepet.view.pet

import Data
import Time
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import br.com.bonnepet.R
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.util.component.CircularProgressBar
import br.com.bonnepet.util.data.GenderEnum
import br.com.bonnepet.util.data.PetSizeEnum
import br.com.bonnepet.view.base.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_pet_details.*


class PetDetailsActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_pet_details

    override val activityTitle = R.string.pet_details_title

    private val collapsingToolbarLayout by lazy { collapse_toolbar }

    private val appBarLayout by lazy { app_bar_layout }

    private val petImageView by lazy { image_view_pet }

    override fun onPrepareActivity(state: Bundle?) {
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.gray_600))
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.gray_100))

        val petDTO = intent.getSerializableExtra(Data.PET_DTO) as PetDTO
        setPetImage(petDTO.pictureURL)
        setFields(petDTO)

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_gray_24dp)
                    collapsingToolbarLayout.title = getString(activityTitle)
                    isShow = true
                } else if (isShow) {
                    if (petDTO.pictureURL.isEmpty()) {
                        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat
                            .getColor(this@PetDetailsActivity, R.color.gray_600))
                        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_gray_24dp)
                    } else {
                        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
                    }
                    collapsingToolbarLayout.title = petDTO.name
                    isShow = false
                }
            }
        })
    }

    private fun setPetImage(imageURL: String) {
        val progressBar = CircularProgressBar(this)
        progressBar.start()

        Glide.with(this)
            .load(imageURL)
            .error(R.drawable.ic_dog)
            .placeholder(progressBar)
            .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(petImageView)
    }

    private fun setFields(pet: PetDTO) {
        text_breed.text = pet.breed
        text_age.text = pet.birthDate

        when (pet.gender) {
            GenderEnum.MALE.name -> text_gender.text = getString(R.string.male)
            GenderEnum.FEMALE.name -> text_gender.text = getString(R.string.female)
        }

        var behaviours = ""
        pet.behaviours.forEach { behaviour ->
            behaviours = if (behaviours.isBlank()) behaviour else "$behaviours\n$behaviour"
        }
        text_behaviour.text = behaviours

        var allergies = ""
        pet.allergies.forEach { allergy ->
            allergies = if (allergies.isBlank()) allergy else "$allergies\n$allergy"
        }
        text_allergies.text = allergies

        when (pet.size) {
            PetSizeEnum.SMALL.name -> text_size.text = getString(R.string.small)
            PetSizeEnum.MEDIUM.name -> text_size.text = getString(R.string.medium)
            PetSizeEnum.LARGE.name -> text_size.text = getString(R.string.large)
        }
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
