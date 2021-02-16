package com.ebukom.arch.ui.classdetail.personal.personalparent.personalparentschoolfeeinfo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPersonalParentSchoolFeeDao
import com.ebukom.arch.dao.ClassDetailPhotoDao
import com.ebukom.arch.dao.ClassDetailScheduleDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoAdapter
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleAdapter
import kotlinx.android.synthetic.main.fragment_personal_parent_school_fee_info.*
import kotlinx.android.synthetic.main.fragment_personal_parent_school_fee_info.view.*
import kotlinx.android.synthetic.main.fragment_school_photo.*
import kotlinx.android.synthetic.main.fragment_school_photo.view.*
import kotlinx.android.synthetic.main.fragment_school_schedule.*
import kotlinx.android.synthetic.main.fragment_school_schedule.view.*

class PersonalParentSchoolFeeInfoFragment : Fragment() {
    private val mSchoolFeeList: ArrayList<ClassDetailPersonalParentSchoolFeeDao> = arrayListOf()
    private lateinit var mSchoolFeeAdapter: PersonalParentSchoolFeeInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_parent_school_fee_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        mSchoolFeeAdapter = PersonalParentSchoolFeeInfoAdapter(mSchoolFeeList, callback, activity!!)
        rvPersonalParentSchoolFeeInfo.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mSchoolFeeAdapter
        }

        checkSchoolFeeEmpty(view)
    }

    private fun checkSchoolFeeEmpty(view: View) {
        if (!mSchoolFeeList.isEmpty()) {
            view.ivPersonalParentSchoolFeeInfoEmpty.visibility = View.INVISIBLE
            view.tvPersonalParentSchoolFeeInfoEmpty.visibility = View.INVISIBLE
        } else {
            view.ivPersonalParentSchoolFeeInfoEmpty.visibility = View.VISIBLE
            view.tvPersonalParentSchoolFeeInfoEmpty.visibility = View.VISIBLE
        }
    }

    private fun addData() {
        mSchoolFeeList.add(
            ClassDetailPersonalParentSchoolFeeDao(
                "Info Pembayaran SPP",
                "28 Maret 2020"
            )
        )
        mSchoolFeeList.add(
            ClassDetailPersonalParentSchoolFeeDao(
                "Info Pembayaran SPP",
                "18 Februari 2020"
            )
        )
    }

    lateinit var callback: OnMoreCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as OnMoreCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString()
                        + " must implement MyInterface "
            );
        }
    }
}