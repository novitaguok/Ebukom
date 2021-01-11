package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnewnext

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailItemCheckDao
import com.ebukom.arch.ui.classdetail.ClassDetailCheckAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementmainpage.SchoolAnnouncementActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew.SchoolAnnouncementNewActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.*
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.loading
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.toolbar
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SchoolAnnouncementNewNextActivity : AppCompatActivity(),
    ClassDetailCheckAdapter.OnCheckListener {

    private val mClassList: ArrayList<ClassDetailItemCheckDao> = arrayListOf()
    lateinit var mClassAdapter: ClassDetailCheckAdapter
    lateinit var title: String
    lateinit var content: String
    var dateTime: String? = null
    lateinit var eventStart: Timestamp
    lateinit var eventEnd: Timestamp
    lateinit var attachmentList: List<ClassDetailAttachmentDao>
    lateinit var sharePref: SharedPreferences
    val db = FirebaseFirestore.getInstance()
    var classId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_new_next)

        initToolbar()

        sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

        // Intent from SchoolAnnouncementNewActivity
        classId = intent.getStringExtra("classId")
        title = intent?.extras?.getString("title", "") ?: ""
        content = intent?.extras?.getString("content", "") ?: ""
        eventStart = intent?.getParcelableExtra("eventStart") as Timestamp
        eventEnd = intent?.getParcelableExtra("eventEnd") as Timestamp
        attachmentList =
            intent?.getSerializableExtra("attachments") as List<ClassDetailAttachmentDao>
        dateTime = ""

        // Class list
        initRecycler()
        val uid = sharePref.getString("uid", "") as String
        db.collection("classes").whereArrayContains("class_teacher_ids", uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Timber.e(error)
                    return@addSnapshotListener
                }

                mClassList.clear()
                for (document in value!!.documents) {
                    val classGrade = document["class_grade"] as String
                    val className = document["class_name"] as String
                    val item = classGrade + " " + className

                    mClassList.add(
                        ClassDetailItemCheckDao(item, id = document.id)
                    )
                    rvSchoolAnnouncementNewNext.adapter?.notifyDataSetChanged()
                }
            }

        mClassAdapter = ClassDetailCheckAdapter(mClassList, this)
        cbSchoolAnnouncementNewNextClassAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) mClassList.forEach { it.isChecked = true }
            else mClassList.forEach { it.isChecked = false }
            rvSchoolAnnouncementNewNext.adapter?.notifyDataSetChanged()
        }

        // Datetime picker
        sSchoolAnnouncementNewNextAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) dateTime = ""
            else {
                val dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                    "Diingatkan kembali pada",
                    "SELESAI",
                    "BATALKAN"
                )
                val dateFormat = SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault())

                dateTimeDialogFragment.setTimeZone(TimeZone.getDefault())
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

        /**
         * Share announcement button
         */
        btnSchoolAnnouncementNewNextDone.setOnClickListener {

            val sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
            val uid = sharePref.getString("uid", "") as String
            val teacherName = sharePref.getString("name", "") as String
            val data = hashMapOf(
                "content" to content,
                "teacher" to mapOf<String, Any>(
                    "name" to teacherName,
                    "id" to uid
                ),
                "time" to Timestamp(Date()),
                "title" to title,
                "attachments" to attachmentList,
                "event_start" to eventStart,
                "event_end" to eventEnd
            )

            loading.visibility = View.VISIBLE
            mClassList.forEach {
                if (it.isChecked && !it.id.isNullOrEmpty()) {
                    db.collection("classes").document(it.id!!).collection("announcements")
                        .add(data).addOnCompleteListener {
                            if (it.isSuccessful) {
                                loading.visibility = View.GONE
                                val intent = Intent("finish_activity")
                                sendBroadcast(intent)
                                finish()
                            } else {
                                Log.d("TAG", "announcement inserted")
                                loading.visibility = View.GONE
                                val intent = Intent("finish_activity")
                                sendBroadcast(intent)
                                finish()
                            }
                        }
                }
            }
        }
    }

    private fun initRecycler() {
        /**
         * Load classes joined data
         */
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
