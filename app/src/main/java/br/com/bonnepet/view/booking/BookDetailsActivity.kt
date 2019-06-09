package br.com.bonnepet.view.booking

import Data
import Prefs
import SharedPreferencesUtil
import Time
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.BookingStatusEnum
import br.com.bonnepet.data.model.BookingDetailsDTO
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.data.model.RateDTO
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.util.extension.setSafeOnClickListener
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.pet.PetDetailsActivity
import br.com.bonnepet.view.pet.adapter.PetAlternativeAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.activity_book_details.*
import kotlinx.android.synthetic.main.host_rate_dialog.view.*

class BookDetailsActivity : BaseActivity(), PetAlternativeAdapter.ItemClickListener {
    override val layoutResource = R.layout.activity_book_details
    override val activityTitle = R.string.book_details_title
    private lateinit var viewModel: BookDetailsViewModel

    private val recyclerView by lazy { recycler_view }

    private lateinit var bookingDetailsDTO: BookingDetailsDTO

    private lateinit var hostDTO: HostDTO

    private val textDateTake by lazy { text_date_take }

    private val textDateGet by lazy { text_date_get }

    private val textTotalMoney by lazy { text_total_money }

    private val chipStatus by lazy { statusColor }

    private lateinit var petAdapter: PetAlternativeAdapter

    private val rateDialog by lazy { buildRateHostDialog() }

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(BookDetailsViewModel::class.java)
        bookingDetailsDTO = intent.getSerializableExtra(Data.BOOK_DETAILS_DTO) as BookingDetailsDTO
        hostDTO = intent.getSerializableExtra(Data.HOST_DTO) as HostDTO

        textDateTake.text = bookingDetailsDTO.stayInitialDate
        textDateGet.text = bookingDetailsDTO.stayFinalDate
        textTotalMoney.text = bookingDetailsDTO.totalPrice
        setBookStatus()

        petAdapter = PetAlternativeAdapter(this, bookingDetailsDTO.petDTO.toMutableList(), this)
        recyclerView.adapter = petAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btn_book_cancel.setSafeOnClickListener { viewModel.cancelBooking(bookingDetailsDTO.id) }

        btn_book_finish.setSafeOnClickListener {
            viewModel.finalizeBooking(bookingDetailsDTO.id)
        }

        viewModel.onCancel.observe(this, Observer {
            if (it) {
                setResult(Activity.RESULT_OK)
                SharedPreferencesUtil.putBoolean(Prefs.FETCH_SEARCH_FRAGMENT, true)
                finish()
            }
        })

        viewModel.onFinalize.observe(this, Observer {
            if (it) {
                setResult(Activity.RESULT_OK)
                SharedPreferencesUtil.putBoolean(Prefs.FETCH_SEARCH_FRAGMENT, true)
                rateDialog.show()
            }
        })

        viewModel.errorMessage().observe(this, Observer {
            showToast(it)
        })

        viewModel.isLoading().observe(this, Observer {
            progressDialogVisibility(it)
        })

        card_book_cancel.isVisible = bookingDetailsDTO.status == BookingStatusEnum.OPEN.name
        card_book_finish.isVisible =
            bookingDetailsDTO.status == BookingStatusEnum.CONFIRMED.name
    }

    private fun buildRateHostDialog(): AlertDialog {
        val view = View.inflate(this, R.layout.host_rate_dialog, null)
        val hostImage = view.image_host_image
        val hostName = view.text_host_name
        val hostRateBar = view.rating_bar
        val rateName = view.text_view_rate
        val feedbackEditText = view.edit_text_feedback
        val layoutRateInfo = view.layout_rate_info

        view.btn_rate.setOnClickListener {
            viewModel.rateHost(RateDTO(bookingDetailsDTO.id, hostRateBar.rating.toString(), feedbackEditText.text.toString()))
            finish()
        }

        Glide.with(this)
            .load(hostDTO.profileDTO.profileImageURL)
            .error(R.drawable.ic_account_circle)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .circleCrop()
            .into(hostImage)

        hostName.text = hostDTO.profileDTO.userName

        hostRateBar.setOnRatingBarChangeListener { ratingBar, rating, _ ->
            if (rating >= 1.0f) layoutRateInfo.isVisible = true
            if (rating < 1.0f) ratingBar.rating = 1.0f
            when (rating) {
                1.0f -> rateName.setText(R.string.very_bad)
                2.0f -> rateName.setText(R.string.bad)
                3.0f -> rateName.setText(R.string.regular)
                4.0f -> rateName.setText(R.string.good)
                5.0f -> rateName.setText(R.string.excellent)
            }
        }

        val dialog = AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar).apply {
            setView(view)
            setCancelable(false)
        }.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun setBookStatus() {
        val bookingStatusEnum = BookingStatusEnum.getStatusEnum(bookingDetailsDTO.status)
        chipStatus.setChipBackgroundColorResource(bookingStatusEnum!!.color)
        chipStatus.setText(bookingStatusEnum.description)
    }

    /**
     *  Ação do botão de voltar da actionBar
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(pet: PetDTO) {
        val intent = Intent(this, PetDetailsActivity::class.java).apply {
            putExtra(Data.PET_DTO, pet)
            putExtra(Data.CAN_EDIT_PET, false)
        }
        startActivity(intent)
    }
}
