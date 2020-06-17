package com.ebukom.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlin.reflect.KClass

abstract class BaseActivity : AppCompatActivity(){
    @LayoutRes
    protected abstract fun setView(): Int
    protected abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (setView() != 0) setContentView(setView())
        initView(savedInstanceState)
        hideKeyboard()
    }

    protected fun startActivity(aClass: Class<*>, finish: Boolean = false, clear: Boolean = false, bundle: Bundle = Bundle()) {
        val intent = Intent(this, aClass)
        intent.putExtras(bundle)
        if (clear) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        if (finish) finish()
    }


    protected fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    protected fun showKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_IMPLICIT)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    protected fun switchView(visibleView: View, invisibleView: View) {
        visibleView.visibility = View.VISIBLE
        invisibleView.visibility = View.GONE
    }


    fun initToolbar(toolbar : Toolbar, title : String = ""){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = title
    }

}