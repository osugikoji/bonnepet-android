package br.com.bonnepet.view.booking


import Data
import RequestCode
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.booking.adapter.RequestBookingAdapter
import br.com.bonnepet.view.host.HostDetailsActivity
import kotlinx.android.synthetic.main.fragment_request_booking.*


class RequestBookingFragment : BaseFragment(), RequestBookingAdapter.ItemClickListener {
    override val layoutResource = R.layout.fragment_request_booking
    override val fragmentTitle: Nothing? = null
    private lateinit var viewModel: RequestBookingViewModel

    private val recyclerView by lazy { recycler_view }

    lateinit var requestBookingAdapter: RequestBookingAdapter

    private val swipeRefresh by lazy { swipe_refresh }

    private val progressBar by lazy { progress_bar }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideActionBarDisplayHome()
        viewModel = ViewModelProviders.of(this).get(RequestBookingViewModel::class.java)

        requestBookingAdapter = RequestBookingAdapter(activity!!, ArrayList(), this)
        recyclerView.adapter = requestBookingAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        loadData()

        swipeRefresh.setColorSchemeColors((ContextCompat.getColor(activity!!, R.color.color_primary)))
        swipeRefresh.setOnRefreshListener {
            loadData()
        }

        viewModel.isLoading().observe(this, Observer {
            progressBar.isVisible = it && !swipeRefresh.isRefreshing
            recyclerView.isVisible = !progressBar.isVisible
        })
    }

    fun loadData(resetData: Boolean = true) {
        viewModel.getRequestBookings()
        viewModel.bookingDetailsList.observe(this, Observer { requestBookingList ->
            layout_empty_bookings.isVisible = requestBookingList.isEmpty()
            recyclerView.isVisible = requestBookingList.isNotEmpty()
            requestBookingAdapter.update(requestBookingList, resetData)
            swipeRefresh.isRefreshing = false
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.REFRESH_REQUEST_BOOKING -> loadData()
            }
        }
    }

    override fun onItemClick(hostList: HostDTO) {
        val intent = Intent(activity, HostDetailsActivity::class.java).apply {
            putExtra(Data.HOST_DTO, hostList)
        }
        startActivityForResult(intent, RequestCode.REFRESH_REQUEST_BOOKING)
    }
}
