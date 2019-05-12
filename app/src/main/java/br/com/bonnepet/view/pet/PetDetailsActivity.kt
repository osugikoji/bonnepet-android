package br.com.bonnepet.view.pet

import Data
import Time
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.GenderEnum
import br.com.bonnepet.data.enums.PetBehaviourEnum
import br.com.bonnepet.data.enums.PetSizeEnum
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.util.extension.formatToPetAge
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.component.CircularProgressBar
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
                    if (petDTO.pictureURL.isNullOrEmpty()) {
                        collapsingToolbarLayout.setExpandedTitleColor(
                            ContextCompat
                                .getColor(this@PetDetailsActivity, R.color.gray_600)
                        )
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

    private fun setPetImage(imageURL: String?) {
        if (imageURL == null) return
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
        text_age.text = pet.birthDate?.formatToPetAge(this)

        when (pet.gender) {
            GenderEnum.MALE.name -> text_gender.text = getString(R.string.male)
            GenderEnum.FEMALE.name -> text_gender.text = getString(R.string.female)
        }

        var behaviours = ""
        pet.behaviours.forEach { behaviour ->
            var formatBehaviour = ""
            when (behaviour) {
                PetBehaviourEnum.SOCIABLE.name -> formatBehaviour = getString(PetBehaviourEnum.SOCIABLE.description)
                PetBehaviourEnum.SHY.name -> formatBehaviour = getString(PetBehaviourEnum.SHY.description)
                PetBehaviourEnum.INDEPENDENT.name -> formatBehaviour =
                    getString(PetBehaviourEnum.INDEPENDENT.description)
                PetBehaviourEnum.CONFIDENT.name -> formatBehaviour = getString(PetBehaviourEnum.CONFIDENT.description)
                PetBehaviourEnum.AGGRESSIVE.name -> formatBehaviour = getString(PetBehaviourEnum.AGGRESSIVE.description)
            }
            behaviours = if (behaviours.isBlank()) formatBehaviour else "$behaviours\n$formatBehaviour"
        }

        text_behaviour.text = behaviours

        when (pet.size) {
            PetSizeEnum.SMALL.name -> text_size.text = getString(R.string.small)
            PetSizeEnum.MEDIUM.name -> text_size.text = getString(R.string.medium)
            PetSizeEnum.LARGE.name -> text_size.text = getString(R.string.large)
        }

        if (pet.observations.isEmpty()) layout_observations.isVisible = false
        else text_observations.text = pet.observations
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
