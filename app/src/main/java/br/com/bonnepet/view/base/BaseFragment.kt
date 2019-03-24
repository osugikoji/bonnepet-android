package br.com.bonnepet.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import br.com.bonnepet.R

abstract class BaseFragment : Fragment() {

    /**
     * @return o id do layout associado à view do fragment.
     */
    @get:LayoutRes
    protected abstract val layoutResource: Int

    protected abstract val fragmentTitle: Int

    private var toolbar: Toolbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutResource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onPrepareSupportActionBar()
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * A Toolbar será configurada como uma [ActionBar] se ela existir no layout
     */
    private fun onPrepareSupportActionBar() {
        toolbar = activity?.findViewById(R.id.tool_bar)
        val toolbarTitle: TextView? = toolbar?.findViewById(R.id.toolbar_title)
        if (toolbar != null) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            toolbarTitle?.text = getText(fragmentTitle)
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
}