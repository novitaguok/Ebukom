package com.ebukom.arch.ui.classdetail.school.schoolschedule

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulefile.SchoolScheduleFileActivity
import com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulenew.SchoolScheduleNewActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_announcement.toolbar
import kotlinx.android.synthetic.main.activity_school_schedule.*
import timber.log.Timber

class SchoolScheduleActivity : AppCompatActivity() {
    lateinit var callback: OnMoreCallback
    var classId: String? = null
    var academic: String = ""
    var eskul: String = ""
    var calendar: String = ""
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_schedule)

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        val level = sharePref.getLong("level", 0)

        initToolbar()
        classId = intent?.extras?.getString("classId")

        // New schedule activity button
        if (level == 1L) {
            btnSchoolScheduleNew.visibility = View.GONE
        }
        btnSchoolScheduleNew.setOnClickListener {
            val intent = Intent(this, SchoolScheduleNewActivity::class.java)
            intent.putExtra("classId", classId)
            startActivity(intent)
        }

        // Load data
        db.collection("classes").document(classId!!).addSnapshotListener { value, error ->
            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            if (value?.get("schedules_academic") as String? != "" || value?.get("schedules_eskul") as String? != "" || value?.get(
                    "schedules_calendar"
                ) as String? != "" || value?.get("schedules_academic") as String? != null || value?.get(
                    "schedules_eskul"
                ) as String? != null || value?.get("schedules_calendar") as String? != null
            ) {
                if ((value?.get("schedules_academic") as String?).isNullOrEmpty()) cvSchoolScheduleAcademic.visibility =
                    View.GONE
                else cvSchoolScheduleAcademic.visibility = View.VISIBLE
                if ((value?.get("schedules_eskul") as String?).isNullOrEmpty()) ivSchoolScheduleEskul.visibility =
                    View.GONE
                else ivSchoolScheduleEskul.visibility = View.VISIBLE
                if ((value?.get("schedules_calendar") as String?).isNullOrEmpty()) cvSchoolScheduleCalendar.visibility =
                    View.GONE
                else cvSchoolScheduleCalendar.visibility = View.VISIBLE
//                if (academic == "" && eskul == "" && calendar == "") {
//                    ivSchoolScheduleEmpty.visibility = View.VISIBLE
//                    tvSchoolScheduleEmpty.visibility = View.VISIBLE
//                }
            } else {
                ivSchoolScheduleEmpty.visibility = View.VISIBLE
                tvSchoolScheduleEmpty.visibility = View.VISIBLE
                cvSchoolScheduleAcademic.visibility = View.GONE
                cvSchoolScheduleEskul.visibility = View.GONE
                cvSchoolScheduleCalendar.visibility = View.GONE
            }

            // Card clicked
            db.collection("classes").document(classId!!).get().addOnSuccessListener { data ->
                cvSchoolScheduleAcademic.setOnClickListener {
                    if ((data["schedules_academic"] as String) != "" || (data["schedules_academic"] as String) != null) {
                        val webpage = Uri.parse(data["schedules_academic"] as String)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, SchoolScheduleFileActivity::class.java)
                        startActivity(intent)
                    }
                }
                cvSchoolScheduleEskul.setOnClickListener {
                    if ((data["schedules_eskul"] as String) != "" || (data["schedules_eskul"] as String) != null) {
                        val webpage = Uri.parse(data["schedules_eskul"] as String)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, SchoolScheduleFileActivity::class.java)
                        startActivity(intent)
                    }
                }
                cvSchoolScheduleCalendar.setOnClickListener {
                    if ((data["schedules_calendar"] as String) != "" || (data["schedules_calendar"] as String) != null) {
                        val webpage = Uri.parse(data["schedules_calendar"] as String)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, SchoolScheduleFileActivity::class.java)
                        startActivity(intent)
                    }
                }
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