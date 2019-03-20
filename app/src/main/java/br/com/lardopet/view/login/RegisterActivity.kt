package br.com.lardopet.view.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.lardopet.R
import br.com.lardopet.data.model.AddressDTO
import br.com.lardopet.util.onTextChanged
import br.com.lardopet.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_register

    private val inputCep by lazy { input_cep }

    private val registerViewModel by lazy { ViewModelProviders.of(this).get(RegisterViewModel::class.java) }

    private val cepLink by lazy { forgot_cep_link }

    private val inputDistrict by lazy { input_district }
    private val inputStreet by lazy { input_street }
    private val inputState by lazy { input_state }
    private val inputCity by lazy { input_city }

    private val inputLayoutDistrict by lazy { input_layout_district }
    private val inputLayoutStreet by lazy { input_layout_street }
    private val inputLayoutState by lazy { input_layout_state }
    private val inputLayoutCity by lazy { input_layout_city }

    override fun onPrepareActivity(state: Bundle?) {
        cepLink.setOnClickListener { redirectToBuscaCep() }
        inputCep.onTextChanged { registerViewModel.getAddress(it) }

        registerViewModel.onAddressRequest().observe(this, Observer {
            if (it) disableAddressEditText() else enableAddressEditText()
        })

        registerViewModel.address().observe(this, Observer { address ->
            setEditTextAddress(address)
        })
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
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     *  Redireciona para o site do correios
     */
    private fun redirectToBuscaCep() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Link.CORREIOS_CEP)))
    }
}