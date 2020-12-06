package com.ebukom.arch.ui.classdetail.school.schoolschedule

import android.content.Intent
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
    //    private val mSchoolScheduleList: ArrayList<ClassDetailScheduleDao> = arrayListOf()
//    lateinit var mSchoolScheduleAdapter: SchoolScheduleAdapter
    lateinit var callback: OnMoreCallback
    var classId: String? = null
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_schedule)

        initToolbar()

        classId = intent?.extras?.getString("classId")

        /**
         * Go to create new schedule activity
         */
        btnSchoolScheduleNew.setOnClickListener {
            val intent = Intent(this, SchoolScheduleNewActivity::class.java)
            intent.putExtra("classId", classId)
            startActivity(intent)
        }

        db.collection("classes").document(classId!!).addSnapshotListener { value, error ->
            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            val subject = value!!["schedules.subject.photoUri"] as? String
            val eskul = value["schedules.eskul.photoUri"] as? String
            val academic = value["schedules.academic.photoUri"] as? String

            if (subject != "") cvSchoolScheduleSubject.visibility =
                View.VISIBLE
            else cvSchoolScheduleSubject.visibility = View.GONE
            if (eskul != "") cvSchoolScheduleEskul.visibility =
                View.VISIBLE
            else cvSchoolScheduleEskul.visibility = View.GONE
            if (academic != "") cvSchoolScheduleAcademic.visibility =
                View.VISIBLE
            else cvSchoolScheduleAcademic.visibility = View.GONE

            if (subject == "" && eskul == "" && academic == "") {
                ivSchoolScheduleEmpty.visibility = View.VISIBLE
                tvSchoolScheduleEmpty.visibility = View.VISIBLE
            } else {
                ivSchoolScheduleEmpty.visibility = View.GONE
                tvSchoolScheduleEmpty.visibility = View.GONE
            }
        }

        /**
         * Card clicked
         */
        cvSchoolScheduleSubject.setOnClickListener {
            val intent = Intent(this, SchoolScheduleFileActivity::class.java)
            intent.putExtra("classId", classId)
            intent.putExtra("scheduleType", "subject")
            startActivity(intent)
        }
        cvSchoolScheduleEskul.setOnClickListener {
            val intent = Intent(this, SchoolScheduleFileActivity::class.java)
            intent.putExtra("classId", classId)
            intent.putExtra("scheduleType", "eskul")
            startActivity(intent)
        }
        cvSchoolScheduleAcademic.setOnClickListener {
            val intent = Intent(this, SchoolScheduleFileActivity::class.java)
            intent.putExtra("classId", classId)
            intent.putExtra("scheduleType", "academic")
            startActivity(intent)
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