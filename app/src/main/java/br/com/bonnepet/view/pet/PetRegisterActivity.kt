package br.com.bonnepet.view.pet

import Mask
import RequestCode
import Time
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.GenderEnum
import br.com.bonnepet.data.enums.PetSizeEnum
import br.com.bonnepet.data.enums.TextTypeEnum
import br.com.bonnepet.util.extension.*
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.component.CheckBoxDialog
import br.com.bonnepet.view.component.ImageRotationHelper
import br.com.bonnepet.view.component.MaskEditText
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.activity_pet_register.*
import java.io.File

class PetRegisterActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_pet_register
    private lateinit var viewModel: PetRegisterViewModel

    override val activityTitle = R.string.register_pet_title

    private val petImage by lazy { image_pet }
    private val textPetName by lazy { input_pet_name }
    private val textPetBreed by lazy { input_pet_breed }
    private val textPetGender by lazy { input_pet_gender }
    private val textPetBehaviour by lazy { input_behaviour }
    private val textPetBirthDate by lazy { input_pet_birth_date }
    private val textPetSize by lazy { input_pet_size }
    private val textPetObservations by lazy { input_observations }

    private val btnAddImage by lazy { btn_add_image }
    private val btnSave by lazy { btn_save }

    private var selectedImage: File? = null

    private lateinit var inputDateMask: TextWatcher

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(PetRegisterViewModel::class.java)

        btnAddImage.setSafeOnClickListener { openGallery() }
        btnSave.setSafeOnClickListener { requestRegister() }

        buildPetBreedAdapter()
        buildPetGenderAdapter()
        buildPetSizeAdapter()

        val items = (resources.getStringArray(R.array.behaviour_list))
        textPetBehaviour.setOnClickListener { CheckBoxDialog(this, items, it as EditText) }

        inputDateMask = MaskEditText(textPetBirthDate, Mask.DATE_MASK)
        textPetBirthDate.addTextChangedListener(inputDateMask)

        viewModel.isLoading().observe(this, Observer { isLoading ->
            progressDialogVisibility(isLoading)
        })

        viewModel.onPetRegisterSuccess.observe(this, Observer { message ->
            setResult(Activity.RESULT_OK)
            showToast(message)
            finish()
        })

        viewModel.errorMessage().observe(this, Observer { message ->
            showToast(message)
        })
    }

    private fun buildPetBreedAdapter() {
        val items = (resources.getStringArray(R.array.breed_list)).toList()
        val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items)
        textPetBreed.setAdapter(adapter)
    }

    private fun buildPetGenderAdapter() {
        val items = ArrayList(
            arrayOf(
                getString(GenderEnum.MALE.description),
                getString(GenderEnum.FEMALE.description)
            ).toList()
        )
        val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items)
        textPetGender.setAdapter(adapter)
    }

    private fun buildPetSizeAdapter() {
        val items = ArrayList(
            arrayOf(
                getString(PetSizeEnum.SMALL.description),
                getString(PetSizeEnum.MEDIUM.description),
                getString(PetSizeEnum.LARGE.description)
            ).toList()
        )
        val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items)
        textPetSize.setAdapter(adapter)
    }

    private fun requestRegister() {
        if (validateInputs()) {
            viewModel.registerPet(
                textPetName.text.toString(),
                textPetBreed.text.toString(),
                textPetGender.text.toString(),
                textPetBirthDate.text.toString(),
                textPetSize.text.toString(),
                textPetObservations.text.toString(),
                textPetBehaviour.text.toString(),
                selectedImage
            )
        }
    }

    /**
     *  Valida os campos
     */
    private fun validateInputs(): Boolean {

        val name = textPetName.validate(this)
        val breed = textPetBreed.validate(this)
        val gender = textPetGender.validate(this)
        val behaviour = textPetBehaviour.validate(this)
        val birthDate = textPetBirthDate.validate(this, TextTypeEnum.DATE)
        val petSize = textPetSize.validate(this)

        return name && breed && gender && behaviour && birthDate && petSize
    }

    /**
     *  Abre o menu de opcoes da galeria
     */
    private fun openGallery() {

        if (!checkWriteExternalPermission()) return

        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
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
                    selectedImage = ImageRotationHelper.getCorrectImageRotation(intent?.data?.imageUrl(this))

                    petImage.setPadding(0, 0, 0, 0)
                    Glide.with(this)
                        .load(selectedImage)
                        .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
                        .circleCrop()
                        .into(petImage)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        textPetBirthDate.removeTextChangedListener(inputDateMask)
    }

    override fun onResume() {
        super.onResume()
        textPetBirthDate.removeTextChangedListener(inputDateMask)
        textPetBirthDate.addTextChangedListener(inputDateMask)
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
