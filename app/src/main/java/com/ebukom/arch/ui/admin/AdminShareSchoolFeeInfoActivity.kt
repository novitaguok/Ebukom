package com.ebukom.arch.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ebukom.R
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.AdminSchoolFeeInfoAddNoteActivity
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.AdminSchoolFeeInfoAddPaymentItemActivity
import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info.*

class AdminShareSchoolFeeInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_share_school_fee_info)

        initToolbar()

        btnAdminShareSchoolFeeInfoAdd.setOnClickListener {
            startActivity(Intent(this, AdminSchoolFeeInfoAddPaymentItemActivity::class.java))
        }

        btnAdminShareSchoolFeeInfoAddNote.setOnClickListener {
            startActivity(Intent(this, AdminSchoolFeeInfoAddNoteActivity::class.java))
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