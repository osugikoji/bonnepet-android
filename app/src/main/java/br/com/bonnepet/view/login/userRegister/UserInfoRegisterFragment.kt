package br.com.bonnepet.view.login.userRegister


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import br.com.bonnepet.R
import br.com.bonnepet.data.model.UserDTO
import br.com.bonnepet.util.extension.*
import br.com.bonnepet.util.component.MaskEditText
import br.com.bonnepet.view.base.BaseFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_user_info_register.*


class UserInfoRegisterFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_user_info_register
    override val fragmentTitle: Nothing? = null

    private val registerActivity by lazy { activity as RegisterActivity }

    private val pictureLayout by lazy { picture_layout }
    private val profileImage by lazy { profile_image }
    private val inputEmail by lazy { input_email }
    private val inputPassword by lazy { input_password }
    private val inputName by lazy { input_name }
    private val inputBirthDate by lazy { input_birth_date }
    private val inputCellphone by lazy { input_cell_phone }
    private val inputTelephone by lazy { input_telephone }

    private val btnNext by lazy { btn_next }

    private lateinit var inputDateMask: TextWatcher
    private lateinit var inputPhoneMask: TextWatcher

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inputDateMask = MaskEditText(inputBirthDate, Mask.DATE_MASK)
        inputPhoneMask = MaskEditText(inputCellphone, Mask.PHONE_MASK)

        inputBirthDate.addTextChangedListener(inputDateMask)
        inputCellphone.addTextChangedListener(inputPhoneMask)
        pictureLayout.setSafeOnClickListener { openGallery() }

        btnNext.setOnClickListener { replaceFragment() }
    }

    /**
     *  Abre o menu de opcoes da galeria
     */
    private fun openGallery() {

        if (!activity!!.checkWriteExternalPermission()) return

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
                    registerActivity.selectedUriImage = data?.data?.imageUrl(context)

                    Glide.with(this)
                        .load(registerActivity.selectedUriImage)
                        .into(profileImage)

                }
            }
        }
    }

    private fun replaceFragment() {
        if (validateInputs()) {

            val userDTO = UserDTO(
                null,
                inputEmail.text.toString(),
                inputName.text.toString(),
                inputPassword.text.toString(),
                inputBirthDate.text.toString(),
                inputCellphone.text.toString(),
                inputTelephone.text.toString()
            )
            registerActivity.userDTO = userDTO
            replaceFragment(R.id.fragment_content, AddressRegisterFragment())
        }
    }

    /**
     *  Valida os campos
     */
    private fun validateInputs(): Boolean {

        val email = inputEmail.validate()
        val name = inputName.validate()
        val password = inputPassword.validate()
        val birthDate = inputBirthDate.validate()
        val cellphone = inputCellphone.validate()

        return email && name && password && birthDate && cellphone
    }

    override fun onPause() {
        super.onPause()
        inputBirthDate.removeTextChangedListener(inputDateMask)
        inputCellphone.removeTextChangedListener(inputPhoneMask)
    }
}
