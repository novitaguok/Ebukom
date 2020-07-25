package com.ebukom.arch.ui.forgotpassword.resetpassword

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ebukom.R
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.toolbar

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        initToolbar()

        btnResetPassword.setOnClickListener {
            if (etResetPasswordNewPassword.text.toString()
                    .isNotEmpty() && etResetPasswordConfirmPassword.text.toString().isNotEmpty()
            ) {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
                    it.windowToken,
                    0
                )

                val builder = AlertDialog.Builder(this@ResetPasswordActivity)
                builder.setMessage("Selamat, kamu berhasil membuat kata sandi baru, silakan login ya")
                builder.setPositiveButton("OK") { dialog, which ->
                    Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()

                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            }
        }
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
