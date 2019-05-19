package br.com.bonnepet.view.host

import Data
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.BookingStatusEnum
import br.com.bonnepet.data.model.BookingDetailsDTO
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.pet.PetDetailsActivity
import br.com.bonnepet.view.pet.adapter.PetAdapter
import kotlinx.android.synthetic.main.activity_book_details.*

class BookDetailsActivity : BaseActivity(), PetAdapter.ItemClickListener {
    override val layoutResource = R.layout.activity_book_details
    override val activityTitle = R.string.book_details_title

    private val recyclerView by lazy { recycler_view }

    lateinit var bookingDetailsDTO: BookingDetailsDTO

    private val textDateTake by lazy { text_date_take }

    private val textDateGet by lazy { text_date_get }

    private val textTotalMoney by lazy { text_total_money }

    private val textStatus by lazy { text_status }

    private lateinit var petAdapter: PetAdapter

    override fun onPrepareActivity(state: Bundle?) {
        bookingDetailsDTO = intent.getSerializableExtra(Data.BOOK_DETAILS_DTO) as BookingDetailsDTO

        textDateTake.text = bookingDetailsDTO.stayInitialDate
        textDateGet.text = bookingDetailsDTO.stayFinalDate
        textTotalMoney.text = bookingDetailsDTO.totalPrice
        textStatus.setText(BookingStatusEnum.getStatusDescription(bookingDetailsDTO.status))

        petAdapter = PetAdapter(this, bookingDetailsDTO.petDTO.toMutableList(), this)
        recyclerView.adapter = petAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
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
