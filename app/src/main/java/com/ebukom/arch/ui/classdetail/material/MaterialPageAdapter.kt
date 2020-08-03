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

        var fragment1 = if (sharePref.getInt("level", 0) == 1) {
            PersonalFragment()
        } else {
            MaterialSubjectFragment()
        }

        var fragment2 = if (sharePref.getInt("level", 0) == 1) {
            PersonalParentSchoolFeeInfoFragment()
        } else {
            MaterialEducationFragment()
        }

        when (position) {
            0 -> {
                return fragment1
            }
            else -> {
                return fragment2
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {

        var tab1 = ""
        var tab2 = ""

        if (sharePref.getInt("level", 0) == 1) {
            tab1 = "CATATAN PRIBADI"
            tab2 = "INFO PEMBAYARAN SPP"
        } else {
            tab1 = "MATERI BELAJAR ANAK"
            tab2 = "MATERI MENDIDIK ANAK"
        }

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