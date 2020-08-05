package com.ebukom.arch.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminPaymentItemFormDao
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent.AdminSchoolFeeInfoSentAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info_next.*

class AdminShareSchoolFeeInfoNextActivity : AppCompatActivity() {

    var objectList = ArrayList<AdminSchoolFeeInfoSentDao>()
    lateinit var adminSchoolFeeInfoSentAdapter: AdminSchoolFeeInfoSentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_share_school_fee_info_next)

        initToolbar()

        btnAdminSchoolFeeInfoNextClass.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_class_detail_header, null)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }
        btnAdminSchoolFeeInfoNextEskul.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_register_parent, null)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        addData()
        adminSchoolFeeInfoSentAdapter =
            AdminSchoolFeeInfoSentAdapter(
                objectList
            )
        rvAdminSchoolFeeInfoNext.layoutManager = LinearLayoutManager(this)
        rvAdminSchoolFeeInfoNext.adapter = adminSchoolFeeInfoSentAdapter
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun addData() {
        for (i in 0..10) {
            objectList.add(
                AdminSchoolFeeInfoSentDao(
                    "Jumaidah Estetika",
                    "Bobbi Andrean • Pramuka, Basket • Kelas IA Aurora",
                    "Terakhir diupdate: 20.00 - 14 Maret 2020"
                )
            )
        }
    }
}