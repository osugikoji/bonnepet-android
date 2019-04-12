package br.com.bonnepet.view.login

import Link
import RequestCode
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.data.model.AddressDTO
import br.com.bonnepet.data.model.UserDTO
import br.com.bonnepet.util.extension.afterTextChanged
import br.com.bonnepet.util.extension.imageUrl
import br.com.bonnepet.util.extension.setSafeOnClickListener
import br.com.bonnepet.util.extension.validate
import br.com.bonnepet.util.view.CustomSpinner
import br.com.bonnepet.util.view.DateInputMask
import br.com.bonnepet.view.base.BaseActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_register
    private val registerViewModel by lazy { ViewModelProviders.of(this).get(RegisterViewModel::class.java) }

    private val profileImage by lazy { profile_image }
    private val inputEmail by lazy { input_email }
    private val inputPassword by lazy { input_password }
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

    private val pictureLayout by lazy { picture_layout }
    private val inputLayoutDistrict by lazy { input_layout_district }
    private val inputLayoutStreet by lazy { input_layout_street }
    private val inputLayoutState by lazy { input_layout_state }
    private val inputLayoutCity by lazy { input_layout_city }

    private val cepLink by lazy { forgot_cep_link }
    private val btnRegister by lazy { btn_register }

    private var selectedUriImage: String? = null


    override fun onPrepareActivity(state: Bundle?) {
        val items = ArrayList(arrayOf("teste1", "teste2", "teste3", "teste4").toList())
        val customAdapter = CustomSpinner(this, items)
        inputState.setAdapter(customAdapter)
        inputState.adapter.getItem(3)

        pictureLayout.setSafeOnClickListener { openGallery() }

        DateInputMask(inputBirthDate).listen()

        cepLink.setSafeOnClickListener { redirectToSearchCep() }

        inputCep.afterTextChanged { cep ->
            registerViewModel.getAddress(cep)
        }

        btnRegister.setSafeOnClickListener {
            doRegister()
        }

        registerViewModel.onAddressRequest.observe(this, Observer {
            if (it) disableAddressEditText() else enableAddressEditText()
        })

        registerViewModel.address.observe(this, Observer { address ->
            setEditTextAddress(address)
        })

        registerViewModel.userRegisterRequestResult.observe(this, Observer { result ->
            hideLoading()
            if (result) showToast(getString(R.string.register_successfully))
        })

        registerViewModel.errorMessage.observe(this, Observer { errorMessage ->
            showToast(errorMessage)
        })
    }

    /**
     *  Registra o usuario
     */
    private fun doRegister() {
        if (validateInputs()) {
            val userDTO = UserDTO(
                "",
                inputEmail.text.toString(),
                inputName.text.toString(),
                inputPassword.text.toString(),
                inputBirthDate.text.toString(),
                inputCellphone.text.toString(),
                inputTelephone.text.toString(),
                inputCep.text.toString(),
                inputStreet.text.toString(),
                inputNumber.text.toString(),
                inputDistrict.text.toString(),
                inputCity.text.toString(),
                inputState.text.toString()
            )
            showLoading()
            registerViewModel.doRegister(userDTO, selectedUriImage)
        }
    }

    /**
     *  Valida os campos de cadastro
     */
    private fun validateInputs(): Boolean {

        val email = inputEmail.validate()
        val name = inputName.validate()
        val password = inputPassword.validate()
        val birthDate = inputBirthDate.validate()
        val cellphone = inputCellphone.validate()
        val cep = inputCep.validate()
        val district = inputDistrict.validate()
        val street = inputStreet.validate()
        val number = inputNumber.validate()
        val state = inputState.validate()
        val city = inputCity.validate()

        return email && name && password && birthDate
                && cellphone && cep && district &&
                street && number && state && city

    }

    /**
     *  Abre o menu de opcoes da galeria
     */
    private fun openGallery() {
        if (!checkWriteExternalPermission()) return

        val mimeTypes = arrayOf("image/jpeg", "image/png")
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        startActivityForResult(intent, RequestCode.GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RequestCode.GALLERY -> {
                    selectedUriImage = data?.data?.imageUrl(this)

                    Glide.with(this)
                        .load(selectedUriImage)
                        .into(profileImage)

                }
            }
        }
    }

    /**
     *  Desabilita todos os campos de endereco
     */
    private fun disableAddressEditText() {
        inputLayoutDistrict.setBoxBackgroundColorResource(R.color.light_gray)
        inputDistrict.isEnabled = false

        inputLayoutStreet.setBoxBackgroundColorResource(R.color.light_gray)
        inputStreet.isEnabled = false

        inputLayoutState.setBoxBackgroundColorResource(R.color.light_gray)
        inputState.isEnabled = false

        inputLayoutCity.setBoxBackgroundColorResource(R.color.light_gray)
        inputCity.isEnabled = false
    }

    /**
     *  Habilita todos os campos de endereco
     */
    private fun enableAddressEditText() {
        inputLayoutDistrict.setBoxBackgroundColorResource(R.color.transparent)
        inputDistrict.isEnabled = true

        inputLayoutStreet.setBoxBackgroundColorResource(R.color.transparent)
        inputStreet.isEnabled = true

        inputLayoutState.setBoxBackgroundColorResource(R.color.transparent)
        inputState.isEnabled = true

        inputLayoutCity.setBoxBackgroundColorResource(R.color.transparent)
        inputCity.isEnabled = true
    }

    /**
     *  Preenche os campos de endereco
     */
    private fun setEditTextAddress(address: AddressDTO) {
        inputDistrict.setText(address.district)
        inputStreet.setText(address.street)
        inputState.setText(address.state)
        inputCity.setText(address.city)
    }

    /**
     *  Acao do botao de voltar da actionBar
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     *  Redireciona para o site dos correios
     */
    private fun redirectToSearchCep() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Link.SEARCH_CEP)))
    }
}
