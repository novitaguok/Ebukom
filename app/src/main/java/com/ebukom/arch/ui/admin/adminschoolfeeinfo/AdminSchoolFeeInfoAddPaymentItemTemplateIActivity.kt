package com.ebukom.arch.ui.admin.adminschoolfeeinfo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.ebukom.R
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_payment_item.*
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_payment_item_template.*
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_payment_item_template.toolbar

class AdminSchoolFeeInfoAddPaymentItemTemplateIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_school_fee_info_add_payment_item_template)

        initToolbar()

        // Textwatcher
        etAdminSchoolFeeInfoAddPaymentItemTemplate.addTextChangedListener(textWatcher)
        etAdminSchoolFeeInfoAddPaymentItemFeeTemplate.addTextChangedListener(textWatcher)
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etAdminSchoolFeeInfoAddPaymentItemTemplate.text.toString()
                    .isNotEmpty() && etAdminSchoolFeeInfoAddPaymentItemFeeTemplate.text.toString().isNotEmpty()
            ) {
                btnAdminSchoolFeeInfoAddPaymentItemFeeTemplateDone.setEnabled(true)
                btnAdminSchoolFeeInfoAddPaymentItemFeeTemplateDone.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnAdminSchoolFeeInfoAddPaymentItemFeeTemplateDone.setEnabled(false)
                btnAdminSchoolFeeInfoAddPaymentItemFeeTemplateDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }
}