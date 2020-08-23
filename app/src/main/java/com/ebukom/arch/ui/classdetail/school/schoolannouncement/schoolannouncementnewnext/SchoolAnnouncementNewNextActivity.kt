package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnewnext

import android.content.Intent
import android.database.DatabaseUtils
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailItemCheckDao
import com.ebukom.arch.ui.classdetail.ClassDetailCheckAdapter
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.data.DataDummy
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.*
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.loading
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.toolbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SchoolAnnouncementNewNextActivity : AppCompatActivity(),
    ClassDetailCheckAdapter.OnCheckListener {
    private val mClassList: ArrayList<ClassDetailItemCheckDao> = arrayListOf()
    lateinit var title: String
    lateinit var content: String
    lateinit var dateTime: String
    lateinit var attachments: List<ClassDetailAttachmentDao>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_new_next)

        initToolbar()

        // Class List
        DataDummy.chooseClassDataMain.forEach {
            mClassList.add(ClassDetailItemCheckDao(it.classNumber + " " + it.className))
        }
        rvSchoolAnnouncementNewNext.apply {
            layoutManager = LinearLayoutManager(
                this@SchoolAnnouncementNewNextActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter =
                ClassDetailCheckAdapter(
                    mClassList,
                    this@SchoolAnnouncementNewNextActivity
                )
        }

        // Intent from SchoolAnnouncementNewActivity
        title = intent?.extras?.getString("title", "") ?: ""
        content = intent?.extras?.getString("content", "") ?: ""
        attachments = intent?.getSerializableExtra("attachments") as List<ClassDetailAttachmentDao>
        dateTime = ""

        // DateTime Picker
        sSchoolAnnouncementNewNextAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) dateTime = ""
            else {
                val dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                    "Atur Waktu Notifikasi Berulang",
                    "SELESAI",
                    "BATALKAN"
                )

                dateTimeDialogFragment.setTimeZone(TimeZone.getDefault())

                val dateFormat = SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault())

                dateTimeDialogFragment.startAtCalendarView()
                dateTimeDialogFragment.set24HoursMode(true)
                dateTimeDialogFragment.minimumDateTime =
                    GregorianCalendar(2020, Calendar.JANUARY, 1).time

                try {
                    dateTimeDialogFragment.simpleDateMonthAndDayFormat =
                        SimpleDateFormat("dd MMMM", Locale.getDefault())
                } catch (e: SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException) {
                    Log.e("error", e.message)
                }


                dateTimeDialogFragment.setOnButtonClickListener(object :
                    SwitchDateTimeDialogFragment.OnButtonClickListener {
                    override fun onPositiveButtonClick(date: Date?) {
                        tvSchoolAnnouncementNewNextAlarmContent.text = dateFormat.format(date)
                        tvSchoolAnnouncementNewNextAlarmContent.setTextColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.colorRed
                            )
                        )
                        dateTime = dateFormat.format(date)
                    }

                    override fun onNegativeButtonClick(date: Date?) {}
                })

                dateTimeDialogFragment.show(supportFragmentManager, "")
            }
        }

        // Share Announcement Button
        btnSchoolAnnouncementNewNextDone.setOnClickListener {
            DataDummy.announcementData.add(
                ClassDetailAnnouncementDao(
                    title,
                    content,
                    arrayListOf(),
                    dateTime,
                    attachments
                )
            )
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

    override fun onCheckChange() {
        var isCheckedItem = false
        mClassList.forEach {
            if (it.isChecked) isCheckedItem = true
        }

        if (isCheckedItem) {
            btnSchoolAnnouncementNewNextDone.isEnabled = true
            btnSchoolAnnouncementNewNextDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        } else {
            btnSchoolAnnouncementNewNextDone.isEnabled = false
            btnSchoolAnnouncementNewNextDone.setBackgroundColor(
                Color.parseColor("#828282")
            )
        }
    }
}
