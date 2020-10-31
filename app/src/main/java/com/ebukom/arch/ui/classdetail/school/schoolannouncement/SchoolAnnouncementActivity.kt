package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.dao.ClassDetailSchoolAnnouncementMonthDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailSchoolAnnouncementMonthAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_payment_item.*
import kotlinx.android.synthetic.main.activity_school_announcement.*
import kotlinx.android.synthetic.main.activity_school_announcement.toolbar
import kotlinx.android.synthetic.main.item_announcement_by_date.view.*

class SchoolAnnouncementActivity : AppCompatActivity() {

    private val mAnnouncementMonthList: ArrayList<ClassDetailSchoolAnnouncementMonthDao> = arrayListOf()
    lateinit var mMonthAdapter : ClassDetailSchoolAnnouncementMonthAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var callback: OnMoreCallback
    lateinit var mAnnounceByDayAdapter : SchoolAnnouncementListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement)

        initToolbar()

        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Januari", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Januari", listOf(ClassDetailAnnouncementDao("Tes", "Tes", arrayListOf(), "Tes", arrayListOf(), "Tes"))))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Januari", listOf(ClassDetailAnnouncementDao("Tes", "Tes", arrayListOf(), "Tes", arrayListOf(), "Tes"))))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Januari", listOf(ClassDetailAnnouncementDao("Tes", "Tes", arrayListOf(), "Tes", arrayListOf(), "Tes"))))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Januari", listOf(ClassDetailAnnouncementDao("Tes", "Tes", arrayListOf(), "Tes", arrayListOf(), "Tes"))))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Februari", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Februari", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Maret", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Maret", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("April", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("April", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Mei", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Mei", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Mei", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Juni", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Juni", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Juli", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Juli", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Agustus", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Agustus", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("September", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("September", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Oktober", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Oktober", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("November", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("November", DataDummy.announcementData))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Desember", arrayListOf(),1))
        mAnnouncementMonthList.add(ClassDetailSchoolAnnouncementMonthDao("Desember", DataDummy.announcementData))

        // Announcement by month
        mMonthAdapter = ClassDetailSchoolAnnouncementMonthAdapter(mAnnouncementMonthList.filter {
            it.viewType == 1
        }, object : ClassDetailSchoolAnnouncementMonthAdapter.OnItemClickedListener{
            override fun onItemClicked(child : ClassDetailSchoolAnnouncementMonthDao) {
                val pos = mAnnouncementMonthList.indexOf(child)
                rvSchoolAnnouncement.scrollToPosition(pos)
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
        mAnnounceByDayAdapter = SchoolAnnouncementListAdapter((mAnnouncementMonthList))
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