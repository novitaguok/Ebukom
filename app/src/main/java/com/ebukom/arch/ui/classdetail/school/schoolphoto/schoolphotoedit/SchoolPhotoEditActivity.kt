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
import com.ebukom.arch.dao.ClassDetailPhotoDao
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoAdapter
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_school_photo_edit.*
import kotlinx.android.synthetic.main.activity_school_photo_new.toolbar

class SchoolPhotoEditActivity : AppCompatActivity() {

    private var pos: Int = -1
    private val mPhotoList: ArrayList<ClassDetailPhotoDao> = DataDummy.photoData
    lateinit var mPhotoAdapter : SchoolPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_photo_edit)

        initToolbar()

        // Get Photo Data
        pos = intent?.extras?.getInt("pos", -1)?: -1

        etSchoolPhotoEditEventName.setText(DataDummy.photoData[pos].photoTitle)
        etSchoolPhotoEditLink.setText(DataDummy.photoData[pos].link)

        // Text watcher
        etSchoolPhotoEditEventName.addTextChangedListener(textWatcher)
        etSchoolPhotoEditLink.addTextChangedListener(textWatcher)

        btnSchoolPhotoEditSave.setOnClickListener {
            var title = etSchoolPhotoEditEventName.text.toString()
            var content = etSchoolPhotoEditLink.text.toString()

            DataDummy.photoData[pos].photoTitle = title
            DataDummy.photoData[pos].link = content

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
                    .isNotEmpty() && etSchoolPhotoEditLink.text.toString()
                    .isNotEmpty()
            ) {
                btnSchoolPhotoEditSave.isEnabled = true
                btnSchoolPhotoEditSave.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnSchoolPhotoEditSave.isEnabled = false
                btnSchoolPhotoEditSave.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }
}
