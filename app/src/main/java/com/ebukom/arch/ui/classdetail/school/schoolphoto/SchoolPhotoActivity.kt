package com.ebukom.arch.ui.classdetail.school.schoolphoto

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailSchoolInfoDao
import com.ebukom.arch.ui.classdetail.school.SchoolFragmentAdapter
import kotlinx.android.synthetic.main.activity_school_announcement.*
import kotlinx.android.synthetic.main.fragment_school.*

class SchoolPhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_school_photo)

            initToolbar()
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