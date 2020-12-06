package com.ebukom.arch.ui.classdetail.personal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebukom.arch.ui.classdetail.personal.personalacceptednote.PersonalAcceptedNoteFragment
import com.ebukom.arch.ui.classdetail.personal.personalsentnote.PersonalSentNoteFragment
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementFragment
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleFragment

class PersonalPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {return PersonalAcceptedNoteFragment()}
            else -> {return PersonalSentNoteFragment()}
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {return "CATATAN DITERIMA"}
            1 -> {return "CATATAN DIKIRIM"}
        }
        return super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return 2
    }
}