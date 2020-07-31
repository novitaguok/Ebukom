package com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulefile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ebukom.R
import kotlinx.android.synthetic.main.activity_school_schedule_file.*

class SchoolScheduleFileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_schedule_file)

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