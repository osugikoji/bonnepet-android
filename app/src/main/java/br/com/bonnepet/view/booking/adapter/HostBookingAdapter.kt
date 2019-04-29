package br.com.bonnepet.view.booking.adapter

import Time
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.bonnepet.R
import br.com.bonnepet.data.model.HostBookingDTO
import br.com.bonnepet.util.data.BookingStatusEnum
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.booking_item.view.*

class HostBookingAdapter(
    private val context: Context,
    private var hostBookingList: MutableList<HostBookingDTO>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<HostBookingAdapter.HostBookingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostBookingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.booking_item, parent, false)
        return HostBookingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hostBookingList.size
    }

    override fun onBindViewHolder(holder: HostBookingViewHolder, position: Int) {
        holder.bindView(hostBookingList[position])
    }

    fun update(hostBookingList: MutableList<HostBookingDTO>, resetData: Boolean) {
        when (resetData) {
            true -> {
                this.hostBookingList.clear()
                this.hostBookingList.addAll(hostBookingList)
            }
            else -> this.hostBookingList.addAll(hostBookingList)
        }
        notifyDataSetChanged()
    }


    inner class HostBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val petLayout = itemView.layout_booking
        private val userImage = itemView.user_image
        private val userName = itemView.text_user_name
        private val city = itemView.text_city
        private val district = itemView.text_district
        private val bookingStatus = itemView.statusColor

        init {
            petLayout.setOnClickListener(this)
        }

        fun bindView(hostBooking: HostBookingDTO) {
            setUserImage(hostBooking.pictureURL)
            userName.text = hostBooking.user.name
            city.text = hostBooking.user.addressDTO?.city
            district.text = hostBooking.user.addressDTO?.district
            setBookingStatus(hostBooking.status)
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(hostBookingList[adapterPosition])
        }

        private fun setBookingStatus(status: String) {
            when (status) {
                BookingStatusEnum.OPEN.name -> bookingStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
                BookingStatusEnum.CONFIRMED.name -> bookingStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                BookingStatusEnum.REFUSED.name -> bookingStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
            }
        }

        private fun setUserImage(imageURL: String) {
            Glide.with(itemView.context)
                .load(imageURL)
                .error(R.drawable.ic_account_circle)
                .placeholder(R.drawable.ic_account_circle)
                .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .circleCrop()
                .into(userImage)
        }
    }

    /**
     * Permite que classes externas definam o listener de click
     */
    interface ItemClickListener {
        fun onItemClick(hostBooking: HostBookingDTO)
    }
}