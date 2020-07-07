package com.ebukom.arch.ui.classdetail.school

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ebukom.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_school.*

class SchoolFragment : Fragment() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_school, container, false)
        tabLayout = view.findViewById(R.id.mainClassTabLayout) as TabLayout
        viewPager = view.findViewById(R.id.mainClassViewPager) as ViewPager
        viewPager!!.setAdapter(fragmentManager?.let { SchoolPageAdapter(it) })
        tabLayout!!.setupWithViewPager(viewPager)
//        tabLayout!!.post(Runnable {  })
        return view
    }
}

//////////////////////////////////////////
//override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.SchoolFragment)
//
//    viewPager.adapter = PagerAdapter(supportFragmentManager)
//    mainClassTabLayout.setupWithViewPager(viewPager)
//}
////////////////////////////////////////////////
