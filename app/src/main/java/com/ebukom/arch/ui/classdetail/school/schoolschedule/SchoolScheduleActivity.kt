package com.ebukom.arch.ui.classdetail.school.schoolschedule

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailScheduleDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import kotlinx.android.synthetic.main.activity_school_announcement.toolbar
import kotlinx.android.synthetic.main.activity_school_schedule.*


class SchoolScheduleActivity : AppCompatActivity() {

    private val mSchoolScheduleList: ArrayList<ClassDetailScheduleDao> = arrayListOf()
    lateinit var mSchoolScheduleAdapter: SchoolScheduleAdapter
    lateinit var callback: OnMoreCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_schedule)

        initToolbar()

        mSchoolScheduleAdapter = SchoolScheduleAdapter(mSchoolScheduleList, this)

        mSchoolScheduleList.add(ClassDetailScheduleDao("Jadwal", "Pelajaran", "", "", R.drawable.bg_schedule_purple, Color.parseColor("#5A4569")))
        mSchoolScheduleList.add(ClassDetailScheduleDao("Jadwal", "Eskul", "", "", R.drawable.bg_schedule_red, Color.parseColor("#7B3C3C")))
        mSchoolScheduleList.add(ClassDetailScheduleDao("Kalender", "Akademik", "", "", R.drawable.bg_schedule_yellow, Color.parseColor("#82802A")))

        rvSchoolSchedule.apply {
            adapter = mSchoolScheduleAdapter
            layoutManager = LinearLayoutManager(this.context)
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