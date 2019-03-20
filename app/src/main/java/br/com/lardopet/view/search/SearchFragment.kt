package br.com.lardopet.view.search

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import br.com.lardopet.R
import br.com.lardopet.view.base.BaseFragment

class SearchFragment : BaseFragment() {
    override val layoutResource = R.layout.search_fragment

    private lateinit var viewModel: SearchViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

}
