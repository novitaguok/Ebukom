package com.ebukom.arch.ui.classdetail.material

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebukom.arch.ui.classdetail.material.materialeducation.MaterialEducationFragment
import com.ebukom.arch.ui.classdetail.material.materialsubject.MaterialSubjectFragment
import com.ebukom.arch.ui.classdetail.personal.PersonalFragment
import com.ebukom.arch.ui.classdetail.personal.personalparent.personalparentschoolfeeinfo.PersonalParentSchoolFeeInfoFragment

class MaterialPageAdapter(fm: FragmentManager, val context: Context) : FragmentPagerAdapter(fm) {

    val sharePref: SharedPreferences = context.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MaterialSubjectFragment()
            }
            else -> {
                MaterialEducationFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {

        var tab1 = "MATERI BELAJAR ANAK"
        var tab2 = "MATERI MENDIDIK ANAK"

        when (position) {
            0 -> {
                return tab1
            }
            1 -> {
                return tab2
            }
        }
        return super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return 2
    }
}