package com.ebukom.arch.ui.forgotpassword.sendcode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.ebukom.R
import com.ebukom.arch.ui.forgotpassword.verification.VerificationActivity
import kotlinx.android.synthetic.main.activity_send_code.*
import kotlinx.android.synthetic.main.activity_send_code.toolbar

class SendCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_code)

        initToolbar()

        // Intent to Join Class
        btnSendCodeReset.setOnClickListener {
            loading.visibility = View.VISIBLE
            if (etSendCodePhone.text.toString() == "000" || etSendCodePhone.text.toString() == "123" || etSendCodePhone.text.toString() == "456") {
                Handler().postDelayed({
                    loading.visibility = View.GONE
                    startActivity(Intent(this, VerificationActivity::class.java))
                    finish()
                }, 1000)
            } else {
                Handler().postDelayed({
                    loading.visibility = View.GONE
                    tvSendCodeErrorMessage.visibility = View.VISIBLE
                }, 1000)
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
