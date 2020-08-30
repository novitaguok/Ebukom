package com.ebukom.arch.ui.classdetail.personal.personalparent.personalparentnote

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebukom.arch.ui.classdetail.material.materialeducation.MaterialEducationFragment
import com.ebukom.arch.ui.classdetail.material.materialsubject.MaterialSubjectFragment
import com.ebukom.arch.ui.classdetail.personal.personalparent.personalparentnote.PersonalParentNoteFragment
import com.ebukom.arch.ui.classdetail.personal.personalparent.personalparentschoolfeeinfo.PersonalParentSchoolFeeInfoFragment

class PersonalParentNotePageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {return PersonalParentNoteFragment() }
            else -> {return PersonalParentSchoolFeeInfoFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {return "CATATAN PRIBADI"}
            1 -> {return "INFO PEMBAYARAN SPP"}
        }
        return super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return 2
    }
}