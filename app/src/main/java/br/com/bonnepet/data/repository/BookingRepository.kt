package br.com.bonnepet.data.repository

import br.com.bonnepet.config.RetrofitConfig
import br.com.bonnepet.data.api.BookingApi
import br.com.bonnepet.data.enums.BookingStatusEnum
import br.com.bonnepet.data.model.*
import br.com.bonnepet.data.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.create

/** Repositorio de Reservas */
class BookingRepository {

    private val bookingApi = RetrofitConfig.getInstance().create<BookingApi>()
    private val schedulerProvider = SchedulerProvider(
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )

    private val hostBookingList: MutableList<HostBookingDTO> = mutableListOf(
        HostBookingDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-9/47379592_2006731116109305_1469582824796323840_n.jpg?_nc_cat=101&_nc_ht=scontent.fcpq4-1.fna&oh=46926d22a40fa23e99573af91331eaba&oe=5D6F7DDF",
            UserDTO("", "", "Koji", "", "", "", "", AddressDTO("", "", "", "Barão Geraldo", "Campinas", "")),
            BookingStatusEnum.OPEN.name
        ), HostBookingDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-9/31654_1442854674748_7588399_n.jpg?_nc_cat=109&_nc_ht=scontent.fcpq4-1.fna&oh=9dc88bf534c5ad21d7052966b97c8b65&oe=5D705CE1",
            UserDTO("", "", "Zuzu", "", "", "", "", AddressDTO("", "", "", "Cambuí", "Campinas", "")),
            BookingStatusEnum.CONFIRMED.name
        ), HostBookingDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-9/53089473_2507472172600588_8239496085626683392_n.jpg?_nc_cat=104&_nc_ht=scontent.fcpq4-1.fna&oh=3fbf297780456120f10f9f3a509b2ac4&oe=5D2E9921",
            UserDTO("", "", "Jojo", "", "", "", "", AddressDTO("", "", "", "Taquaral", "Campinas", "")),
            BookingStatusEnum.CONFIRMED.name
        ), HostBookingDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-1/p160x160/38679668_1849579558460524_8769734777847676928_n.jpg?_nc_cat=110&_nc_ht=scontent.fcpq4-1.fna&oh=ad2d76c3ca5018fac7b48d79f96bdde0&oe=5D7341BA",
            UserDTO("", "", "Samu", "", "", "", "", AddressDTO("", "", "", "Barao Geraldo", "Campinas", "")),
            BookingStatusEnum.REFUSED.name
        ), HostBookingDTO(
            "",
            UserDTO("", "", "Teste", "", "", "", "", AddressDTO("", "", "", "Jardim Chapadao", "Campinas", "")),
            BookingStatusEnum.REFUSED.name
        ), HostBookingDTO(
            "",
            UserDTO("", "", "Teste4", "", "", "", "", AddressDTO("", "", "", "Jardim Chapadao", "Campinas", "")),
            BookingStatusEnum.REFUSED.name
        ), HostBookingDTO(
            "",
            UserDTO("", "", "Teste5", "", "", "", "", AddressDTO("", "", "", "Jardim Chapadao", "Campinas", "")),
            BookingStatusEnum.CONFIRMED.name
        ), HostBookingDTO(
            "",
            UserDTO("", "", "Teste8", "", "", "", "", AddressDTO("", "", "", "Jardim Chapadao", "Campinas", "")),
            BookingStatusEnum.OPEN.name
        ), HostBookingDTO(
            "",
            UserDTO("", "", "Teste2", "", "", "", "", AddressDTO("", "", "", "Dom Pedro", "Campinas", "")),
            BookingStatusEnum.REFUSED.name
        ), HostBookingDTO(
            "",
            UserDTO("", "", "Maria da Silva", "", "", "", "", AddressDTO("", "", "", "Jardim Chapadao", "Campinas", "")),
            BookingStatusEnum.CONFIRMED.name
        ), HostBookingDTO(
            "",
            UserDTO("", "", "Teste", "", "", "", "", AddressDTO("", "", "", "Jardim Chapadao", "Campinas", "")),
            BookingStatusEnum.OPEN.name
        )
    )

    fun insertBooking(newBookingDTO: NewBookingDTO): Completable {
        return bookingApi.insertBooking(newBookingDTO).compose(schedulerProvider.getSchedulersForCompletable())
    }

    fun getHostBookings(): MutableList<HostBookingDTO> {
        return hostBookingList
    }

    fun getRequestBookings(): Single<List<HostDTO>> {
        return bookingApi.getRequestedBookings().compose(schedulerProvider.getSchedulersForSingle())
    }

}