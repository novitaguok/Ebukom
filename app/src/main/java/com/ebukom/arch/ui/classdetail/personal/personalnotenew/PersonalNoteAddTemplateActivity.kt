package com.ebukom.arch.ui.classdetail.personal.personalnotenew

import android.app.PendingIntent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_note.*
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_note.toolbar
import kotlinx.android.synthetic.main.activity_personal_note_add_template.*

class PersonalNoteAddTemplateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_add_template)

        initToolbar()

        btnPersonalNoteAddTemplate.setOnClickListener {
            val title = etPersonalNoteAddTemplate?.text.toString()
            val content = etPersonalNoteAddTemplateContent?.text.toString()
            DataDummy.adminNoteTemplateData.add(ClassDetailTemplateTextDao(title, content))
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