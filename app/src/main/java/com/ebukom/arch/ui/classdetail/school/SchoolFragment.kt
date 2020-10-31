package com.ebukom.arch.ui.classdetail.school

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailSchoolInfoDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_school.view.*
import java.lang.ClassCastException

class SchoolFragment : Fragment() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    lateinit var callback : OnMoreCallback
    private val mSchoolInfoList: ArrayList<ClassDetailSchoolInfoDao> = arrayListOf()
    lateinit var mSchoolInfoAdapter: SchoolFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_school, container, false)
//        tabLayout = view.findViewById(R.id.mainClassSchoolTabLayout) as TabLayout
//        viewPager = view.findViewById(R.id.mainClassSchoolViewPager) as ViewPager
//        viewPager?.adapter = SchoolFragmentAdapter(childFragmentManager)
//        tabLayout?.setupWithViewPager(viewPager)
//        viewPager?.currentItem = 0
//        return view

        mSchoolInfoAdapter = SchoolFragmentAdapter(mSchoolInfoList, activity!!)

        mSchoolInfoList.add(ClassDetailSchoolInfoDao("Pengumuman\nSekolah", R.drawable.bg_school_announcement, Color.parseColor("#005C39"), "pengumuman"))
        mSchoolInfoList.add(ClassDetailSchoolInfoDao("Jadwal", R.drawable.bg_school_schedule, Color.parseColor("#004A61"), "jadwal"))
        mSchoolInfoList.add(ClassDetailSchoolInfoDao("Foto\nKegiatan", R.drawable.bg_school_photo, Color.parseColor("#693535"), "foto"))

        view.rvMainClassSchoolInfo.apply {
            adapter = mSchoolInfoAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as OnMoreCallback
        } catch (e : ClassCastException){
            throw ClassCastException(activity.toString()
                    + " must implement MyInterface ");
        }
    }

//    override fun onResume() {
//        super.onResume()
//
//        mAnnouncementAdapter = SchoolAnnouncementAdapter(mAnnouncementList, parentFragment?)
//
//        mAnnouncementList.clear()
//        mAnnouncementList.addAll(DataDummy.announcementData)
//        mAnnouncementAdapter.notifyDataSetChanged()
//    }
}
