package br.com.bonnepet.view.menu.editUser

import Data
import Link
import Mask
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.TextTypeEnum
import br.com.bonnepet.data.model.AddressDTO
import br.com.bonnepet.data.model.CepDTO
import br.com.bonnepet.data.model.EditProfileDTO
import br.com.bonnepet.data.model.ProfileDTO
import br.com.bonnepet.util.extension.afterTextChanged
import br.com.bonnepet.util.extension.progressDialog
import br.com.bonnepet.util.extension.validate
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.component.MaskEditText
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

    private val inputLayoutDistrict by lazy { input_layout_district }
    private val inputLayoutStreet by lazy { input_layout_street }
    private val inputLayoutState by lazy { input_layout_state }
    private val inputLayoutCity by lazy { input_layout_city }

    private val cepLink by lazy { forgot_cep_link }

    private val btnSave by lazy { btn_save }

    private lateinit var inputDateMask: TextWatcher
    private lateinit var inputPhoneMask: TextWatcher
    private lateinit var inputTelephoneMask: TextWatcher

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)
        inputDateMask = MaskEditText(inputBirthDate, Mask.DATE_MASK)
        inputPhoneMask = MaskEditText(inputCellphone, Mask.CELLPHONE_MASK)
        inputTelephoneMask = MaskEditText(inputTelephone, Mask.TELEPHONE_MASK)

        val profileDTO = intent.getSerializableExtra(Data.PROFILE_DTO) as ProfileDTO
        setFields(profileDTO)

        buildInputState()

        inputBirthDate.addTextChangedListener(inputDateMask)
        inputCellphone.addTextChangedListener(inputPhoneMask)
        inputTelephone.addTextChangedListener(inputTelephoneMask)

        btnSave.setOnClickListener { updateProfile() }
        cepLink.setOnClickListener { redirectToSearchCep() }

        inputCep.afterTextChanged { cep ->
            viewModel.getAddress(cep)
        }

        viewModel.onAddressRequest.observe(this, Observer {
            addressTextVisibility(it)
        })

        viewModel.address.observe(this, Observer { address ->
            setEditTextAddress(address)
        })

        viewModel.errorMessage().observe(this, Observer { message ->
            showToast(message)
        })

        viewModel.isLoading().observe(this, Observer {
            progressDialogVisibility(it)
        })
    }

    private fun buildInputState() {
        val items = (resources.getStringArray(R.array.states)).toList()
        val stateAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items)
        inputState.setAdapter(stateAdapter)
    }

    /**
     *  Preenche os campos de endereco
     */
    private fun setEditTextAddress(address: CepDTO) {
        inputDistrict.setText(address.district)
        inputStreet.setText(address.street)
        inputState.setText(address.state)
        inputCity.setText(address.city)
    }

    private fun updateProfile() {
        if (!validateInputs()) return

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
        viewModel.updateUserProfile(editProfileDTO)
        viewModel.onUpdateUserProfile.observe(this, Observer {
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

    private fun validateInputs(): Boolean {

        val name = inputName.validate(this, TextTypeEnum.NAME)
        val birthDate = inputBirthDate.validate(this, TextTypeEnum.DATE)
        val cellphone = inputCellphone.validate(this, TextTypeEnum.PHONE)

        val cep = inputCep.validate(this)
        val district = inputDistrict.validate(this)
        val street = inputStreet.validate(this)
        val number = inputNumber.validate(this)
        val state = inputState.validate(this)
        buildInputState()
        val city = inputCity.validate(this)

        return name && birthDate && cellphone && cep && district && street && number && state && city
    }

    private fun addressTextVisibility(visibility: Boolean) {
        val backgroundColor = if (visibility) R.color.gray_300 else R.color.transparent
        inputLayoutDistrict.setBoxBackgroundColorResource(backgroundColor)
        inputDistrict.isEnabled = !visibility

        inputLayoutStreet.setBoxBackgroundColorResource(backgroundColor)
        inputStreet.isEnabled = !visibility

        inputLayoutState.setBoxBackgroundColorResource(backgroundColor)
        inputState.isEnabled = !visibility

        inputLayoutCity.setBoxBackgroundColorResource(backgroundColor)
        inputCity.isEnabled = !visibility

        btnSave.isEnabled = !visibility
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

    /**
     *  Redireciona para o site dos correios
     */
    private fun redirectToSearchCep() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Link.SEARCH_CEP)))
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
