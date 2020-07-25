package com.ebukom.arch.ui.register.school

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ebukom.R
import com.ebukom.arch.ui.forgotpassword.verification.VerificationActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_register_school.*
import kotlinx.android.synthetic.main.activity_register_school.toolbar
import kotlinx.android.synthetic.main.bottom_sheet_register_school.view.*

class RegisterSchoolActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_school)

        initToolbar()

        // Pop Up
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_register_school, null)
        bottomSheetDialog.setContentView(view)
        btnRegisterSchoolRole.setOnClickListener {
            bottomSheetDialog.show()
        }
        view.btnRegisterSchoolBottomSheetDone.setOnClickListener{
            bottomSheetDialog.dismiss()
        }
        btnRegisterSchoolRegister.setOnClickListener {
            val intent = Intent(this, VerificationActivity::class.java)
            intent.putExtra("Layout", 1)
            startActivity(intent)
        }
        btnRegisterSchoolLogin.setOnClickListener {
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
