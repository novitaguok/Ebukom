package com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.AdminSchoolFeeInfoPageAdapter
import kotlinx.android.synthetic.main.fragment_admin_school_fee_info_sent.*
import kotlinx.android.synthetic.main.fragment_admin_school_fee_info_sent.view.*

class AdminSchoolFeeInfoSentFragment : Fragment() {
    var objectList = ArrayList<AdminSchoolFeeInfoSentDao>()
    lateinit var adminSchoolFeeInfoSentAdapter: AdminSchoolFeeInfoSentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_school_fee_info_sent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        adminSchoolFeeInfoSentAdapter =
            AdminSchoolFeeInfoSentAdapter(
                objectList, null
            )
        rvSchoolFeeInfoSent.layoutManager = LinearLayoutManager(this.context)
        rvSchoolFeeInfoSent.adapter = adminSchoolFeeInfoSentAdapter

        if (objectList.isNotEmpty()) {
            view.ivSchoolFeeInfoSentEmpty.visibility = View.INVISIBLE
            view.tvSchoolFeeInfoSentEmpty.visibility = View.INVISIBLE
        } else {
            view.ivSchoolFeeInfoSentEmpty.visibility = View.VISIBLE
            view.tvSchoolFeeInfoSentEmpty.visibility = View.VISIBLE
        }
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