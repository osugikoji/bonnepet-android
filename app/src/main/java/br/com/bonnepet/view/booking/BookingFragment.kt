package br.com.bonnepet.view.booking


import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import br.com.bonnepet.R
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.booking.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_booking.*


class BookingFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_booking

    override val fragmentTitle = R.string.title_reservations

    private val tabLayout by lazy { tabs }

    private val viewPager by lazy { viewpager }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideActionBarDisplayHome()
        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(HostBookingFragment(), getString(R.string.your_home))
        adapter.addFragment(RequestBookingFragment(), getString(R.string.your_request))
        viewPager.adapter = adapter
    }
}
