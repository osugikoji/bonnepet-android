package br.com.bonnepet.view.base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.bonnepet.R
import br.com.bonnepet.util.extension.isVisible

abstract class BaseActivity : AppCompatActivity() {

    /**
     * @return o id do layout associado à view da activity.
     */
    @get:LayoutRes
    protected abstract val layoutResource: Int

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
        onPrepareSupportActionBar()
        onPrepareActivity(savedInstanceState)
    }


    /**
     * Método invocado durante o OnCreate da Activity
     * Fazer um Override dele para configurar a activity e inicializar a view
     * @param state a bundle recebida pela activity, utiliza-la para recuperar informações salvas
     * assim como era feito no onCreate
     */
    protected abstract fun onPrepareActivity(state: Bundle?)

    /**
     * A Toolbar será configurada como uma [ActionBar] se ela existir no layout
     */
    private fun onPrepareSupportActionBar() {
        toolbar = findViewById(R.id.tool_bar)
        val toolbarTitle: TextView? = toolbar?.findViewById(R.id.toolbar_title)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            toolbarTitle?.text = toolbar?.title
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    /**
     * Remove o botão de voltar da [ActionBar]
     */
    protected fun hideActionBarDisplayHome() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Mostra o progress bar da view, caso ele exista
     */
    protected open fun showLoading() {
        val view = findViewById<View>(R.id.progress_bar)
        if (view != null) {
            view.isVisible = true
        }
    }

    /**
     * Esconde o progress bar da view, caso ele exista
     */
    protected open fun hideLoading() {
        val view = findViewById<View>(R.id.progress_bar)
        if (view != null) {
            view.isVisible = false
        }
    }

    protected fun checkWriteExternalPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            val permissionCheck =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RequestCode.PERMISSION)
                return false
            }
            return true
        }
        return true
    }
}

