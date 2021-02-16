package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_announcement_add_template.*

class SchoolAnnouncementAddTemplateActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_add_template)

        // Toolbar
        initToolbar()

        // Text Watcher
        etSchoolAnnouncementAddTemplateTitle.addTextChangedListener(textWatcher)
        etSchoolAnnouncementAddTemplateContent.addTextChangedListener(textWatcher)

        // Add Template to Database
        btnSchoolAnnouncementAddTemplate.setOnClickListener {
            val data = ClassDetailTemplateTextDao(
                etSchoolAnnouncementAddTemplateTitle?.text.toString(),
                etSchoolAnnouncementAddTemplateContent?.text.toString()
            )

            db.collection("note_templates").add(data).addOnSuccessListener {
                Log.d("AnnouncementAddTemplateActivity", "template added successfully")
            }.addOnFailureListener {
                Log.d("AnnouncementAddTemplateActivity", "template added fail")
            }

            finish()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etSchoolAnnouncementAddTemplateTitle.text.toString()
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
