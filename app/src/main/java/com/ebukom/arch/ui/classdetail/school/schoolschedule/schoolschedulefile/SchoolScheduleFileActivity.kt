package com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulefile

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        db.collection("classes").document(classId!!).addSnapshotListener { value, error ->
            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

//            val subject = value!!["schedules.subject"] as? String
//            val eskul = value["schedules.eskul"] as? String
//            val academic = value["schedules.academic"] as? String



//            when (scheduleType) {
//                "subject" -> ivSchoolScheduleFile.setImageURI(Uri.parse(subject))
//                "eskul" -> {
//                    ivSchoolScheduleFile.setImageURI(null)
//                    ivSchoolScheduleFile.setImageURI(Uri.parse(eskul))
//                }
//                "academic" -> ivSchoolScheduleFile.setImageURI(Uri.parse(academic))
//            }
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

    fun Context.drawableToUri(drawable: Int):Uri{
        return Uri.parse("android.resource://$packageName/$drawable")
    }

}