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
import br.com.bonnepet.data.model.HostBookingDTO
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.util.extension.setSafeOnClickListener
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.booking.adapter.HostBookingAdapter
import br.com.bonnepet.view.menu.beHost.BeHostActivity
import kotlinx.android.synthetic.main.fragment_host_booking.*


class HostBookingFragment : BaseFragment(), HostBookingAdapter.ItemClickListener {
    override val layoutResource = R.layout.fragment_host_booking
    override val fragmentTitle: Nothing? = null
    private lateinit var viewModel: HostBookingViewModel

    private val recyclerView by lazy { recycler_view }

    private val swipeRefresh by lazy { swipe_refresh }

    private lateinit var hostBookingAdapter: HostBookingAdapter

    private val progressBar by lazy { progress_bar }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideActionBarDisplayHome()
        viewModel = ViewModelProviders.of(this).get(HostBookingViewModel::class.java)

        btn_turn_host.setSafeOnClickListener { startBeHostActivity() }

        hostBookingAdapter = HostBookingAdapter(activity!!, ArrayList(), this)
        recyclerView.adapter = hostBookingAdapter
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

        viewModel.errorMessage().observe(this, Observer {
            if (it == "NOT_A_HOST") showNoHostLayout()
            else showToast(it)
        })
    }

    private fun startBeHostActivity() {
        startActivityForResult(Intent(context, BeHostActivity::class.java), RequestCode.REFRESH_DATA)
    }

    private fun showNoHostLayout() {
        layout_not_host.isVisible = true
        layout_empty_bookings.isVisible = false
        recyclerView.isVisible = false
        swipeRefresh.isRefreshing = false
    }

    private fun layoutEmptyVisibility(visibility: Boolean) {
        layout_empty_bookings.isVisible = visibility
        layout_not_host.isVisible = false
    }

    fun loadData(resetData: Boolean = true) {
        viewModel.getHostBookings()
        viewModel.hostBookingList.observe(this, Observer { hostBookingList ->
            layoutEmptyVisibility(hostBookingList.isEmpty())
            recyclerView.isVisible = hostBookingList.isNotEmpty()
            hostBookingAdapter.update(hostBookingList, resetData)
            swipeRefresh.isRefreshing = false
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.REFRESH_DATA -> loadData()
            }
        }
    }

    override fun onItemClick(hostBooking: HostBookingDTO) {
        val intent = Intent(activity, BookRequestActivity::class.java).apply {
            putExtra(Data.HOST_BOOKING_DTO, hostBooking)
        }
        startActivityForResult(intent, RequestCode.REFRESH_DATA)
    }
}
