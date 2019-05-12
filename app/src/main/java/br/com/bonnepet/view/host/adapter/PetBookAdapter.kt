package br.com.bonnepet.view.host.adapter

import Time
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import br.com.bonnepet.R
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.view.component.CircularProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.pet_book_item.view.*


class PetBookAdapter(
    private val context: Context,
    private var petList: MutableList<PetDTO>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<PetBookAdapter.PetBookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetBookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pet_book_item, parent, false)
        return PetBookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    override fun onBindViewHolder(holder: PetBookViewHolder, position: Int) {
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


    inner class PetBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        CompoundButton.OnCheckedChangeListener {
        private val petImage = itemView.pet_image
        private val petName = itemView.pet_name
        private val petBreed = itemView.pet_breed
        private val switchPet = itemView.switch_pet

        fun bindView(pet: PetDTO) {
            setPetImage(pet.pictureURL)
            petName.text = pet.name
            petBreed.text = pet.breed
            switchPet.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            itemClickListener.onSwitchClick(petList[adapterPosition], isChecked)
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
        fun onSwitchClick(pet: PetDTO, isChecked: Boolean)
    }
}