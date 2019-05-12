package br.com.bonnepet.view.pet.adapter

import Time
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.GenderEnum
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.util.extension.formatToPetAge
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.view.component.CircularProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.pet_item.view.*


class PetAdapter(
    private val context: Context,
    private var petList: MutableList<PetDTO>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pet_item, parent, false)
        return PetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bindView(petList[position])
    }

    fun update(petList: MutableList<PetDTO>, resetData: Boolean) {
        when (resetData) {
            true -> {
                this.petList.clear()
                this.petList.addAll(petList)
            }
            else -> this.petList.addAll(petList)
        }
        notifyDataSetChanged()
    }


    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val petLayout = itemView.layout_pet
        private val petImage = itemView.pet_image
        private val petName = itemView.pet_name
        private val petBreed = itemView.pet_breed
        private val petGender = itemView.pet_gender
        private val petAge = itemView.pet_age

        init {
            petLayout.setOnClickListener(this)
        }

        fun bindView(pet: PetDTO) {
            setPetImage(pet.pictureURL)
            petName.text = pet.name
            petBreed.text = pet.breed
            setPetGender(pet.gender)
            petAge.text = pet.birthDate?.formatToPetAge(context)
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(petList[adapterPosition])
        }

        private fun setPetGender(gender: String) {
            when (gender) {
                GenderEnum.MALE.name -> petGender.setImageResource(R.drawable.ic_gender_male)
                GenderEnum.FEMALE.name -> petGender.setImageResource(R.drawable.ic_gender_female)
            }
        }

        private fun setPetImage(imageURL: String?) {
            val progressBar = CircularProgressBar(context)
            progressBar.start()

            Glide.with(itemView.context)
                .load(imageURL)
                .error(R.drawable.ic_dog)
                .placeholder(progressBar)
                .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .circleCrop()
                .into(petImage)
        }
    }

    /**
     * Permite que classes externas definam o listener de click
     */
    interface ItemClickListener {
        fun onItemClick(pet: PetDTO)
    }
}