package com.ebukom.arch.ui.forgotpassword.verification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.ui.forgotpassword.resetpassword.ResetPasswordActivity
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.android.synthetic.main.activity_verification.toolbar

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        initToolbar()

        btnVerificationReset.setOnClickListener {
//            val builder = AlertDialog.Builder(this@VerificationActivity)
//
//            builder.setMessage("Akun kamu berhasil diverifikasi. Selamat menggunakan.")
//            builder.setPositiveButton("OK"){ dialog, which ->
//                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
//            }
//
//            val dialog: AlertDialog = builder.create()
//            dialog.show()
            loading.visibility = View.VISIBLE
            if (txt_pin_entry.text.toString() == "0000") {
                Handler().postDelayed({
                    loading.visibility = View.GONE
                    startActivity(Intent(this, ResetPasswordActivity::class.java))
                    finish()
                }, 1000)
            } else {
                Handler().postDelayed({
                    loading.visibility = View.GONE
                    tvVerificationErrorMessage.visibility = View.VISIBLE
                }, 1000)
            }
        }

        tvVerificationNotReceiveCode.setOnClickListener {
            Handler().postDelayed({
                tvVerificationCodeReceived.visibility = View.VISIBLE
                tvVerificationNotReceiveCode.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorRed
                    )
                )
            }, 1000)
        }

        txt_pin_entry.setOnClickListener {
            tvVerificationCodeReceived.visibility = View.GONE
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
