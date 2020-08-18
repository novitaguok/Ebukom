package com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotonew

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailPhotoDao
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_school_photo_new.*
import kotlinx.android.synthetic.main.activity_school_photo_new.loading
import kotlinx.android.synthetic.main.activity_school_photo_new.toolbar

class SchoolPhotoNewActivity : AppCompatActivity() {

    private val mPhotoList: ArrayList<ClassDetailPhotoDao> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_photo_new)

        initToolbar()

        // Text watcher
        etSchoolPhotoNewEventName.addTextChangedListener(textWatcher)
        etSchoolPhotoNewLink.addTextChangedListener(textWatcher)

        // Save
        btnSchoolPhotoNewDone.setOnClickListener {
            val type = if (DataDummy.photoData.size % 2 == 0) {
                0
            } else {
                1
            }

            var title = etSchoolPhotoNewEventName.text.toString()
            var content = etSchoolPhotoNewLink.text.toString()

            DataDummy.photoData.add(ClassDetailPhotoDao(title, content, type))

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
            if (etSchoolPhotoNewEventName.text.toString()
                    .isNotEmpty() && etSchoolPhotoNewLink.text.toString()
                    .isNotEmpty()
            ) {
                btnSchoolPhotoNewDone.isEnabled = true
                btnSchoolPhotoNewDone.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnSchoolPhotoNewDone.isEnabled = false
                btnSchoolPhotoNewDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }
}
