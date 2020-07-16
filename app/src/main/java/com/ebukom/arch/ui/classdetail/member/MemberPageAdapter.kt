package com.ebukom.arch.ui.classdetail.member

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebukom.arch.ui.classdetail.member.memberparent.MemberParentFragment
import com.ebukom.arch.ui.classdetail.member.memberschool.MemberSchoolFragment
import com.ebukom.arch.ui.classdetail.personal.personalacceptednote.PersonalAcceptedNoteFragment
import com.ebukom.arch.ui.classdetail.personal.personalsentnote.PersonalSentNoteFragment
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementFragment
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoFragment
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleFragment

class MemberPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {return MemberParentFragment()}
            else -> {return MemberSchoolFragment()}
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {return "WALI MURID"}
            1 -> {return "PIHAK SEKOLAH"}
        }
        return super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return 2
    }
}