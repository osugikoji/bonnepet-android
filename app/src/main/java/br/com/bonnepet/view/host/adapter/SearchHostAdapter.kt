package br.com.bonnepet.view.host.adapter

import Time
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bonnepet.R
import br.com.bonnepet.data.model.HostDTO
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.host_card_item.view.*

class SearchHostAdapter(
    private val context: Context,
    private var hostList: MutableList<HostDTO>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<SearchHostAdapter.HostViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.host_card_item, parent, false)
        return HostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hostList.size
    }

    override fun onBindViewHolder(holder: HostViewHolder, position: Int) {
        holder.bindView(hostList[position])
    }

    fun update(hostList: MutableList<HostDTO>, resetData: Boolean) {
        when (resetData) {
            true -> {
                this.hostList.clear()
                this.hostList.addAll(hostList)
            }
            else -> this.hostList.addAll(hostList)
        }
        notifyDataSetChanged()
    }


    inner class HostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val hostLayout = itemView.layout_host
        private val userImage = itemView.user_image
        private val userName = itemView.text_user_name
        private val city = itemView.text_city
        private val district = itemView.text_district
        private val price = itemView.text_money_value
        private val rateAvg = itemView.text_rate_avg

        init {
            hostLayout.setOnClickListener(this)
        }

        fun bindView(host: HostDTO) {
            setUserImage(host.profileDTO.profileImageURL)
            userName.text = host.profileDTO.userName
            city.text = host.profileDTO.addressDTO.city
            district.text = host.profileDTO.addressDTO.district
            price.text = host.price
            rateAvg.text = if (host.rateAvg.isNullOrEmpty()) context.getString(R.string.new_host) else host.rateAvg
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(hostList[adapterPosition])
        }

        private fun setUserImage(imageURL: String?) {
            Glide.with(itemView.context)
                .load(imageURL)
                .error(R.drawable.ic_account_circle)
                .placeholder(R.drawable.ic_account_circle)
                .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(userImage)
        }
    }

    /**
     * Permite que classes externas definam o listener de click
     */
    interface ItemClickListener {
        fun onItemClick(host: HostDTO)
    }
}