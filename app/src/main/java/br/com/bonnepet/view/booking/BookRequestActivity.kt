package br.com.bonnepet.view.booking

import Data
import Time
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.BookingStatusEnum
import br.com.bonnepet.data.model.HostBookingDTO
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.util.extension.formatToCurrency
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.util.extension.setSafeOnClickListener
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.pet.PetDetailsActivity
import br.com.bonnepet.view.pet.adapter.PetAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.activity_book_request.*

class BookRequestActivity : BaseActivity(), PetAdapter.ItemClickListener {
    override val layoutResource = R.layout.activity_book_request
    private lateinit var viewModel: BookRequestViewModel

    override val activityTitle: Nothing? = null

    private val userImage by lazy { image_view_user }

    private val userName by lazy { user_name }

    private val status by lazy { statusColor }

    private val totalPrice by lazy { text_price_value }

    private val checkInText by lazy { text_check_in_date }

    private val checkOutText by lazy { text_check_out_date }

    private val stayDays by lazy { text_stay_days }

    private val phoneText by lazy { text_telephone_number }

    private lateinit var petAdapter: PetAdapter

    private val recyclerView by lazy { recycler_view }

    private lateinit var hostBookingDTO: HostBookingDTO

    private val progressBar by lazy { progress_bar }

    private val cardBottom by lazy { card_view_bottom }

    private var isDataUpdated = false

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(BookRequestViewModel::class.java)

        hostBookingDTO = intent.getSerializableExtra(Data.HOST_BOOKING_DTO) as HostBookingDTO
        setFields()
        btn_refuse.setSafeOnClickListener { viewModel.refuseBooking(hostBookingDTO.bookingDetailsDTO.id) }
        btn_accept.setSafeOnClickListener { viewModel.acceptBooking(hostBookingDTO.bookingDetailsDTO.id) }

        viewModel.isLoading().observe(this, Observer {
            progressBar.isVisible = it
        })

        viewModel.errorMessage().observe(this, Observer {
            showToast(it)
        })

        viewModel.hostBooking.observe(this, Observer {
            isDataUpdated = true
            hostBookingDTO = it
            setFields()
        })
    }


    private fun setFields() {
        setUserImage(hostBookingDTO.profileDTO.profileImageURL)
        userName.text = hostBookingDTO.profileDTO.userName
        setStatus()
        totalPrice.text = hostBookingDTO.bookingDetailsDTO.totalPrice.formatToCurrency()
        checkInText.text = hostBookingDTO.bookingDetailsDTO.stayInitialDate
        checkOutText.text = hostBookingDTO.bookingDetailsDTO.stayFinalDate
        setStayDaysText(hostBookingDTO.bookingDetailsDTO.stayDays)
        phoneText.text = hostBookingDTO.profileDTO.telephone
        petAdapter = PetAdapter(this, hostBookingDTO.bookingDetailsDTO.petDTO.toMutableList(), this)
        recyclerView.adapter = petAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setStatus() {
        val bookingStatusEnum = BookingStatusEnum.getStatusEnum(hostBookingDTO.bookingDetailsDTO.status)
        status.setChipBackgroundColorResource(bookingStatusEnum!!.color)
        status.setText(bookingStatusEnum.description)

        cardBottom.isVisible = bookingStatusEnum == BookingStatusEnum.OPEN
    }

    private fun setStayDaysText(days: String) {
        stayDays.text =
            if (days.toInt() == 1) "$days ${getString(R.string.night)}"
            else "$days ${getString(R.string.nights)}"
    }

    private fun setUserImage(image: String?) {
        Glide.with(this)
            .load(image)
            .error(R.drawable.ic_account_circle)
            .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .circleCrop()
            .into(userImage)
    }

    /** Acao do botao de voltar da actionBar */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (isDataUpdated) setResult(Activity.RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (isDataUpdated) setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }

    override fun onItemClick(pet: PetDTO) {
        val intent = Intent(this, PetDetailsActivity::class.java).apply {
            putExtra(Data.PET_DTO, pet)
        }
        startActivity(intent)
    }
}

