package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfoaddnote

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfo.AdminShareSchoolFeeInfoActivity
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.ebukom.arch.ui.classdetail.personal.personalnotenew.PersonalNoteAddTemplateActivity
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_note.*
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_note.toolbar
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.*

class AdminSchoolFeeInfoAddNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_school_fee_info_add_note)

        initToolbar()

        // Template Note
        val templateText: MutableList<ClassDetailTemplateTextDao> = ArrayList()
//        templateText.add(ClassDetailTemplateTextDao("Catatan 1"))
//        templateText.add(ClassDetailTemplateTextDao("Catatan Field Trip"))
//        for (i: Int in 1..10) templateText.add(ClassDetailTemplateTextDao("Test tes"))
        rvAdminSchoolFeeInfoAddNoteTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@AdminSchoolFeeInfoAddNoteActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter =
                ClassDetailTemplateTextAdapter(
                    templateText
                )
        }

        // Add note template
        tvAdminSchoolFeeInfoAddNoteAddTemplate.setOnClickListener {
            startActivity(Intent(this, PersonalNoteAddTemplateActivity::class.java))
        }

        // Textwatcher
        etAdminSchoolFeeInfoAddNote.addTextChangedListener(textWatcher)

        // Done
        btnAdminSchoolFeeInfoAddNoteDone.setOnClickListener {
            loading.visibility = View.VISIBLE
            Handler().postDelayed({
                loading.visibility = View.GONE
                startActivity(Intent(this, AdminShareSchoolFeeInfoActivity::class.java))
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

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etAdminSchoolFeeInfoAddNote.text.toString().isNotEmpty()) {
                btnAdminSchoolFeeInfoAddNoteDone.setEnabled(true)
                btnAdminSchoolFeeInfoAddNoteDone.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnAdminSchoolFeeInfoAddNoteDone.setEnabled(false)
                btnAdminSchoolFeeInfoAddNoteDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }
}