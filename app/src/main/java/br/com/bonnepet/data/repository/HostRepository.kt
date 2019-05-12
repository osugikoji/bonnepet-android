package br.com.bonnepet.data.repository

import br.com.bonnepet.config.RetrofitConfig
import br.com.bonnepet.data.api.HostApi
import br.com.bonnepet.data.enums.GenderEnum
import br.com.bonnepet.data.enums.PetSizeEnum
import br.com.bonnepet.data.model.AddressDTO
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.model.NewHostDTO
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.data.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.create

/** Repositorio de pet */
class HostRepository {

    private val hostApi = RetrofitConfig.getInstance().create<HostApi>()
    private val schedulerProvider = SchedulerProvider(
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )

    private val hostList: MutableList<HostDTO> = mutableListOf(
        HostDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-9/47379592_2006731116109305_1469582824796323840_n.jpg?_nc_cat=101&_nc_ht=scontent.fcpq4-1.fna&oh=46926d22a40fa23e99573af91331eaba&oe=5D6F7DDF",
            "Koji",
            "Desde que me conheço como gente tive um cachorrinho ao meu lado. Dos meus 4 aos 17 anos vivi com Bonni Faccio, cheio de personalidade, que nos deixou de velhice, mas foi meu primeiro amigo pet.",
            arrayOf(
                PetDTO(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjs6srps1gQqrzujdY5jWx77GtXhiGC5zAHgvWTjhR-GrLTQuS",
                    "Tomy",
                    "Akita",
                    GenderEnum.MALE.name,
                    "2 anos e 5 meses",
                    PetSizeEnum.SMALL.name,
                    "Possui alergia a ração",
                    arrayListOf("Timido", "Destemido", "Covarde")
                ),
                PetDTO(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAu3262iyGDFqBRb_NdSRgn9NGI9M7C1NNzuubwgGWhhfg1ZdoCA",
                    "Greeg",
                    "Shi-Tzu",
                    GenderEnum.MALE.name,
                    "2 anos e 5 meses",
                    PetSizeEnum.SMALL.name,
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                    arrayListOf("Shy")
                )
            ).toList(),
            arrayOf("(19)98225-2031", "(19)3225-2031").toList(),
            "80",
            arrayOf("0", "1").toList(),
            AddressDTO("", "Jose Bernardinetti", "20", "Barão Geraldo", "Campinas", "São Paulo")
        ),
        HostDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-9/31654_1442854674748_7588399_n.jpg?_nc_cat=109&_nc_ht=scontent.fcpq4-1.fna&oh=9dc88bf534c5ad21d7052966b97c8b65&oe=5D705CE1",
            "Zu",
            "A coisa que eu mais detesto nessa vida são os cachorros. Nem fodendo meu!",
            arrayOf(
                PetDTO(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjs6srps1gQqrzujdY5jWx77GtXhiGC5zAHgvWTjhR-GrLTQuS",
                    "Tomy",
                    "Akita",
                    GenderEnum.MALE.name,
                    "2 anos e 5 meses",
                    PetSizeEnum.SMALL.name,
                    "Possui alergia a ração",
                    arrayListOf("Timido", "Destemido", "Covarde")
                )
            ).toList(),
            arrayOf("(19)98225-2031", "(19)3225-2031").toList(),
            "60",
            arrayOf("0").toList(),
            AddressDTO("", "Jose Bernardinetti", "40", "Barão Geraldo", "Campinas", "São Paulo")
        ),
        HostDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-9/53089473_2507472172600588_8239496085626683392_n.jpg?_nc_cat=104&_nc_ht=scontent.fcpq4-1.fna&oh=3fbf297780456120f10f9f3a509b2ac4&oe=5D2E9921",
            "Jo",
            "Ah não cara",
            arrayOf(
                PetDTO(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjs6srps1gQqrzujdY5jWx77GtXhiGC5zAHgvWTjhR-GrLTQuS",
                    "Tomy",
                    "Akita",
                    GenderEnum.MALE.name,
                    "2 anos e 5 meses",
                    PetSizeEnum.SMALL.name,
                    "Possui alergia a ração",
                    arrayListOf("Timido", "Destemido", "Covarde")
                )
            ).toList(),
            arrayOf("(19)98225-2031", "(19)3225-2031").toList(),
            "50",
            arrayOf("0", "1").toList(),
            AddressDTO("", "Jose Bernardinetti", "33", "Barão Geraldo", "Campinas", "São Paulo")
        ),
        HostDTO(
            "https://scontent.fcpq4-1.fna.fbcdn.net/v/t1.0-1/p160x160/38679668_1849579558460524_8769734777847676928_n.jpg?_nc_cat=110&_nc_ht=scontent.fcpq4-1.fna&oh=ad2d76c3ca5018fac7b48d79f96bdde0&oe=5D7341BA",
            "Sam",
            "Vendo minha arte na praia para sobreviver.",
            arrayOf(
                PetDTO(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjs6srps1gQqrzujdY5jWx77GtXhiGC5zAHgvWTjhR-GrLTQuS",
                    "Tomy",
                    "Akita",
                    GenderEnum.MALE.name,
                    "2 anos e 5 meses",
                    PetSizeEnum.SMALL.name,
                    "Possui alergia a ração",
                    arrayListOf("Timido", "Destemido", "Covarde")
                )
            ).toList(),
            arrayOf("(19)98225-2031", "(19)3225-2031").toList(),
            "999",
            arrayOf("0", "1", "2").toList(),
            AddressDTO("", "Jose Bernardinetti", "20", "Barão Geraldo", "Santos", "São Paulo")
        ),
        HostDTO(
            "",
            "Maligno",
            "Se reservar comigo, ensino sua criaturinha a implementar os métodos obrigatórios",
            arrayOf(
                PetDTO(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjs6srps1gQqrzujdY5jWx77GtXhiGC5zAHgvWTjhR-GrLTQuS",
                    "Tomy",
                    "Akita",
                    GenderEnum.MALE.name,
                    "2 anos e 5 meses",
                    PetSizeEnum.SMALL.name,
                    "Possui alergia a ração",
                    arrayListOf("Timido", "Destemido", "Covarde")
                )
            ).toList(),
            arrayOf("(19)98225-2031").toList(),
            "99",
            arrayOf("0", "1").toList(),
            AddressDTO("", "Jose Bernardinetti", "50", "Barão Geraldo", "Campinas", "São Paulo")
        )
    )

    fun registerHost(newHostDTO: NewHostDTO) : Completable {
        return hostApi.registerHost(newHostDTO).compose(schedulerProvider.getSchedulersForCompletable())
    }


    fun getAllHost(): MutableList<HostDTO> {
        return hostList
    }
}