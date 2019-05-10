package br.com.bonnepet.view.pet


import Data
import RequestCode
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.model.PetDTO
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBarDisplayHome()
        viewModel = ViewModelProviders.of(this).get(PetViewModel::class.java)

        petAdapter = PetAdapter(activity!!, ArrayList(), this)
        recyclerView.adapter = petAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        btnPetRegister.setSafeOnClickListener { startRegisterPetActivity() }
        loadData(true)
    }


    private fun startRegisterPetActivity() {
        startActivityForResult(Intent(context, PetRegisterActivity::class.java), RequestCode.PET_REGISTER)
    }

    private fun loadData(resetData: Boolean) {
        viewModel.getAllPets()
        viewModel.petList.observe(this, Observer { petList ->
            petAdapter.update(petList, resetData)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.PET_REGISTER -> loadData(true)
            }
        }
    }

    override fun onItemClick(pet: PetDTO) {
        val intent = Intent(activity, PetDetailsActivity::class.java).apply {
            putExtra(Data.PET_DTO, pet)
        }
        startActivity(intent)
    }
}
