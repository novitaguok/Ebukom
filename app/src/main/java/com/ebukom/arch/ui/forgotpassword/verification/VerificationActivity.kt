package com.ebukom.arch.ui.forgotpassword.verification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ebukom.R
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_verification.*

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        btnVerificationReset.setOnClickListener {
            val builder = AlertDialog.Builder(this@VerificationActivity)

            builder.setMessage("Akun kamu berhasil diverifikasi. Selamat menggunakan.")
            builder.setPositiveButton("OK"){ dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}
