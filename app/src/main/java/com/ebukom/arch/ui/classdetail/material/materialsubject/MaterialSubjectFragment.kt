package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.material.MaterialPageAdapter
import com.ebukom.arch.ui.classdetail.school.SchoolPageAdapter
import com.google.android.material.tabs.TabLayout

class MaterialSubjectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_material_subject, container, false)
    }

}