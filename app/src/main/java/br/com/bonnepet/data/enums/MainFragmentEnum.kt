package br.com.bonnepet.data.enums

import androidx.fragment.app.Fragment
import br.com.bonnepet.R
import br.com.bonnepet.view.booking.BookingFragment
import br.com.bonnepet.view.host.searchHost.SearchHostFragment
import br.com.bonnepet.view.login.LoginFragment
import br.com.bonnepet.view.menu.MenuFragment
import br.com.bonnepet.view.pet.PetFragment

enum class MainFragmentEnum(val id: Int?, val instance: Fragment) {
    LOGIN(null, LoginFragment()),
    SEARCH(R.id.navigation_search, SearchHostFragment()),
    BOOKING(R.id.navigation_reservations, BookingFragment()),
    PET(R.id.navigation_pet, PetFragment()),
    MENU(R.id.navigation_menu, MenuFragment())
}