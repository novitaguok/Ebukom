package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.dao.ClassDetailSchoolAnnouncementMonthDao
import com.ebukom.arch.ui.classdetail.ClassDetailSchoolAnnouncementMonthAdapter
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew.SchoolAnnouncementNewActivity
import com.ebukom.data.DataDummy
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_announcement.*
import kotlinx.android.synthetic.main.activity_school_announcement.toolbar
import timber.log.Timber


class SchoolAnnouncementActivity : AppCompatActivity() {

    private var mAnnouncementMonthList: ArrayList<ClassDetailSchoolAnnouncementMonthDao> =
        arrayListOf()
    private var mAnnouncementList = arrayListOf<ClassDetailAnnouncementDao>()
    lateinit var mMonthAdapter: ClassDetailSchoolAnnouncementMonthAdapter
    lateinit var callback: OnMoreCallback
    lateinit var mAnnounceByDayAdapter: SchoolAnnouncementPageAdapter
    var classId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement)

        initToolbar()
        initListener()
        initRecycler()
//        loadDummy()
        loadAnnouncement()
    }

    /**
     * Load announcement data from Firestore
     */
    private fun loadAnnouncement() {
        classId = intent.getStringExtra("classId")

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
                        // jika udh ada
                        mAnnouncementList.add(
                            ClassDetailAnnouncementDao(
                                document["title"] as String,
                                document["content"] as String,
                                arrayListOf(),
                                (document["time"] as Timestamp).toString(),
                                arrayListOf(),
                                document["teacher.name"] as String,
                                document.id
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
        mAnnouncementList.sortBy {
            it.timestamp.seconds
        }
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
        var pointerMonth = 0
        val groupedList = mAnnouncementList.groupBy {
            it.timestamp.toDate().month
        }


        mAnnouncementMonthList.clear()
//        Create title
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
                    arrayOfMonth[key],
                    value,
                    0,
                    key
                )
            )
        }
        mAnnouncementMonthList.sortWith(compareBy<ClassDetailSchoolAnnouncementMonthDao> { it.monthId }.thenByDescending { it.viewType })
        rvSchoolAnnouncementMonth.adapter =
            ClassDetailSchoolAnnouncementMonthAdapter(mAnnouncementMonthList.filter {
                it.viewType == 1
            }, object : ClassDetailSchoolAnnouncementMonthAdapter.OnItemClickedListener {
                override fun onItemClicked(child: ClassDetailSchoolAnnouncementMonthDao) {
                    val pos = mAnnouncementMonthList.indexOf(child)
                    (rvSchoolAnnouncement.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        pos,
                        0
                    )
                }
            })
        rvSchoolAnnouncement.adapter?.notifyDataSetChanged()
    }


    private fun initListener() {
        /**
         * Tap on "Bagikan Pengumuman Baru" button
         */

        btnSchoolAnnouncementNew.setOnClickListener {
            startActivity(Intent(this, SchoolAnnouncementNewActivity::class.java))
        }
    }

    private fun initRecycler() {
        /**
         * Announcement(s) by month
         */
        mMonthAdapter = ClassDetailSchoolAnnouncementMonthAdapter(mAnnouncementMonthList.filter {
            it.viewType == 1
        }, object : ClassDetailSchoolAnnouncementMonthAdapter.OnItemClickedListener {
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

    /**
     * Dummy database
     */
    private fun loadDummy() {
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Januari",
                arrayListOf(),
                1
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Januari", listOf(
                    ClassDetailAnnouncementDao(
                        "Tes",
                        "Tes",
                        arrayListOf(),
                        "Tes",
                        arrayListOf(),
                        "Tes"
                    ),
                    ClassDetailAnnouncementDao(
                        "Tes",
                        "Tes",
                        arrayListOf(),
                        "Tes",
                        arrayListOf(),
                        "Tes"
                    ),
                    ClassDetailAnnouncementDao(
                        "Tes",
                        "Tes",
                        arrayListOf(),
                        "Tes",
                        arrayListOf(),
                        "Tes"
                    )
                )
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Februari",
                arrayListOf(),
                1
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Februari",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Maret", arrayListOf(), 1))
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Maret",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("April", arrayListOf(), 1))
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "April",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Mei", arrayListOf(), 1))
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Mei",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Mei",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Juni", arrayListOf(), 1))
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Juni",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Juli", arrayListOf(), 1))
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Juli",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Agustus",
                arrayListOf(),
                1
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Agustus",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "September",
                arrayListOf(),
                1
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "September",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Oktober",
                arrayListOf(),
                1
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Oktober",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "November",
                arrayListOf(),
                1
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "November",
                DataDummy.announcementData
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Desember",
                arrayListOf(),
                1
            )
        )
        mAnnouncementMonthList.add(
            ClassDetailSchoolAnnouncementMonthDao(
                "Desember",
                DataDummy.announcementData
            )
        )
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