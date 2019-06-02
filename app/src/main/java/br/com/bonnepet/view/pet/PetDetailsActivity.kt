package br.com.bonnepet.view.pet

import Data
import RequestCode
import Time
import android.app.Activity
import android.content.Intent
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
import br.com.bonnepet.util.extension.setSafeOnClickListener
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.component.CircularProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.activity_pet_details.*


class PetDetailsActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_pet_details

    override val activityTitle: Nothing? = null

    private val collapsingToolbarLayout by lazy { collapse_toolbar }

    private val petImageView by lazy { image_view_pet }

    private val btnEditPet by lazy { register_pet_btn }

    private lateinit var petDTO: PetDTO

    override fun onPrepareActivity(state: Bundle?) {
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.gray_600))
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.gray_100))

        petDTO = intent.getSerializableExtra(Data.PET_DTO) as PetDTO

        val canEditPet = intent.getBooleanExtra(Data.CAN_EDIT_PET, true)
        btnEditPet.isVisible = canEditPet

        setPetImage(petDTO.pictureURL)
        setFields(petDTO)

        btnEditPet.setSafeOnClickListener {
            startPetEditActivity()
        }
        showSupportActionBarTitle()
    }

    private fun startPetEditActivity() {
        val intent = Intent(this, PetEditActivity::class.java).apply {
            putExtra(Data.PET_DTO, petDTO)
        }
        startActivityForResult(intent, RequestCode.REFRESH_DATA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.REFRESH_DATA -> {
                    val bundle = data?.extras
                    val petDTO = bundle?.getSerializable(Data.PET_EDIT_DTO) as PetDTO
                    this.petDTO = petDTO
                    if (!petDTO.pictureURL.isNullOrEmpty()) setPetImage(petDTO.pictureURL)
                    setFields(petDTO)
                    SharedPreferencesUtil.putBoolean(Prefs.FETCH_PET_FRAGMENT, true)
                    setResult(Activity.RESULT_OK)
                }
            }
        }
    }

    private fun setPetImage(imageURL: String?) {
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
        supportActionBar?.title = pet.name
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

        layout_observations.isVisible = pet.observations.isNotEmpty()
        text_observations.text = pet.observations
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
