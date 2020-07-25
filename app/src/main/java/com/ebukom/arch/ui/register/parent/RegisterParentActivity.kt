package com.ebukom.arch.ui.register.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ebukom.R
import com.ebukom.arch.ui.forgotpassword.verification.VerificationActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_register_parent.*
import kotlinx.android.synthetic.main.bottom_sheet_register_parent.view.*

class RegisterParentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_parent)

        initToolbar()

        // Pop Up
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_register_parent, null)
        bottomSheetDialog.setContentView(view)

        btnRegisterParentEskul.setOnClickListener {
            bottomSheetDialog.show()
        }
        view.btnRegisterParentBottomSheetDone.setOnClickListener{
            bottomSheetDialog.dismiss()
        }
        btnRegisterParentRegister.setOnClickListener {
            val intent = Intent(this, VerificationActivity::class.java)
            intent.putExtra("Layout", 1)
            startActivity(intent)
        }
        btnRegisterParentLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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
