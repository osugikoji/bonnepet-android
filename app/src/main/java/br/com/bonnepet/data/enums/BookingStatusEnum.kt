package br.com.bonnepet.data.enums

import br.com.bonnepet.R

enum class BookingStatusEnum(val code: Int, val description: Int, val color: Int) {
    OPEN(0, R.string.status_open, R.color.yellow),
    CONFIRMED(1, R.string.status_confirmed, R.color.green),
    REFUSED(2, R.string.status_refused, R.color.light_red),
    FINALIZED(3, R.string.status_finalized, R.color.color_accent);

    companion object {
        fun getStatusDescription(statusName: String): Int {
            values().forEach {
                if (it.name == statusName) return it.description
            }
            return R.string.status_error
        }

        fun getStatusEnum(statusName: String): BookingStatusEnum? {
            values().forEach {
                if (it.name == statusName) return it
            }
            return null
        }
    }

}