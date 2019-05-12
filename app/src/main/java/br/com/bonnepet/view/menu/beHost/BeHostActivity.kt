package br.com.bonnepet.view.menu.beHost

import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.component.CheckBoxDialog
import kotlinx.android.synthetic.main.activity_be_host.*

class BeHostActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_be_host
    private lateinit var viewModel: BeHostViewModel

    override val activityTitle = R.string.host_register_title

    private val textPriceValue by lazy { text_price_value }

    private val textPreferenceSize by lazy { edit_text_size_preference }

    private val aboutYou by lazy { edit_text_about_you }

    private val seekBar by lazy { seek_bar }

    private val btnRegister by lazy { btn_register }

    private val progressBar by lazy { progress_bar }

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(BeHostViewModel::class.java)

        textPriceValue.text = "0"
        seekBar.max = 200
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textPriceValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val items = (resources.getStringArray(R.array.size_list))
        textPreferenceSize.setOnClickListener { CheckBoxDialog(this, items, it as EditText) }

        btnRegister.setOnClickListener { doRegister() }

        viewModel.onHostRegister.observe(this, Observer { isSuccess ->
            if (isSuccess) finish()
        })

        viewModel.errorMessage().observe(this, Observer { errorMessage ->
            showToast(errorMessage)
        })

        viewModel.isLoading().observe(this, Observer { isLoading ->
            progressBar.isVisible = isLoading
        })
    }

    private fun doRegister() {
        viewModel.registerHost(
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
