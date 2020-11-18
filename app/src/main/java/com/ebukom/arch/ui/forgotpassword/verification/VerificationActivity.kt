package com.ebukom.arch.ui.forgotpassword.verification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.ui.forgotpassword.resetpassword.ResetPasswordActivity
import com.ebukom.arch.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.android.synthetic.main.activity_verification.loading
import kotlinx.android.synthetic.main.activity_verification.toolbar

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        initToolbar()

        val role = intent.getIntExtra("role", 0)
        when (role) {
            0 -> {
                btnVerificationReset.setOnClickListener {
                    loading.visibility = View.VISIBLE
                    if (txt_pin_entry.text.toString() == "0000") {
                        Handler().postDelayed({
                            loading.visibility = View.GONE
                            startActivity(Intent(this, ResetPasswordActivity::class.java))
                        }, 1000)
                    } else {
                        Handler().postDelayed({
                            loading.visibility = View.GONE
                            tvVerificationErrorMessage.visibility = View.VISIBLE
                        }, 1000)
                    }
                }
            }
            else -> {
                tvToolbarTitle.text = "Verifikasi Akun"
                tvVerificationText.text = "Sebelum akun Anda bisa digunakan, Anda harus verifikasi terlebih dahulu. Kami sudah mengirimkan kode ke nomor telepon Anda melalui SMS. Silakan memasukkan 4 digit kode tersebut"
                btnVerificationReset.setText("VERIFIKASI AKUN")

                btnVerificationReset.setOnClickListener {
                    Handler().postDelayed({
                        loading.visibility = View.GONE

                        if (txt_pin_entry.text.toString() == "0000") {
                            Handler().postDelayed({
                                loading.visibility = View.GONE

                                val builder = AlertDialog.Builder(this@VerificationActivity)
                                builder.setMessage("Akun Anda berhasil diverifikasi. Selamat menggunakan.")
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

                                positiveButton.setOnClickListener{
                                    startActivity(Intent(this, LoginActivity::class.java))
                                }

                            }, 1000)
                        } else {
                            Handler().postDelayed({
                                loading.visibility = View.GONE
                                tvVerificationErrorMessage.visibility = View.VISIBLE
                            }, 1000)
                        }
                    }, 1000)
                }
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
            tvVerificationErrorMessage.visibility = View.GONE
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