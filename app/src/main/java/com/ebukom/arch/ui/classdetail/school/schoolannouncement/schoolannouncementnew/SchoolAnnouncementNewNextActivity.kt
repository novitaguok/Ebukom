package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailItemCheckDao
import com.ebukom.arch.ui.classdetail.ClassDetailCheckAdapter
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.*
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.loading
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.toolbar
import kotlinx.android.synthetic.main.item_check.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SchoolAnnouncementNewNextActivity : AppCompatActivity(), ClassDetailCheckAdapter.OnCheckListener {

    val list = ArrayList<ClassDetailItemCheckDao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_new_next)

        initToolbar()

        list.add(ClassDetailItemCheckDao("Semua Kelas"))
        list.add(ClassDetailItemCheckDao("Kelas 1 Aurora"))
        list.add(ClassDetailItemCheckDao("Kelas 1 Astronot"))
        list.add(ClassDetailItemCheckDao("Kelas 2 Aurora"))
        list.add(ClassDetailItemCheckDao("Kelas 3 Astronot"))

        rvSchoolAnnouncementNewNext.apply {
            layoutManager = LinearLayoutManager(
                this@SchoolAnnouncementNewNextActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter =
                ClassDetailCheckAdapter(
                    list,
                    this@SchoolAnnouncementNewNextActivity
                )
        }

        // Share Announcement Button
        btnSchoolAnnouncementNewNextDone.setOnClickListener {
            loading.visibility = View.VISIBLE
            Handler().postDelayed({
                loading.visibility = View.GONE
                startActivity(Intent(this, MainClassDetailActivity::class.java))
                finish()
            }, 1000)
        }

        //
        sSchoolAnnouncementNewNextAlarm.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                val dateTimeDialogFragmentActivity = SwitchDateTimeDialogFragment(
//                    "Atur Waktu Notifikasi Berulang",
//                    "SELESAI",
//                    "BATALKAN"
//                )
//
//                dateTimeDialogFragmentActivity.startAtCalendarView()
//                dateTimeDialogFragmentActivity.set24HoursMode(true)
//                dateTimeDialogFragmentActivity.minimumDateTime(GregorianCalendar(2020, Calendar.JANUARY, 1).time)
//
//                try {
//                    dateTimeDialogFragmentActivity.simpleDateMonthAndDayFormat(SimpleDateFormat("dd MMMM", Locale.getDefault()))
//                } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
//                    Log.e(TAG, e.getMessage())
//                }
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

    override fun onCheckChange() {
        var isCheckedItem = false
        list.forEach {
            if(it.isChecked) isCheckedItem = true
        }

        if(isCheckedItem){
            btnSchoolAnnouncementNewNextDone.setEnabled(true)
            btnSchoolAnnouncementNewNextDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        } else {
            btnSchoolAnnouncementNewNextDone.setEnabled(false)
            btnSchoolAnnouncementNewNextDone.setBackgroundColor(
                Color.parseColor("#828282")
            )
        }
    }
}
