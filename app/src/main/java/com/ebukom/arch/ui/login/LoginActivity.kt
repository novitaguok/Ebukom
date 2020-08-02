package com.ebukom.arch.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.ebukom.R
import com.ebukom.arch.ui.chooseclass.ChooseClassActivity
import com.ebukom.arch.ui.forgotpassword.sendcode.SendCodeActivity
import com.ebukom.arch.ui.register.parent.RegisterParentActivity
import com.ebukom.arch.ui.register.school.RegisterSchoolActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.loading
import kotlinx.android.synthetic.main.bottom_sheet_login.view.*

class LoginActivity : AppCompatActivity() {

    lateinit var sharePref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

        // Register button
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_login, null)

        bottomSheetDialog.setContentView(view)
        btnLoginRegister.setOnClickListener {
            bottomSheetDialog.show()
        }
        view.tvRegisSchool.setOnClickListener {
            bottomSheetDialog.dismiss()
            startActivity(Intent(this, RegisterSchoolActivity::class.java))
        }
        view.tvRegisParent.setOnClickListener {
            bottomSheetDialog.dismiss()
            startActivity(Intent(this, RegisterParentActivity::class.java))
        }

        // Intent to Choose Class
        btnLoginLogin.setOnClickListener {

            val intent = Intent(this, ChooseClassActivity::class.java)

            val phone = etLoginPhone.text.toString()

            loading.visibility = View.VISIBLE
            if (etLoginPhone.text.toString() == "000") {
                if (etLoginPassword.text.toString() == "000") {
                    // Teacher
                    sharePref.edit().apply {
                        putBoolean("isLogin", true)
                        putString("phone", phone)
                        putInt("level", 0)
                    }.apply()

                    Handler().postDelayed({
                        loading.visibility = View.GONE
                        startActivity(intent)
                        finish()
                    }, 1000)

                } else {
                    Handler().postDelayed({
                        loading.visibility = View.GONE
                        tvLoginPhoneErrorMessage.visibility = View.GONE
                        tvLoginPasswordErrorMessage.visibility = View.VISIBLE
                    }, 1000)
                }
            } else if (etLoginPhone.text.toString() == "123") {
                // Parent
                if (etLoginPassword.text.toString() == "123") {
                    sharePref.edit().apply {
                        putBoolean("isLogin", true)
                        putString("phone", phone)
                        putInt("level", 1)
                    }.apply()

                    Handler().postDelayed({
                        loading.visibility = View.GONE
                        startActivity(intent)
                        finish()
                    }, 1000)
                } else {
                    Handler().postDelayed({
                        loading.visibility = View.GONE
                        tvLoginPhoneErrorMessage.visibility = View.GONE
                        tvLoginPasswordErrorMessage.visibility = View.VISIBLE
                    }, 1000)
                }
            } else if (etLoginPhone.text.toString() == "456") {
                // Admin
                if (etLoginPassword.text.toString() == "456") {
                    sharePref.edit().apply {
                        putBoolean("isLogin", true)
                        putString("phone", phone)
                        putInt("level", 2)
                    }.apply()

                    Handler().postDelayed({
                        loading.visibility = View.GONE
                        startActivity(intent)
                        finish()
                    }, 1000)
                } else {
                    Handler().postDelayed({
                        loading.visibility = View.GONE
                        tvLoginPhoneErrorMessage.visibility = View.GONE
                        tvLoginPasswordErrorMessage.visibility = View.VISIBLE
                    }, 1000)
                }
            } else {
                Handler().postDelayed({
                    loading.visibility = View.GONE
                    tvLoginPhoneErrorMessage.visibility = View.VISIBLE
                    tvLoginPasswordErrorMessage.visibility = View.VISIBLE
                }, 1000)
            }
        }

        // Intent to Forget Password Send Code page
        tvLoginForgetPassword.setOnClickListener {
            val intent = Intent(this, SendCodeActivity::class.java)
            startActivity(intent)
        }
    }
}
