package br.com.bonnepet.view.search

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.view.base.BaseFragment

class SearchFragment : BaseFragment() {
    override val layoutResource = R.layout.search_fragment

    private lateinit var viewModel: SearchViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

}
