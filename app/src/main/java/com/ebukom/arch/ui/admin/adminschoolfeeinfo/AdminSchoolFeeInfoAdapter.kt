package com.ebukom.arch.ui.admin.adminschoolfeeinfo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfoparentlist.AdminSchoolFeeInfoParentListFragment
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent.AdminSchoolFeeInfoSentFragment
import com.ebukom.arch.ui.classdetail.personal.personalacceptednote.PersonalAcceptedNoteFragment
import com.ebukom.arch.ui.classdetail.personal.personalsentnote.PersonalSentNoteFragment
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementFragment
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoFragment
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleFragment

class AdminSchoolFeeInfoAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {return AdminSchoolFeeInfoParentListFragment()}
            else -> {return AdminSchoolFeeInfoSentFragment()}
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {return "DAFTAR WALI MURID"}
            1 -> {return "INFO TERKIRIM"}
        }
        return super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return 2
    }
}