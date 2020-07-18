package com.ebukom.arch.ui.admin.adminschoolfeeinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.personal.PersonalPageAdapter
import com.google.android.material.tabs.TabLayout

class AdminSchoolFeeInfoFragment : Fragment() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_school_fee_info, container, false)
        tabLayout = view.findViewById(R.id.mainSchoolFeeInfoTabLayout) as TabLayout
        viewPager = view.findViewById(R.id.mainSchoolFeeInfoViewPager) as ViewPager
        viewPager?.adapter = AdminSchoolFeeInfoAdapter(childFragmentManager)
        tabLayout?.setupWithViewPager(viewPager)
        viewPager?.currentItem = 0
        return view
    }
}