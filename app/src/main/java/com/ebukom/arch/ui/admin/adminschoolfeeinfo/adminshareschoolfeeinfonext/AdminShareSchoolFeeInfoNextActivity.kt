package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfonext

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinforecheck.AdminShareSchoolFeeInfoRecheckActivity
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent.AdminSchoolFeeInfoSentAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info_next.*

class AdminShareSchoolFeeInfoNextActivity : AppCompatActivity(),
    AdminSchoolFeeInfoSentAdapter.onCheckListener {

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
                objectList, this@AdminShareSchoolFeeInfoNextActivity
            )
        rvAdminSchoolFeeInfoNext.layoutManager = LinearLayoutManager(this)
        rvAdminSchoolFeeInfoNext.adapter = adminSchoolFeeInfoSentAdapter

        // Get intent from PersonalParentSchoolFeeInfoActivity
        val layout = intent?.extras?.getString("layout", null)
        when (layout) {
            "edit" -> btnAdminSchoolFeeInfoNextDone.text = "SIMPAN PERUBAHAN"
        }

        // Done
        btnAdminSchoolFeeInfoNextDone.setOnClickListener {
            startActivity(Intent(this, AdminShareSchoolFeeInfoRecheckActivity::class.java))
        }

        // Check all
        cbAdminSchoolFeeInfoNextAllParent.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                objectList.forEach {
                    it.isChecked = true
                }
                adminSchoolFeeInfoSentAdapter.notifyDataSetChanged()
            } else {
                objectList.forEach {
                    it.isChecked = false
                }
                adminSchoolFeeInfoSentAdapter.notifyDataSetChanged()
            }
        }

        // Share
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

    override fun onCheckChange() {
        var isCheckedItem = false
        objectList.forEach {
            if (it.isChecked) isCheckedItem = true
        }

        if (isCheckedItem) {
            btnAdminSchoolFeeInfoNextDone.isEnabled = true
            btnAdminSchoolFeeInfoNextDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        } else {
            btnAdminSchoolFeeInfoNextDone.isEnabled = false
            btnAdminSchoolFeeInfoNextDone.setBackgroundColor(
                Color.parseColor("#BDBDBD")
            )
        }
    }
}