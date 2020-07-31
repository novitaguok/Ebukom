package com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotoedit

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.ebukom.R
import kotlinx.android.synthetic.main.activity_school_photo_edit.*
import kotlinx.android.synthetic.main.activity_school_photo_new.*
import kotlinx.android.synthetic.main.activity_school_photo_new.loading
import kotlinx.android.synthetic.main.activity_school_photo_new.toolbar

class SchoolPhotoEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_photo_edit)

        initToolbar()

        // Text watcher
        etSchoolPhotoEditEventName.addTextChangedListener(textWatcher)
        etSchoolPhotoEditLink.addTextChangedListener(textWatcher)

        btnSchoolPhotoEditSave.setOnClickListener {
            loading.visibility = View.VISIBLE
            Handler().postDelayed({
                loading.visibility = View.GONE
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
            if (etSchoolPhotoEditEventName.text.toString()
                    .isNotEmpty() && etSchoolPhotoNewLink.text.toString()
                    .isNotEmpty()
            ) {
                btnSchoolPhotoEditSave.setEnabled(true)
                btnSchoolPhotoEditSave.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnSchoolPhotoEditSave.setEnabled(false)
                btnSchoolPhotoEditSave.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }
}
