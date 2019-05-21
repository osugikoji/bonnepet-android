package br.com.bonnepet.view.booking


import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.model.HostBookingDTO
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.booking.adapter.HostBookingAdapter
import kotlinx.android.synthetic.main.fragment_host_booking.*


class HostBookingFragment : BaseFragment(), HostBookingAdapter.ItemClickListener {
    override val layoutResource = R.layout.fragment_host_booking
    override val fragmentTitle: Nothing? = null
    private lateinit var viewModel: HostBookingViewModel

    private val recyclerView by lazy { recycler_view }

    private lateinit var hostBookingAdapter: HostBookingAdapter


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideActionBarDisplayHome()
        viewModel = ViewModelProviders.of(this).get(HostBookingViewModel::class.java)

        hostBookingAdapter = HostBookingAdapter(activity!!, ArrayList(), this)
        recyclerView.adapter = hostBookingAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        loadData(true)
    }

    private fun loadData(resetData: Boolean) {
        viewModel.getHostBookings()
        viewModel.hostBookingList.observe(this, Observer { hostBookingList ->
            hostBookingAdapter.update(hostBookingList, resetData)
        })
    }

    override fun onItemClick(hostBooking: HostBookingDTO) {
    }
}
