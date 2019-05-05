package br.com.bonnepet.view.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.search.adapter.SearchHostAdapter
import kotlinx.android.synthetic.main.fragment_pet.*

class SearchHostFragment : BaseFragment(), SearchHostAdapter.ItemClickListener {
    override val layoutResource = R.layout.search_fragment
    override val fragmentTitle = R.string.search_accommodation
    private lateinit var viewModel: SearchHostViewModel

    private val recyclerView by lazy { recycler_view }

    lateinit var hostAdapter: SearchHostAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideActionBarDisplayHome()
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this).get(SearchHostViewModel::class.java)

        hostAdapter = SearchHostAdapter(activity!!, ArrayList(), this)
        recyclerView.adapter = hostAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        loadData(true)
    }

    private fun loadData(resetData: Boolean) {
        viewModel.getAllHost()
        viewModel.hostList.observe(this, Observer { hostList ->
            hostAdapter.update(hostList, resetData)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_fragment, menu)
    }

    override fun onItemClick(host: HostDTO) {

    }
}
