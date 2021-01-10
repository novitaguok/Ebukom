package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementmainpage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.dao.ClassDetailSchoolAnnouncementMonthDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew.SchoolAnnouncementNewActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_announcement.*
import timber.log.Timber


class SchoolAnnouncementActivity : AppCompatActivity() {

    private var mAnnouncementMonthList: ArrayList<ClassDetailSchoolAnnouncementMonthDao> =
        arrayListOf()
    private var mAnnouncementList = arrayListOf<ClassDetailAnnouncementDao>()
    lateinit var mMonthAdapter: SchoolAnnouncementMonthAdapter
    lateinit var callback: OnMoreCallback
    lateinit var mAnnounceByDayAdapter: SchoolAnnouncementPageAdapter
    var classId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement)

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getLong("level", 0) == 1L) {
            btnSchoolAnnouncementNew.visibility = View.GONE
        }
        classId = intent.extras?.getString("classId")

        initToolbar()
        initListener()
        initRecycler()
        loadAnnouncement()

    }

    /**
     * Load announcement data from Firestore
     */
    private fun loadAnnouncement() {
        if (classId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("classes").document(classId!!).collection("announcements")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    mAnnouncementList.clear()
                    for (document in value!!.documents) {
                        mAnnouncementList.add(
                            ClassDetailAnnouncementDao(
                                document["title"] as String,
                                document["content"] as String,
                                arrayListOf(),
                                (document["time"] as Timestamp).toDate().toString(),
                                arrayListOf(),
                                document["teacher.name"] as String,
                                document.id,
                                (document["time"] as Timestamp),
                                classId!!,
                                document["event_start"] as Timestamp,
                                document["event_end"] as Timestamp
                            )
                        )
                    }

                    filterData()
                }
        }
    }

    /**
     * Filter data by month
     */
    private fun filterData() {

        val arrayOfMonth = listOf<String>(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
        )
        val arrayOfDay = listOf<String>(
            "Minggu",
            "Senin",
            "Selasa",
            "Rabu",
            "Kamis",
            "Jumat",
            "Sabtu"
        )

        mAnnouncementList.sortBy {
            it.eventStart.seconds
        }

        val groupedList = mAnnouncementList.groupBy {
            Triple(
                it.eventStart.toDate().day,
                it.eventStart.toDate().date,
                it.eventStart.toDate().month
            )
        }

        mAnnouncementMonthList.clear()
        var pointerMonth = 0
        arrayOfMonth.forEach {
            mAnnouncementMonthList.add(
                ClassDetailSchoolAnnouncementMonthDao(
                    it,
                    arrayListOf(),
                    1,
                    pointerMonth++
                )
            )
        }

        /**
         * Create data
         */
        for ((key, value) in groupedList) {
            mAnnouncementMonthList.add(
                ClassDetailSchoolAnnouncementMonthDao(
                    key.third.toString(),
                    value,
                    0,
                    key.third,
                    date = key.second,
                    day = key.first,
                    dayName = arrayOfDay[key.first]
                )
            )
        }
        mAnnouncementMonthList.sortWith(compareBy<ClassDetailSchoolAnnouncementMonthDao> { it.monthId }.thenByDescending { it.viewType })
        rvSchoolAnnouncementMonth.adapter =
            SchoolAnnouncementMonthAdapter(mAnnouncementMonthList.filter {
                it.viewType == 1
            }, object : SchoolAnnouncementMonthAdapter.OnItemClickedListener {
                override fun onItemClicked(child: ClassDetailSchoolAnnouncementMonthDao) {
                    val pos = mAnnouncementMonthList.indexOf(child)
                    (rvSchoolAnnouncement.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        pos,
                        0
                    )

                    mAnnouncementMonthList.forEach {
                        it.isSelected = false
                    }
                    mAnnouncementMonthList[pos].isSelected = true

                    rvSchoolAnnouncementMonth.adapter?.notifyDataSetChanged()
                }
            })
        rvSchoolAnnouncement.adapter?.notifyDataSetChanged()
    }


    private fun initListener() {
        /**
         * Tap on "Bagikan Pengumuman Baru" button
         */

        btnSchoolAnnouncementNew.setOnClickListener {
            val intent = Intent(this, SchoolAnnouncementNewActivity::class.java)
            intent.putExtra("classId", classId)
            startActivity(intent)
        }
    }

    private fun initRecycler() {
        /**
         * Announcement(s) by month
         */
        mMonthAdapter = SchoolAnnouncementMonthAdapter(mAnnouncementMonthList.filter {
            it.viewType == 1
        }, object : SchoolAnnouncementMonthAdapter.OnItemClickedListener {
            override fun onItemClicked(child: ClassDetailSchoolAnnouncementMonthDao) {
                val pos = mAnnouncementMonthList.indexOf(child)
                (rvSchoolAnnouncement.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    pos,
                    0
                )
            }
        })
        rvSchoolAnnouncementMonth.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SchoolAnnouncementActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = mMonthAdapter
        }

        /**
         * Announcement by day
         */
        mAnnounceByDayAdapter = SchoolAnnouncementPageAdapter((mAnnouncementMonthList))
        rvSchoolAnnouncement.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SchoolAnnouncementActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mAnnounceByDayAdapter
        }
    }

    /**
     * Smooth scroller
     */
    class LinearLayoutManagerWithSmoothScroller : LinearLayoutManager {
        constructor(context: Context?) : super(context, VERTICAL, false) {}
        constructor(
            context: Context?,
            orientation: Int,
            reverseLayout: Boolean
        ) : super(context, orientation, reverseLayout) {
        }

        override fun smoothScrollToPosition(
            recyclerView: RecyclerView, state: RecyclerView.State,
            position: Int
        ) {
            val smoothScroller: SmoothScroller = TopSnappedSmoothScroller(recyclerView.context)
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
        }

        private inner class TopSnappedSmoothScroller(context: Context?) :
            LinearSmoothScroller(context) {
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return this@LinearLayoutManagerWithSmoothScroller
                    .computeScrollVectorForPosition(targetPosition)
            }

            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
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