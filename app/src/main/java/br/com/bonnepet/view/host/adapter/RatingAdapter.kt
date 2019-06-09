package br.com.bonnepet.view.host.adapter

import Time
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bonnepet.R
import br.com.bonnepet.data.model.RateDetailsDTO
import br.com.bonnepet.view.component.CircularProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.rating_item.view.*


class RatingAdapter(
    private val context: Context,
    private var ratingList: MutableList<RateDetailsDTO>
) : RecyclerView.Adapter<RatingAdapter.RateViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rating_item, parent, false)
        return RateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ratingList.size
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bindView(ratingList[position])
    }

    fun update(rateList: MutableList<RateDetailsDTO>, resetData: Boolean) {
        when (resetData) {
            true -> {
                this.ratingList.clear()
                this.ratingList.addAll(rateList)
            }
            else -> this.ratingList.addAll(rateList)
        }
        notifyDataSetChanged()
    }


    inner class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val userImage = itemView.image_user
        private val userName = itemView.text_name
        private val ratingValue = itemView.rating_bar
        private val feedback = itemView.text_feedback
        private val initialDate = itemView.date_initial
        private val finalDate = itemView.date_final

        fun bindView(rateDTO: RateDetailsDTO) {
            setUserImage(rateDTO.userImage)
            userName.text = rateDTO.userName
            feedback.text = rateDTO.feedback
            ratingValue.rating = rateDTO.rate.toFloat()
            initialDate.text = rateDTO.initialDate
            finalDate.text = rateDTO.finalDate
        }

        private fun setUserImage(imageURL: String?) {
            val progressBar = CircularProgressBar(context)
            progressBar.start()

            Glide.with(itemView.context)
                .load(imageURL)
                .error(R.drawable.ic_account_circle)
                .placeholder(progressBar)
                .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .circleCrop()
                .into(userImage)
        }
    }
}