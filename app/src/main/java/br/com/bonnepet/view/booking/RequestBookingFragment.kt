package br.com.bonnepet.view.booking


import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.model.BookingDetailsDTO
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.booking.adapter.RequestBookingAdapter
import kotlinx.android.synthetic.main.fragment_request_booking.*


class RequestBookingFragment : BaseFragment(), RequestBookingAdapter.ItemClickListener {
    override val layoutResource = R.layout.fragment_request_booking
    override val fragmentTitle: Nothing? = null
    private lateinit var viewModel: RequestBookingViewModel

    private val recyclerView by lazy { recycler_view }

    lateinit var requestBookingAdapter: RequestBookingAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideActionBarDisplayHome()
        viewModel = ViewModelProviders.of(this).get(RequestBookingViewModel::class.java)

        requestBookingAdapter = RequestBookingAdapter(activity!!, ArrayList(), this)
        recyclerView.adapter = requestBookingAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        loadData(true)
    }

    private fun loadData(resetData: Boolean) {
        viewModel.getRequestBookings()
        viewModel.bookingDetailsList.observe(this, Observer { requestBookingList ->
            requestBookingAdapter.update(requestBookingList, resetData)
        })
    }

    override fun onItemClick(bookingDetails: BookingDetailsDTO) {
    }
}
