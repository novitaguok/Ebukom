package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinforecheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminPaymentItemDao
import com.ebukom.arch.dao.AdminPaymentItemFormDao
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.ui.admin.MainAdminActivity
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.AdminShareSchoolFeeInfoAdapter
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent.ParentSchoolFeeInfoAdapter
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_personal_parent_school_fee_info.*

class AdminShareSchoolFeeInfoRecheckActivity : AppCompatActivity() {
    private val mPaymentList: ArrayList<AdminPaymentItemFormDao> = arrayListOf()
    lateinit var mPaymentAdapter: AdminShareSchoolFeeInfoAdapter
    private val mParentList: ArrayList<AdminSchoolFeeInfoSentDao> = arrayListOf()
    lateinit var mParentAdapter: ParentSchoolFeeInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_parent_school_fee_info)

        tvPersonalParentSchoolFeeInfoTitle.visibility = View.GONE
        tvPersonalParentSchoolFeeInfoDate.visibility = View.GONE
        btnPersonalParentSchoolFeeInfoDone.visibility = View.VISIBLE

        val layout = intent?.extras?.getString("role", "")
        when (layout) {
            "admin" -> {
                initToolbar()
                tvToolbarTitle.text = "Periksa Kembali Informasi"
                cvPersonalParentSchoolFeeInfoProcedure.visibility = View.GONE
                vPersonalParentSchoolFeeInfo.visibility = View.VISIBLE
                tvPersonalParentSchoolFeeInfoRecipientTitle.visibility = View.VISIBLE
                rvPersonalParentSchoolFeeInfoRecipient.visibility = View.VISIBLE
                tbPersonalParentSchoolFeeInfo.visibility = View.GONE
                tbAdminSchoolFeeInfo.visibility = View.VISIBLE
                tvPersonalParentSchoolFeeInfoNoteContent.text = DataDummy.adminNoteTemporaryData

                if (DataDummy.adminNoteTemporaryData == "") cvPersonalParentSchoolFeeInfoNote.visibility = View.GONE
            }
            else -> {
                tbPersonalParentSchoolFeeInfo.visibility = View.VISIBLE
                tbAdminSchoolFeeInfo.visibility = View.GONE
            }
        }
        initToolbar()

        val eskulData = intent?.extras?.getString("eskulData", "")
        val classData = intent?.extras?.getString("classData", "")

        // Payment Item
        mPaymentAdapter = AdminShareSchoolFeeInfoAdapter(mPaymentList)
        rvPersonalParentSchoolFeeInfo.apply {
            layoutManager = LinearLayoutManager(
                this@AdminShareSchoolFeeInfoRecheckActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mPaymentAdapter
        }
        mPaymentList.addAll(DataDummy.paymentTemporaryData)
        mPaymentAdapter.notifyDataSetChanged()

        // Recipient List
        mParentAdapter = ParentSchoolFeeInfoAdapter(mParentList, null)
        rvPersonalParentSchoolFeeInfoRecipient.apply {
            layoutManager = LinearLayoutManager(
                this@AdminShareSchoolFeeInfoRecheckActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mParentAdapter
        }
        mParentList.addAll(DataDummy.recipientTemporaryData)
        mParentAdapter.notifyDataSetChanged()

        // Share
        btnPersonalParentSchoolFeeInfoDone.setOnClickListener {

            // Payment Data
            var data = AdminPaymentItemDao(
                "Jumaidah Estetika",
                "Bobbi Andrean",
                eskulData!!,
                classData,
                "Terakhir diupdate: 20.00 - 14 Maret 2020",
                DataDummy.adminNoteTemporaryData,
                DataDummy.paymentTemporaryData.clone() as ArrayList<AdminPaymentItemFormDao>,
                DataDummy.recipientTemporaryData.clone() as ArrayList<AdminSchoolFeeInfoSentDao>
            )
            DataDummy.paymentData.add(data)

            // Clear all temporary data
            DataDummy.recipientTemporaryData.clear()
            DataDummy.adminNoteTemporaryData = ""
            DataDummy.eskulTemporaryData.clear()
            DataDummy.paymentTemporaryData.clear()

            loading.visibility = View.VISIBLE
            Handler().postDelayed({
                loading.visibility = View.GONE
                startActivity(Intent(this, MainAdminActivity::class.java))
                finish()
            }, 1000)
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