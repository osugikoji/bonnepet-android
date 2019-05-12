package br.com.bonnepet.view.booking.adapter

import Time
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.bonnepet.R
import br.com.bonnepet.data.model.RequestBookingDTO
import br.com.bonnepet.data.enums.BookingStatusEnum
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.booking_item.view.layout_booking
import kotlinx.android.synthetic.main.booking_item.view.statusColor
import kotlinx.android.synthetic.main.booking_item.view.text_city
import kotlinx.android.synthetic.main.booking_item.view.text_district
import kotlinx.android.synthetic.main.booking_item.view.text_user_name
import kotlinx.android.synthetic.main.booking_item.view.user_image
import kotlinx.android.synthetic.main.request_booking_item.view.*

class RequestBookingAdapter(
    private val context: Context,
    private var requestBookingList: MutableList<RequestBookingDTO>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<RequestBookingAdapter.RequestBookingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestBookingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.request_booking_item, parent, false)
        return RequestBookingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return requestBookingList.size
    }

    override fun onBindViewHolder(holder: RequestBookingViewHolder, position: Int) {
        holder.bindView(requestBookingList[position])
    }

    fun update(requestBookingList: MutableList<RequestBookingDTO>, resetData: Boolean) {
        when (resetData) {
            true -> {
                this.requestBookingList.clear()
                this.requestBookingList.addAll(requestBookingList)
            }
            else -> this.requestBookingList.addAll(requestBookingList)
        }
        notifyDataSetChanged()
    }


    inner class RequestBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val petLayout = itemView.layout_booking
        private val userImage = itemView.user_image
        private val userName = itemView.text_user_name
        private val city = itemView.text_city
        private val district = itemView.text_district
        private val price = itemView.text_money_value
        private val bookingStatus = itemView.statusColor

        init {
            petLayout.setOnClickListener(this)
        }

        fun bindView(requestBooking: RequestBookingDTO) {
            setUserImage(requestBooking.host.pictureURL)
            userName.text = requestBooking.host.name
            city.text = requestBooking.host.addressDTO.city
            district.text = requestBooking.host.addressDTO.district
            price.text = requestBooking.host.price
            setBookingStatus(requestBooking.status)
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(requestBookingList[adapterPosition])
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
        fun onItemClick(requestBooking: RequestBookingDTO)
    }
}