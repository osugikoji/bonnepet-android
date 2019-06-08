package br.com.bonnepet.view.menu.beHost

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.PetSizeEnum
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.component.CheckBoxDialog
import kotlinx.android.synthetic.main.activity_edit_host.*
import java.math.BigDecimal

class EditHostActivity : BaseActivity() {

    override val layoutResource = R.layout.activity_edit_host
    private lateinit var viewModel: EditHostViewModel

    override val activityTitle = R.string.host_edit_title

    private val textPriceValue by lazy { text_price_value }

    private val textPreferenceSize by lazy { edit_text_size_preference }

    private val aboutYou by lazy { edit_text_about_you }

    private val seekBar by lazy { seek_bar }

    private val btnEdit by lazy { btn_edit }

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(EditHostViewModel::class.java)
        viewModel.initViewModel(intent)

        textPriceValue.text = viewModel.getPrice()
        seekBar.max = 200
        seekBar?.progress = BigDecimal(viewModel.getPrice()).toInt()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textPriceValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        textPreferenceSize.setText(getPreferenceSize())

        val items = (resources.getStringArray(R.array.size_list))
        textPreferenceSize.setOnClickListener { CheckBoxDialog(this, items, it as EditText) }

        aboutYou.setText(viewModel.getAboutHost())

        btnEdit.setOnClickListener { editHost() }

        viewModel.onEditHost.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        viewModel.errorMessage().observe(this, Observer { errorMessage ->
            showToast(errorMessage)
        })

        viewModel.isLoading().observe(this, Observer { isLoading ->
            progressDialogVisibility(isLoading)
        })
    }

    private fun getPreferenceSize(): String {
        var sizeList = ""
        viewModel.getPetPreferenceSize().forEach { size ->
            val sizeDescription = getString(PetSizeEnum.getDescription(size)!!)
            sizeList += "$sizeDescription, "
        }
        return sizeList.removeSuffix(", ")
    }

    private fun editHost() {
        viewModel.editHost(
            textPriceValue.text.toString(),
            textPreferenceSize.text.toString(),
            aboutYou.text.toString()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
