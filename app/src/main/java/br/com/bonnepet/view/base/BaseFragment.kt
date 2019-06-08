package br.com.bonnepet.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import br.com.bonnepet.R
import br.com.bonnepet.util.extension.progressDialog
import kotlinx.android.synthetic.main.tool_bar.*

abstract class BaseFragment : Fragment() {

    /**
     * @return o id do layout associado à view do fragment.
     */
    @get:LayoutRes
    protected abstract val layoutResource: Int

    protected abstract val fragmentTitle: Int?

    private var toolbar: Toolbar? = null

    private var v: View? = null

    private val progressDialog by lazy { progressDialog() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (v == null) {
            v = inflater.inflate(layoutResource, container, false)
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onPrepareSupportActionBar()
        super.onViewCreated(view, savedInstanceState)
    }

    protected fun progressDialogVisibility(visibility: Boolean) {
        if (visibility) progressDialog.show()
        else progressDialog.dismiss()
    }

    /**
     * A Toolbar será configurada como uma [ActionBar] se ela existir no layout
     */
    private fun onPrepareSupportActionBar() {
        toolbar = tool_bar
        val toolbarTitle: TextView? = toolbar?.findViewById(R.id.toolbar_title)
        if (toolbar != null) {
            toolbarTitle?.text = getText(fragmentTitle ?: return)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    /**
     * Remove o botão de voltar da [ActionBar]
     */
    protected fun hideActionBarDisplayHome() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    protected fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}