package br.com.bonnepet.view.booking.adapter

import Time
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.bonnepet.R
import br.com.bonnepet.data.model.BookingDetailsDTO
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
    private var bookingDetailsList: MutableList<BookingDetailsDTO>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<RequestBookingAdapter.RequestBookingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestBookingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.request_booking_item, parent, false)
        return RequestBookingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookingDetailsList.size
    }

    override fun onBindViewHolder(holder: RequestBookingViewHolder, position: Int) {
        holder.bindView(bookingDetailsList[position])
    }

    fun update(bookingDetailsList: MutableList<BookingDetailsDTO>, resetData: Boolean) {
        when (resetData) {
            true -> {
                this.bookingDetailsList.clear()
                this.bookingDetailsList.addAll(bookingDetailsList)
            }
            else -> this.bookingDetailsList.addAll(bookingDetailsList)
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

        fun bindView(bookingDetails: BookingDetailsDTO) {
//            setUserImage(bookingDetails.host.pictureURL)
//            userName.text = bookingDetails.host.name
//            city.text = bookingDetails.host.addressDTO.city
//            district.text = bookingDetails.host.addressDTO.district
//            totalPrice.text = bookingDetails.host.totalPrice
            setBookingStatus(bookingDetails.status)
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(bookingDetailsList[adapterPosition])
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
        fun onItemClick(bookingDetails: BookingDetailsDTO)
    }
}