package com.ebukom.arch.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminPaymentItemFormDao
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.AdminSchoolFeeInfoAddNoteActivity
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.AdminSchoolFeeInfoAddPaymentItemActivity
import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info.*
import kotlinx.android.synthetic.main.alert_edit_payment.view.*
import kotlinx.android.synthetic.main.item_admin_payment_detail_color.*
import kotlinx.android.synthetic.main.item_admin_payment_detail_white.*

class AdminShareSchoolFeeInfoActivity : AppCompatActivity() {
    var objectList = ArrayList<AdminPaymentItemFormDao>()
    lateinit var adminShareSchoolFeeInfoAdapter: AdminShareSchoolFeeInfoAdapter

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

        addData()
        adminShareSchoolFeeInfoAdapter = AdminShareSchoolFeeInfoAdapter(objectList)
        rvAdminShareSchoolFeeInfo.layoutManager = LinearLayoutManager(this)
        rvAdminShareSchoolFeeInfo.adapter = adminShareSchoolFeeInfoAdapter

        if (objectList.isNotEmpty()) {
            ivAdminShareSchoolFeeInfoEmpty.visibility = View.GONE
            tvAdminShareSchoolFeeInfoEmpty.visibility = View.GONE
            tbtnAdminShareSchoolFeeInfoDetailEdit.visibility = View.VISIBLE
            btnAdminShareSchoolFeeInfoNext.isEnabled = true
            btnAdminShareSchoolFeeInfoNext.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        }

        // Toggle button
        tbtnAdminShareSchoolFeeInfoDetailEdit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ivItemAdminPaymentDetailDelete.visibility = View.VISIBLE
                ivItemAdminPaymentDetailWhiteDelete.visibility = View.VISIBLE
            } else {
                ivItemAdminPaymentDetailDelete.visibility = View.GONE
                ivItemAdminPaymentDetailWhiteDelete.visibility = View.GONE
            }
        }

//        // Table row clicked
//        trItemAdminPaymentDetailWhite.setOnClickListener {
//            val builder = AlertDialog.Builder(this@AdminShareSchoolFeeInfoActivity)
//            val view = layoutInflater.inflate(R.layout.alert_edit_text, null)
//
//            builder.setView(view)
//
//            builder.setPositiveButton("SIMPAN", null)
//            builder.setNegativeButton("BATALKAN") { dialog, which ->
//                dialog.dismiss()
//            }
//
//            val dialog: AlertDialog = builder.create()
//            dialog.show()
//
//            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//            positiveButton.setOnClickListener {
//                if (view.etAlertEditPaymentName.text.toString().isEmpty() && view.etAlertEditPaymentFee.text.toString().isEmpty()) {
//                    view.tvAlertEditPaymentNameErrorMessage.visibility = View.VISIBLE
//                    view.tvAlertEditPaymentFeeErrorMessage.visibility = View.VISIBLE
//                } else if (view.etAlertEditPaymentName.text.toString().isEmpty()) {
//                    view.tvAlertEditPaymentNameErrorMessage.visibility = View.VISIBLE
//                } else if (view.etAlertEditPaymentFee.text.toString().isEmpty()) {
//                    view.tvAlertEditPaymentFeeErrorMessage.visibility = View.VISIBLE
//                }
//            }
//            positiveButton.setTextColor(
//                ContextCompat.getColor(
//                    applicationContext,
//                    R.color.colorSuperDarkBlue
//                )
//            )
//
//            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//            negativeButton.setTextColor(
//                ContextCompat.getColor(
//                    applicationContext,
//                    R.color.colorRed
//                )
//            )
//        }

        // Next activity
        btnAdminShareSchoolFeeInfoNext.setOnClickListener {
            startActivity(Intent(this, AdminShareSchoolFeeInfoNextActivity::class.java))
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

    private fun addData() {
        for (i in 0..15) {
            objectList.add(
                AdminPaymentItemFormDao(
                    "Pengembangan I/II",
                    "880.000"
                )
            )
        }
    }
}

