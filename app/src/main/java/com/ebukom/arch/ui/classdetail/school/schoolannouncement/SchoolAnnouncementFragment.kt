package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew.SchoolAnnouncementNewActivity
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_school_announcement_edit.*
import kotlinx.android.synthetic.main.fragment_school_announcement.*
import java.lang.ClassCastException

class SchoolAnnouncementFragment : androidx.fragment.app.Fragment() {

    private val mAnnouncementList: ArrayList<ClassDetailAnnouncementDao> = arrayListOf()
    lateinit var mAnnouncementAdapter: SchoolAnnouncementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_school_announcement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharePref: SharedPreferences =
            activity!!.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if (sharePref.getInt("level", 0) == 1) {
            btnSchoolAnnouncementNew.visibility = View.GONE
        }

        // Announcement List
        mAnnouncementAdapter = SchoolAnnouncementAdapter(mAnnouncementList, callback)
        rvSchoolAnnouncement.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mAnnouncementAdapter
        }
        mAnnouncementList.addAll(DataDummy.announcementData)
        mAnnouncementAdapter.notifyDataSetChanged()

        btnSchoolAnnouncementNew.setOnClickListener {
            (context as MainClassDetailActivity).startActivity(
                Intent(
                    (context as MainClassDetailActivity),
                    SchoolAnnouncementNewActivity::class.java
                )
            )
        }
    }

    lateinit var callback: OnMoreCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as OnMoreCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString()
                        + " must implement MyInterface "
            );
        }
    }

    override fun onResume() {
        super.onResume()
        MainClassDetailActivity.isAnnouncement = true

        // Announcement List
        mAnnouncementList.clear()
        mAnnouncementList.addAll(DataDummy.announcementData)
        mAnnouncementAdapter.notifyDataSetChanged()
    }
}
