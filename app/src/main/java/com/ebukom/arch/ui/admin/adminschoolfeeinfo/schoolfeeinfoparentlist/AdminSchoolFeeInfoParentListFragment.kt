package com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfoparentlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent.ParentSchoolFeeInfoAdapter
import kotlinx.android.synthetic.main.fragment_admin_school_fee_info_parent_list.*

class AdminSchoolFeeInfoParentListFragment : Fragment() {

    var objectList = ArrayList<AdminSchoolFeeInfoSentDao>()
    lateinit var adminSchoolFeeInfoSentAdapter: ParentSchoolFeeInfoAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        adminSchoolFeeInfoSentAdapter =
            ParentSchoolFeeInfoAdapter(
                objectList, null
            )
        rvSchoolFeeInfoParentList.layoutManager = LinearLayoutManager(this.context)
        rvSchoolFeeInfoParentList.adapter = adminSchoolFeeInfoSentAdapter
    }

    private fun addData() {
        for (i in 0..10) {
            objectList.add(
                AdminSchoolFeeInfoSentDao(
                    "Jumaidah Estetika",
                    "Bobbi Andrean • Pramuka, Basket • Kelas IA Aurora",
                    "Terakhir diupdate: 20.00 - 14 Maret 2020"
                )
            )
        }
    }
}