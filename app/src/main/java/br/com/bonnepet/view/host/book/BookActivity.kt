package br.com.bonnepet.view.host.book

import Time
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.host.adapter.PetBookAdapter
import br.com.bonnepet.view.pet.PetRegisterActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.host_item.*
import java.util.*

class BookActivity : BaseActivity(), PetBookAdapter.ItemClickListener {
    override val layoutResource = R.layout.activity_book
    override val activityTitle = R.string.book
    private lateinit var viewModel: BookViewModel

    private val hostImage by lazy { user_image }
    private val hostNameText by lazy { text_user_name }
    private val cityText by lazy { text_city }
    private val districtText by lazy { text_district }
    private val priceText by lazy { text_money_value }

    private val layoutDateTake by lazy { layout_date_take }
    private val dateTakeText by lazy { text_date_take }

    private val layoutDateGet by lazy { layout_date_get }
    private val dateGetText by lazy { text_date_get }

    private val nightText by lazy { text_night }
    private val totalPriceText by lazy { text_total_money }

    private val recyclerView by lazy { recycler_view }

    private val btnAddPet by lazy { btn_add_pet }

    private val btnBook by lazy { btn_book }

    private lateinit var petBookAdapter: PetBookAdapter

    private val progressBar by lazy { progress_bar }

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(BookViewModel::class.java)
        viewModel.initViewModel(intent)
        val initialNightText = "0 ${getString(R.string.night)}"
        nightText.text = initialNightText
        totalPriceText.text = "0"
        setHostProfile()
        layout_host.isClickable = false

        layoutDateTake.setOnClickListener { buildDatePicker(dateTakeText) }
        layoutDateGet.setOnClickListener { buildDatePicker(dateGetText) }

        petBookAdapter = PetBookAdapter(this, ArrayList(), this)
        recyclerView.adapter = petBookAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAddPet.setOnClickListener { addPet() }
        btnBook.setOnClickListener { book() }

        viewModel.textNight.observe(this, Observer {
            nightText.text = it
        })
        viewModel.textTotalPrice.observe(this, Observer {
            totalPriceText.text = it
        })

        viewModel.errorMessage().observe(this, Observer {
            showToast(it)
        })

        viewModel.isLoading().observe(this, Observer {
            progressBar.isVisible = it
        })

        viewModel.bookingSuccess.observe(this, Observer {
            if (it) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        loadPetData(true)
    }

    private fun addPet() {
        startActivityForResult(Intent(this, PetRegisterActivity::class.java), RequestCode.PET_REGISTER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.PET_REGISTER -> loadPetData(true)
            }
        }
    }

    private fun book() {
        viewModel.book(dateTakeText.text.toString(), dateGetText.text.toString(), totalPriceText.text.toString())
    }

    private fun loadPetData(resetData: Boolean) {
        viewModel.getAllPets()
        viewModel.petList.observe(this, Observer { petList ->
            petBookAdapter.update(petList, resetData)
        })
    }

    private fun buildDatePicker(textView: TextView) {
        val calendar = Calendar.getInstance()

        val datePickerDialog =
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val date = "$dayOfMonth/${monthOfYear + 1}/$year"
                textView.text = date
                viewModel.calculateTotalPriceAndNight(dateTakeText.text.toString(), dateGetText.text.toString())
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun setHostProfile() {
        Glide.with(this)
            .load(viewModel.getHostPicture())
            .error(R.drawable.ic_account_circle)
            .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .circleCrop()
            .into(hostImage)

        hostNameText.text = viewModel.getHostName()
        cityText.text = viewModel.getAddress().city
        districtText.text = viewModel.getAddress().district
        priceText.text = viewModel.getPrice()

        view_divider.isVisible = false
    }

    override fun onSwitchClick(pet: PetDTO, isChecked: Boolean) {
        viewModel.petSelected(pet, isChecked)
    }

    /**
     *  Ação do botão de voltar da actionBar
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
