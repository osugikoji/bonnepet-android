package br.com.bonnepet.util.extension

import androidx.fragment.app.Fragment

fun Fragment.replaceFragment(frameId: Int, fragment: Fragment) {
    fragmentManager?.beginTransaction()?.replace(frameId, fragment)?.commit()
}