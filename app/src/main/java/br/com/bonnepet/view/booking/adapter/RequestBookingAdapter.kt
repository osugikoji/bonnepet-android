package br.com.bonnepet.view.booking.adapter

import Time
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.BookingStatusEnum
import br.com.bonnepet.data.model.HostDTO
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.request_booking_item.view.*

class RequestBookingAdapter(
    private val context: Context,
    private var hostList: MutableList<HostDTO>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<RequestBookingAdapter.RequestBookingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestBookingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.request_booking_item, parent, false)
        return RequestBookingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hostList.size
    }

    override fun onBindViewHolder(holder: RequestBookingViewHolder, position: Int) {
        holder.bindView(hostList[position])
    }

    fun update(bookingDetailsList: MutableList<HostDTO>, resetData: Boolean) {
        when (resetData) {
            true -> {
                this.hostList.clear()
                this.hostList.addAll(bookingDetailsList)
            }
            else -> this.hostList.addAll(bookingDetailsList)
        }
        notifyDataSetChanged()
    }


    inner class RequestBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val bookLayout = itemView.layout_booking
        private val userImage = itemView.user_image
        private val userName = itemView.text_user_name
        private val city = itemView.text_city
        private val district = itemView.text_district
        private val takeDate = itemView.text_take_date
        private val getDate = itemView.text_get_date
        private val totalPrice = itemView.text_price_value
        private val bookingStatus = itemView.statusColor

        init {
            bookLayout.setOnClickListener(this)
        }

        fun bindView(hostList: HostDTO) {
            setUserImage(hostList.profileDTO.profileImageURL)
            userName.text = hostList.profileDTO.userName
            city.text = hostList.profileDTO.addressDTO.city
            district.text = hostList.profileDTO.addressDTO.district
            takeDate.text = hostList.bookingDetailsDTO?.stayInitialDate
            getDate.text = hostList.bookingDetailsDTO?.stayInitialDate
            val price = "R$ ${hostList.bookingDetailsDTO?.totalPrice}"
            totalPrice.text = price
            setBookingStatus(hostList.bookingDetailsDTO!!.status)
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(hostList[adapterPosition])
        }

        private fun setBookingStatus(status: String) {
            val statusEnum = BookingStatusEnum.getStatusEnum(status)
            bookingStatus.setChipBackgroundColorResource(statusEnum!!.color)
            bookingStatus.setText(statusEnum.description)
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
        fun onItemClick(hostList: HostDTO)
    }
}