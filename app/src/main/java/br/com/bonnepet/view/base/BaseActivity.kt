package br.com.bonnepet.view.base

import Prefs
import SharedPreferencesUtil
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.bonnepet.R
import br.com.bonnepet.util.extension.progressDialog
import java.util.*


abstract class BaseActivity : AppCompatActivity() {

    /**
     * @return o id do layout associado à view da activity.
     */
    @get:LayoutRes
    protected abstract val layoutResource: Int

    protected abstract val activityTitle: Int?

    private var toolbar: Toolbar? = null

    private val progressDialog by lazy { progressDialog() }

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
            toolbarTitle?.text = getText(activityTitle ?: return)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    protected fun progressDialogVisibility(visibility: Boolean) {
        if (visibility) progressDialog.show()
        else progressDialog.dismiss()
    }

    /**
     * Remove o botão de voltar da [ActionBar]
     */
    protected fun hideActionBarDisplayHome() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    protected fun showSupportActionBarTitle() =
        supportActionBar?.setDisplayShowTitleEnabled(true)

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     *  Atualiza o idioma de acordo com o que o usuario selecionou
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }

    private fun updateBaseContextLocale(context: Context): Context {
        val language = SharedPreferencesUtil.getString(Prefs.SELECTED_LANGUAGE)

        val locale = Locale(language)
        Locale.setDefault(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else updateResourcesLocaleLegacy(context, locale)

    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResourcesLocale(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}

