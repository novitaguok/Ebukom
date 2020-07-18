package com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfoparentlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebukom.R

class AdminSchoolFeeInfoParentListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_admin_school_fee_info_parent_list,
            container,
            false
        )
    }

}