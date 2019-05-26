package br.com.bonnepet.view.menu.editUser

import Data
import Mask
import android.app.Activity
import android.os.Bundle
import android.text.TextWatcher
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.data.model.AddressDTO
import br.com.bonnepet.data.model.EditProfileDTO
import br.com.bonnepet.data.model.ProfileDTO
import br.com.bonnepet.view.component.MaskEditText
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_edit_profile
    override val activityTitle = R.string.edit_profile
    private lateinit var viewModel: EditProfileViewModel

    private val inputName by lazy { input_name }
    private val inputBirthDate by lazy { input_birth_date }
    private val inputCellphone by lazy { input_cell_phone }
    private val inputTelephone by lazy { input_telephone }

    private val inputCep by lazy { input_cep }
    private val inputDistrict by lazy { input_district }
    private val inputStreet by lazy { input_street }
    private val inputNumber by lazy { input_number }
    private val inputState by lazy { input_state }
    private val inputCity by lazy { input_city }

    private val btnSave by lazy { btn_save }

    private val progressBar by lazy { progress_bar }

    private lateinit var inputDateMask: TextWatcher
    private lateinit var inputPhoneMask: TextWatcher
    private lateinit var inputTelephoneMask: TextWatcher

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)
        inputDateMask = MaskEditText(inputBirthDate, Mask.DATE_MASK)
        inputPhoneMask = MaskEditText(inputCellphone, Mask.CELLPHONE_MASK)
        inputTelephoneMask = MaskEditText(inputTelephone, Mask.TELEPHONE_MASK)

        inputBirthDate.addTextChangedListener(inputDateMask)
        inputCellphone.addTextChangedListener(inputPhoneMask)
        inputTelephone.addTextChangedListener(inputTelephoneMask)

        btnSave.setOnClickListener { updateProfile() }

        val profileDTO = intent.getSerializableExtra(Data.PROFILE_DTO) as ProfileDTO
        setFields(profileDTO)
    }

    private fun updateProfile() {
        val addressDTO = AddressDTO(
            inputCep.text.toString(),
            inputStreet.text.toString(),
            inputNumber.text.toString(),
            inputDistrict.text.toString(),
            inputCity.text.toString(),
            inputState.text.toString()
        )
        val editProfileDTO = EditProfileDTO(
            inputName.text.toString(),
            inputBirthDate.text.toString(),
            inputCellphone.text.toString(),
            inputTelephone.text.toString(),
            addressDTO
        )
        progressBar.isVisible = true
        viewModel.updateUserProfile(editProfileDTO)
        viewModel.errorMessage().observe(this, Observer { message ->
            progressBar.isVisible = false
            showToast(message)
        })
        viewModel.onUpdateUserProfile.observe(this, Observer {
            showToast(getString(R.string.edit_profile_successfully))
            setResult(Activity.RESULT_OK)
            progressBar.isVisible = false
            finish()
        })
    }

    private fun setFields(profileDTO: ProfileDTO) {
        profileDTO.run {
            inputName.setText(userName)
            inputBirthDate.setText(birthDate)
            inputCellphone.setText(cellphone)
            inputTelephone.setText(telephone)
            inputCep.setText(addressDTO.cep)
            inputDistrict.setText(addressDTO.district)
            inputStreet.setText(addressDTO.street)
            inputNumber.setText(addressDTO.number)
            inputState.setText(addressDTO.state)
            inputCity.setText(addressDTO.city)
        }
    }

    /** Acao do botao de voltar da actionBar */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
