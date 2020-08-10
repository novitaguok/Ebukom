package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinforecheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ebukom.R
import com.ebukom.arch.ui.admin.MainAdminActivity
import kotlinx.android.synthetic.main.activity_personal_parent_school_fee_info.*

class AdminShareSchoolFeeInfoRecheckActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_parent_school_fee_info)

        initToolbar()

        tvPersonalParentSchoolFeeInfoTitle.visibility = View.GONE
        tvPersonalParentSchoolFeeInfoDate.visibility = View.GONE
        btnPersonalParentSchoolFeeInfoDone.visibility = View.VISIBLE

        btnPersonalParentSchoolFeeInfoDone.setOnClickListener {
            startActivity(Intent(this, MainAdminActivity::class.java))
            finish()
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