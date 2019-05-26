package br.com.bonnepet.view.booking

import Data
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.BookingStatusEnum
import br.com.bonnepet.data.model.BookingDetailsDTO
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.util.extension.setSafeOnClickListener
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.pet.PetDetailsActivity
import br.com.bonnepet.view.pet.adapter.PetAdapter
import kotlinx.android.synthetic.main.activity_book_details.*

class BookDetailsActivity : BaseActivity(), PetAdapter.ItemClickListener {
    override val layoutResource = R.layout.activity_book_details
    override val activityTitle = R.string.book_details_title
    private lateinit var viewModel: BookDetailsViewModel

    private val recyclerView by lazy { recycler_view }

    lateinit var bookingDetailsDTO: BookingDetailsDTO

    private val textDateTake by lazy { text_date_take }

    private val textDateGet by lazy { text_date_get }

    private val textTotalMoney by lazy { text_total_money }

    private val chipStatus by lazy { statusColor }

    private lateinit var petAdapter: PetAdapter

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(BookDetailsViewModel::class.java)
        bookingDetailsDTO = intent.getSerializableExtra(Data.BOOK_DETAILS_DTO) as BookingDetailsDTO

        textDateTake.text = bookingDetailsDTO.stayInitialDate
        textDateGet.text = bookingDetailsDTO.stayFinalDate
        textTotalMoney.text = bookingDetailsDTO.totalPrice
        setBookStatus()

        petAdapter = PetAdapter(this, bookingDetailsDTO.petDTO.toMutableList(), this)
        recyclerView.adapter = petAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btn_book_cancel.setSafeOnClickListener { viewModel.cancelBooking(bookingDetailsDTO.id) }

        viewModel.onCancelBooking.observe(this, Observer {
            if (it) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        viewModel.errorMessage().observe(this, Observer {
            showToast(it)
        })

        card_book_cancel.isVisible = bookingDetailsDTO.status == BookingStatusEnum.OPEN.name
        card_book_finish.isVisible = bookingDetailsDTO.status == BookingStatusEnum.CONFIRMED.name
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
        }
        startActivity(intent)
    }
}
