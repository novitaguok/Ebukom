package com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulefile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ebukom.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_schedule_file.*
import timber.log.Timber

class SchoolScheduleFileActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    var classId: String? = null
    var scheduleType: String? = null
//    private var imageUri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_schedule_file)

        initToolbar()

        classId = intent?.extras?.getString("classId")
        scheduleType = intent?.extras?.getString("scheduleType")

        ivSchoolScheduleFile.visibility = View.GONE

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