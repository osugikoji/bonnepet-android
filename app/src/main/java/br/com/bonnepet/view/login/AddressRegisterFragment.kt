package br.com.bonnepet.view.login

import Link
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.data.model.AddressDTO
import br.com.bonnepet.data.model.CepDTO
import br.com.bonnepet.util.extension.afterTextChanged
import br.com.bonnepet.util.extension.popBackStack
import br.com.bonnepet.util.extension.validate
import br.com.bonnepet.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_address_register.*

class AddressRegisterFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_address_register
    override val fragmentTitle: Nothing? = null

    private val registerViewModel by lazy { ViewModelProviders.of(this).get(RegisterViewModel::class.java) }

    private val registerActivity by lazy { activity as RegisterActivity }

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

    private val btnRegister by lazy { btn_register }

    private val btnReturn by lazy { btn_return }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inputCep.afterTextChanged { cep ->
            registerViewModel.getAddress(cep)
        }
        cepLink.setOnClickListener { redirectToSearchCep() }
        btnRegister.setOnClickListener { requestRegister() }
        btnReturn.setOnClickListener { previousFragment() }

        registerViewModel.onAddressRequest.observe(this, Observer {
            if (it) disableAddressEditText() else enableAddressEditText()
        })

        registerViewModel.address.observe(this, Observer { address ->
            setEditTextAddress(address)
        })

        registerViewModel.userRegisterRequestResult.observe(this, Observer { result ->
            registerActivity.hideLoading()
            if (result) showToast(getString(R.string.register_successfully))
        })

        registerViewModel.errorMessage.observe(this, Observer { errorMessage ->
            showToast(errorMessage)
        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun requestRegister() {
        if (validateInputs()) {
            val addressDTO = AddressDTO(
                inputCep.text.toString(),
                inputStreet.text.toString(),
                inputNumber.text.toString(),
                inputDistrict.text.toString(),
                inputCity.text.toString(),
                inputState.text.toString()
            )
            registerActivity.userDTO.addressDTO = addressDTO
            registerActivity.showLoading()
            registerViewModel.doRegister(registerActivity.userDTO, registerActivity.selectedUriImage)
        }
    }

    /**
     *  Valida os campos
     */
    private fun validateInputs(): Boolean {

        val cep = inputCep.validate()
        val district = inputDistrict.validate()
        val street = inputStreet.validate()
        val number = inputNumber.validate()
        val state = inputState.validate()
        val city = inputCity.validate()

        return cep && district && street && number && state && city
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

        btnRegister.isEnabled = true
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

        btnRegister.isEnabled = false
    }

    private fun previousFragment() {
        popBackStack()
    }

    /**
     *  Redireciona para o site dos correios
     */
    private fun redirectToSearchCep() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Link.SEARCH_CEP)))
    }
}
