package com.ebukom.arch.ui.classdetail.material

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebukom.arch.ui.classdetail.material.materialeducation.MaterialEducationFragment
import com.ebukom.arch.ui.classdetail.material.materialsubject.MaterialSubjectFragment

class MaterialPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {return MaterialSubjectFragment() }
            else -> {return MaterialEducationFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {return "MATERI BELAJAR ANAK"}
            1 -> {return "MATERI MENDIDIK ANAK"}
        }
        return super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return 2
    }
}