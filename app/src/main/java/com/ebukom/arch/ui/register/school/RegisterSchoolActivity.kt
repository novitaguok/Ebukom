package com.ebukom.arch.ui.register.school

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ebukom.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_register_school.*
import kotlinx.android.synthetic.main.activity_register_school.toolbar

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
