package br.com.bonnepet.view.pet


import Data
import RequestCode
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.util.extension.setSafeOnClickListener
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.pet.adapter.PetAdapter
import kotlinx.android.synthetic.main.fragment_pet.*

class PetFragment : BaseFragment(), PetAdapter.ItemClickListener {
    override val layoutResource = R.layout.fragment_pet
    override val fragmentTitle = R.string.my_pets
    private lateinit var viewModel: PetViewModel

    private val recyclerView by lazy { recycler_view }

    private val btnPetRegister by lazy { register_pet_btn }

    private lateinit var petAdapter: PetAdapter

    private val progressBar by lazy { progress_bar }

    private val swipeRefresh by lazy { swipe_refresh }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBarDisplayHome()
        viewModel = ViewModelProviders.of(this).get(PetViewModel::class.java)

        swipeRefresh.setColorSchemeColors((ContextCompat.getColor(activity!!, R.color.color_primary)))
        swipeRefresh.setOnRefreshListener {
            loadData()
        }

        petAdapter = PetAdapter(activity!!, ArrayList(), this)
        recyclerView.adapter = petAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        btnPetRegister.setSafeOnClickListener { startRegisterPetActivity() }
        loadData()

        viewModel.isLoading().observe(this, Observer { isLoading ->
            progressBar.isVisible = isLoading && !swipeRefresh.isRefreshing
        })
    }


    private fun startRegisterPetActivity() {
        startActivityForResult(Intent(context, PetRegisterActivity::class.java), RequestCode.PET_REGISTER)
    }

    private fun loadData(resetData: Boolean = true) {
        viewModel.getAllPets()
        viewModel.petList.observe(this, Observer { petList ->
            layout_empty.isVisible = petList.isEmpty()
            recyclerView.isVisible = petList.isNotEmpty()
            petAdapter.update(petList, resetData)
            swipeRefresh.isRefreshing = false
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.PET_REGISTER -> loadData()
                RequestCode.PET_EDITED -> loadData()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && SharedPreferencesUtil.getBoolean(Prefs.FETCH_PET_FRAGMENT)) {
            loadData()
            SharedPreferencesUtil.putBoolean(Prefs.FETCH_PET_FRAGMENT, false)
        }
    }

    override fun onItemClick(pet: PetDTO) {
        val intent = Intent(activity, PetDetailsActivity::class.java).apply {
            putExtra(Data.PET_DTO, pet)
        }
        startActivityForResult(intent, RequestCode.PET_EDITED)
    }
}
