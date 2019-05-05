package br.com.bonnepet.data.repository

import br.com.bonnepet.config.RetrofitConfig
import br.com.bonnepet.data.api.PetApi
import br.com.bonnepet.data.model.AddressDTO
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.util.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.create

/** Repositorio de pet */
class HostRepository {

    private val petApi = RetrofitConfig.getInstance().create<PetApi>()
    private val schedulerProvider = SchedulerProvider(
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )

    private val hostList: MutableList<HostDTO> = mutableListOf(
        HostDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-9/47379592_2006731116109305_1469582824796323840_n.jpg?_nc_cat=101&_nc_ht=scontent.fcpq4-1.fna&oh=46926d22a40fa23e99573af91331eaba&oe=5D6F7DDF",
            "Koji",
            "80",
            AddressDTO("", "Jose Bernardinetti", "", "Barão Geraldo", "Campinas", "São Paulo")
        ),
        HostDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-9/31654_1442854674748_7588399_n.jpg?_nc_cat=109&_nc_ht=scontent.fcpq4-1.fna&oh=9dc88bf534c5ad21d7052966b97c8b65&oe=5D705CE1",
            "Zu",
            "60",
            AddressDTO("", "Jose Bernardinetti", "", "Barão Geraldo", "Campinas", "São Paulo")
        ),
        HostDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-9/53089473_2507472172600588_8239496085626683392_n.jpg?_nc_cat=104&_nc_ht=scontent.fcpq4-1.fna&oh=3fbf297780456120f10f9f3a509b2ac4&oe=5D2E9921",
            "Jo",
            "50",
            AddressDTO("", "Jose Bernardinetti", "", "Barão Geraldo", "Campinas", "São Paulo")
        ),
        HostDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-1/p160x160/38679668_1849579558460524_8769734777847676928_n.jpg?_nc_cat=110&_nc_ht=scontent.fcpq4-1.fna&oh=ad2d76c3ca5018fac7b48d79f96bdde0&oe=5D7341BA",
            "Sam",
            "999",
            AddressDTO("", "Jose Bernardinetti", "", "Barão Geraldo", "Santos", "São Paulo")
        ),
        HostDTO(
            "",
            "Maligno",
            "99",
            AddressDTO("", "Jose Bernardinetti", "", "Barão Geraldo", "Campinas", "São Paulo")
        )
    )


    fun getAllHost(): MutableList<HostDTO> {
        return hostList
    }
}