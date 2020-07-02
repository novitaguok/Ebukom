package com.ebukom.arch.ui.forgotpassword.resetpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ebukom.R
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        btnResetPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this@ResetPasswordActivity)

            builder.setMessage("Selamat, kamu berhasil membuat kata sandi baru, silakan login ya")
            builder.setPositiveButton("OK"){ dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}
