package br.com.bonnepet.util.extension

import androidx.fragment.app.Fragment
import br.com.bonnepet.R

fun Fragment.replaceFragment(frameId: Int, fragment: Fragment) {
    fragmentManager?.beginTransaction()?.
        setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)?.
        replace(frameId, fragment)?.
        addToBackStack(null)?.
        commit()
}

fun Fragment.popBackStack() {
    fragmentManager?.popBackStack()
}