package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfoaddpaymentitem

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminPaymentItemFormDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_payment_item.*
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_payment_item.toolbar

class AdminSchoolFeeInfoAddPaymentItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_school_fee_info_add_payment_item)

        initToolbar()

        // Template Item
        val templateText: MutableList<ClassDetailTemplateTextDao> = ArrayList()
//        templateText.add(ClassDetailTemplateTextDao("Pengembangan I/II"))
//        templateText.add(ClassDetailTemplateTextDao("Kegiatan s.d. 2019-2020"))
//        for (i: Int in 1..10) templateText.add(ClassDetailTemplateTextDao("Test tes"))
        rvAdminSchoolFeeInfoAddItemNewTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@AdminSchoolFeeInfoAddPaymentItemActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter =
                ClassDetailTemplateTextAdapter(
                    templateText
                )
        }

        // Recycler View of Form
        val form: MutableList<AdminPaymentItemFormDao> = ArrayList()
        btnAdminSchoolFeeInfoAddItemForm.setOnClickListener {
            vAdminSchoolFeeInfoAddItem.visibility = View.VISIBLE
            tvAdminSchoolFeeInfoAddItemDeleteForm.visibility = View.VISIBLE

            form.add(AdminPaymentItemFormDao("", ""))
            rvAdminSchoolFeeInfoAddItemForm?.adapter?.notifyDataSetChanged()
        }
        rvAdminSchoolFeeInfoAddItemForm.apply {
            layoutManager =
                LinearLayoutManager(
                    this@AdminSchoolFeeInfoAddPaymentItemActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter =
                AdminSchoolFeeInfoAddPaymentItemAdapter(
                    form
                )
        }

        // Delete form
        tvAdminSchoolFeeInfoAddItemDeleteForm.setOnClickListener {
            val builder = AlertDialog.Builder(this@AdminSchoolFeeInfoAddPaymentItemActivity)

            builder.setMessage("Apakah Anda yakin ingin menghapus form ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )

            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorRed
                )
            )
        }

        // Textwatcher
        etAdminSchoolFeeInfoAddItem.addTextChangedListener(textWatcher)
        etAdminSchoolFeeInfoAddItemFee.addTextChangedListener(textWatcher)

        // Add template
        tvAdminSchoolFeeInfoAddItemNewTemplateAdd.setOnClickListener {
            startActivity(Intent())
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etAdminSchoolFeeInfoAddItem.text.toString()
                    .isNotEmpty() && etAdminSchoolFeeInfoAddItemFee.text.toString().isNotEmpty()
            ) {
                btnAdminSchoolFeeInfoAddItemDone.setEnabled(true)
                btnAdminSchoolFeeInfoAddItemDone.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnAdminSchoolFeeInfoAddItemDone.setEnabled(false)
                btnAdminSchoolFeeInfoAddItemDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
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