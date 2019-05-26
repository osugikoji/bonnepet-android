package br.com.bonnepet.view.host

import Data
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.host.adapter.SearchHostAdapter
import kotlinx.android.synthetic.main.search_fragment.*

class SearchHostFragment : BaseFragment(), SearchHostAdapter.ItemClickListener {
    override val layoutResource = R.layout.search_fragment
    override val fragmentTitle = R.string.search_accommodation
    private lateinit var viewModel: SearchHostViewModel

    private val recyclerView by lazy { recycler_view }

    lateinit var hostAdapter: SearchHostAdapter

    private val progressBar by lazy { progress_bar }

    private val swipeRefresh by lazy { swipe_refresh }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideActionBarDisplayHome()
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this).get(SearchHostViewModel::class.java)

        swipeRefresh.setColorSchemeColors((ContextCompat.getColor(activity!!, R.color.color_primary)))
        swipeRefresh.setOnRefreshListener {
            loadData(true)
        }

        hostAdapter = SearchHostAdapter(activity!!, ArrayList(), this)
        recyclerView.adapter = hostAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        loadData(true)

        viewModel.isLoading().observe(this, Observer { isLoading ->
            progressBar.isVisible = isLoading
        })
    }

    private fun loadData(resetData: Boolean) {
        viewModel.getAllHost()
        viewModel.hostList.observe(this, Observer { hostList ->
            hostAdapter.update(hostList, resetData)
            swipeRefresh.isRefreshing = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_fragment, menu)
    }

    override fun onItemClick(host: HostDTO) {
        val intent = Intent(activity, HostDetailsActivity::class.java).apply {
            putExtra(Data.HOST_DTO, host)
        }
        startActivityForResult(intent, RequestCode.REFRESH_SEARCH_HOST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.REFRESH_SEARCH_HOST -> loadData(true)
            }
        }
    }
}
