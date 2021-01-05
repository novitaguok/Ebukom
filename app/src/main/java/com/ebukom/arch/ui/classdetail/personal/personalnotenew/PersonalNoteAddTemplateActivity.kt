package com.ebukom.arch.ui.classdetail.personal.personalnotenew

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
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_note.*
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_note.toolbar
import kotlinx.android.synthetic.main.activity_personal_note_add_template.*

class PersonalNoteAddTemplateActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_add_template)

        initToolbar()

        btnPersonalNoteAddTemplate.setOnClickListener {
            val data = ClassDetailTemplateTextDao(
                etPersonalNoteAddTemplateTitle?.text.toString(),
                etPersonalNoteAddTemplateContent?.text.toString()
            )

            db.collection("note_templates").add(data).addOnSuccessListener {
                Log.d("NoteAddTemplateActivity", "template added successfully")
            }.addOnFailureListener {
                Log.d("NoteAddTemplateActivity", "template added fail")
            }

            finish()
        }

        // Text Watcher
        etPersonalNoteAddTemplateTitle.addTextChangedListener(textWatcher)
        etPersonalNoteAddTemplateContent.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etPersonalNoteAddTemplateTitle.text.toString()
                    .isNotEmpty() && etPersonalNoteAddTemplateContent.text.toString()
                    .isNotEmpty()
            ) {
                btnPersonalNoteAddTemplate.isEnabled = true
                btnPersonalNoteAddTemplate.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnPersonalNoteAddTemplate.isEnabled = false
                btnPersonalNoteAddTemplate.setBackgroundColor(
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