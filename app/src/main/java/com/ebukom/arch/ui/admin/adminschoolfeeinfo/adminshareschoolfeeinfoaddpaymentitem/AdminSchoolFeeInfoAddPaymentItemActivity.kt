package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfoaddpaymentitem

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminPaymentItemFormDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAddTemplateActivity
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_payment_item.*

class AdminSchoolFeeInfoAddPaymentItemActivity : AppCompatActivity() {

    private val mTemplateList: ArrayList<ClassDetailTemplateTextDao> = arrayListOf()
    private val mTemplateAdapter = ClassDetailTemplateTextAdapter(mTemplateList)
    private val mFormList: ArrayList<AdminPaymentItemFormDao> = arrayListOf()
    private val mFormAdapter =
        AdminSchoolFeeInfoAddPaymentItemAdapter(mFormList, object : OnPaymentItemCallback {
            override fun onItemChange() {
                var isFilled = true
                mFormList.forEach {
                    if (it.itemName.isEmpty() || it.itemFee.isEmpty()) isFilled = false
                }
                btnAdminSchoolFeeInfoAddItemDone.isEnabled = isFilled
                if (isFilled) {
                    btnAdminSchoolFeeInfoAddItemDone.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorSuperDarkBlue
                        )
                    )
                } else {
                    btnAdminSchoolFeeInfoAddItemDone.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorGray
                        )
                    )
                }
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_school_fee_info_add_payment_item)

        initToolbar()

        // Template List
        rvAdminSchoolFeeInfoAddItemNewTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@AdminSchoolFeeInfoAddPaymentItemActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = mTemplateAdapter
        }

        // Form List
        btnAdminSchoolFeeInfoAddItemForm.setOnClickListener {
            tvAdminSchoolFeeInfoAddItemDeleteForm.visibility = View.VISIBLE
            mFormList.add(AdminPaymentItemFormDao("", ""))
            rvAdminSchoolFeeInfoAddItemForm?.adapter?.notifyDataSetChanged()
        }

        mFormList.add(AdminPaymentItemFormDao("", ""))
        rvAdminSchoolFeeInfoAddItemForm.apply {
            layoutManager =
                LinearLayoutManager(
                    this@AdminSchoolFeeInfoAddPaymentItemActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mFormAdapter
        }

        checkFormSize()

        // Delete form
        tvAdminSchoolFeeInfoAddItemDeleteForm.setOnClickListener {
            val builder = AlertDialog.Builder(this@AdminSchoolFeeInfoAddPaymentItemActivity)

            builder.setMessage("Apakah Anda yakin ingin menghapus form ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                dialog.dismiss()
                mFormList.removeAt(mFormList.size - 1)
                mFormAdapter.notifyDataSetChanged()
                checkFormSize()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                Color.parseColor("#BDBDBD")
            )

            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorRed
                )
            )
        }

        // Add template
        tvAdminSchoolFeeInfoAddItemNewTemplateAdd.setOnClickListener {
            val intent = Intent(this, SchoolAnnouncementAddTemplateActivity::class.java)
            intent.putExtra("layout", "payment")
            startActivity(intent)
        }

        // Done
        btnAdminSchoolFeeInfoAddItemDone.setOnClickListener {
            mFormList.forEach {
                DataDummy.paymentTemporaryData.add(AdminPaymentItemFormDao(it.itemName, it.itemFee))
            }
            mFormAdapter.notifyDataSetChanged()
            finish()
        }
    }

    private fun checkFormSize() {
        if (mFormList.size == 1) tvAdminSchoolFeeInfoAddItemDeleteForm.visibility = View.GONE
        else tvAdminSchoolFeeInfoAddItemDeleteForm.visibility = View.VISIBLE
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