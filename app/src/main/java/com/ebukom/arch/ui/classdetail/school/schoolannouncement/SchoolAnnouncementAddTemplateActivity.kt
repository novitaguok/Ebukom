package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_school_announcement_add_template.*

class SchoolAnnouncementAddTemplateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_add_template)

        // Toolbar
        initToolbar()
        var layout = intent.extras?.getString("layout", "announcement")
        when(layout) {
            "note" -> tvToolbarTitle.text = "Tambah Template Catatan"
        }

        // Text Watcher
        etSchoolAnnouncementAddTemplate.addTextChangedListener(textWatcher)
        etSchoolAnnouncementAddTemplateContent.addTextChangedListener(textWatcher)

        // Add Template to Database
        btnSchoolAnnouncementAddTemplate.setOnClickListener {
            val title = etSchoolAnnouncementAddTemplate?.text.toString()
            val content = etSchoolAnnouncementAddTemplateContent?.text.toString()
            DataDummy.textTemplateData.add(ClassDetailTemplateTextDao(title, content))

            finish()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etSchoolAnnouncementAddTemplate.text.toString()
                    .isNotEmpty() && etSchoolAnnouncementAddTemplateContent.text.toString()
                    .isNotEmpty()
            ) {
                btnSchoolAnnouncementAddTemplate.isEnabled = true
                btnSchoolAnnouncementAddTemplate.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnSchoolAnnouncementAddTemplate.isEnabled = false
                btnSchoolAnnouncementAddTemplate.setBackgroundColor(
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
