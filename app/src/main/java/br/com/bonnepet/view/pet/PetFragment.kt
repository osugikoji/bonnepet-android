package br.com.bonnepet.view.pet


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_pet.*

class PetFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_pet
    override val fragmentTitle = R.string.my_pets
    private lateinit var viewModel: PetViewModel

    private val recyclerView by lazy { recycler_view }

    lateinit var petAdapter: PetAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBarDisplayHome()
        viewModel = ViewModelProviders.of(this).get(PetViewModel::class.java)

        petAdapter = PetAdapter(activity!!, ArrayList())
        recyclerView.adapter = petAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        loadData(true)
    }

    private fun loadData(resetData: Boolean) {
        viewModel.getAllPets()
        viewModel.petList.observe(this, Observer { petList ->
            petAdapter.update(petList, resetData)
        })
    }
}
