package br.com.bonnepet.data.enums

import br.com.bonnepet.R

enum class BookingStatusEnum(val code: Int, private val description: Int) {
    OPEN(0, R.string.status_open),
    CONFIRMED(1, R.string.status_confirmed),
    REFUSED(2, R.string.status_refused),
    FINALIZED(3, R.string.status_finalized);

    companion object {
        fun getStatusDescription(statusName: String): Int {
            values().forEach {
                if (it.name == statusName) return it.description
            }
            return R.string.status_error
        }
    }

}