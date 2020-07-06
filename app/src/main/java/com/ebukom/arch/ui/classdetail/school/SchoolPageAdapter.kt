package com.ebukom.arch.ui.classdetail.school

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementFragment
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoFragment
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleFragment

class SchoolPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {return SchoolAnnouncementFragment()}
            1 -> {return SchoolScheduleFragment()}
            2 -> {return SchoolPhotoFragment()}
            else -> {return SchoolAnnouncementFragment()}
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {return "PENGUMUMAN"}
            1 -> {return "JADWAL"}
            2 -> {return "FOTO"}
        }
        return super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return 3
    }
}