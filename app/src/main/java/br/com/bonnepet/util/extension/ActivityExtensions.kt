package br.com.bonnepet.util.extension

import RequestCode
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}


fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment) {
    supportFragmentManager.inTransaction { setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(frameId, fragment).setPrimaryNavigationFragment(fragment) }
}

fun Activity.checkWriteExternalPermission(): Boolean {
    if (Build.VERSION.SDK_INT >= 23) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RequestCode.PERMISSION
            )
            return false
        }
        return true
    }
    return true
}