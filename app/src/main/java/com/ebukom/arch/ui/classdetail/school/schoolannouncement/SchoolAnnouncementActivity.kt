package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.content.Context
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
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_school_announcement.*


class SchoolAnnouncementActivity : AppCompatActivity() {

    private val mAnnouncementMonthList: ArrayList<ClassDetailSchoolAnnouncementMonthDao> = arrayListOf()
    lateinit var mMonthAdapter : ClassDetailSchoolAnnouncementMonthAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var callback: OnMoreCallback
    lateinit var mAnnounceByDayAdapter : SchoolAnnouncementPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement)

        initToolbar()

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

        // Smooth Scroller
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

        // Announcement by month
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

        // Announcement by day
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

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }



}