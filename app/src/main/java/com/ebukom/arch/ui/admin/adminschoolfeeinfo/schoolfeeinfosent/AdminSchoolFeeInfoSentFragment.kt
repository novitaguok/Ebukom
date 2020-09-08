package com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminPaymentItemDao
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.fragment_admin_school_fee_info_sent.*
import kotlinx.android.synthetic.main.fragment_admin_school_fee_info_sent.view.*

class AdminSchoolFeeInfoSentFragment : Fragment() {
    private val mInfoSentList: ArrayList<AdminPaymentItemDao> = arrayListOf()
    lateinit var mInfoSentAdapter: AdminSchoolFeeInfoSentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_school_fee_info_sent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mInfoSentAdapter = AdminSchoolFeeInfoSentAdapter(mInfoSentList, null)
        rvSchoolFeeInfoSent.apply {
            layoutManager = LinearLayoutManager(
                this.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mInfoSentAdapter
        }
        mInfoSentList.addAll(DataDummy.paymentData)
        mInfoSentAdapter.notifyDataSetChanged()

        checkInfoEmpty(view)
    }

    private fun checkInfoEmpty(view: View) {
        if (mInfoSentList.isNotEmpty()) {
            view.ivSchoolFeeInfoSentEmpty.visibility = View.INVISIBLE
            view.tvSchoolFeeInfoSentEmpty.visibility = View.INVISIBLE
        } else {
            view.ivSchoolFeeInfoSentEmpty.visibility = View.VISIBLE
            view.tvSchoolFeeInfoSentEmpty.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

        mInfoSentList.clear()
        mInfoSentList.addAll(DataDummy.paymentData)
        mInfoSentAdapter.notifyDataSetChanged()

        checkInfoEmpty(view!!)
    }
}