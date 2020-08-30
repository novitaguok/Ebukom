package com.ebukom.arch.ui.classdetail.material.materialeducation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.material.materialeducation.materialeducationnew.MaterialEducationNewActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAdapter
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.fragment_material_education.*
import java.lang.ClassCastException

class MaterialEducationFragment : Fragment() {
    private val mPersonalEducationList: ArrayList<ClassDetailAnnouncementDao> = arrayListOf()
    lateinit var mPersonalEducationAdapter: SchoolAnnouncementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material_education, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Shared Preference
        val sharePref: SharedPreferences = activity!!.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getInt("level", 0) == 1){
            btnMaterialEducationNew.visibility = View.GONE
        }

        mPersonalEducationAdapter = SchoolAnnouncementAdapter(mPersonalEducationList, callback, this)
        mPersonalEducationList.clear()
        mPersonalEducationList.addAll(DataDummy.educationData)
        mPersonalEducationAdapter.notifyDataSetChanged()

        // Education List
        rvMaterialEducation.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mPersonalEducationAdapter
        }
        mPersonalEducationList.addAll(DataDummy.educationData)
        mPersonalEducationAdapter.notifyDataSetChanged()

        btnMaterialEducationNew.setOnClickListener {
            (context as MainClassDetailActivity).startActivity(Intent((context as MainClassDetailActivity), MaterialEducationNewActivity::class.java))
        }

        checkEducationEmpty()
    }

    private fun checkEducationEmpty() {
        if (mPersonalEducationList.isEmpty()) {
            ivMaterialEducationEmpty.visibility = View.VISIBLE
            tvMaterialEducationEmpty.visibility = View.VISIBLE
        } else {
            ivMaterialEducationEmpty.visibility = View.GONE
            tvMaterialEducationEmpty.visibility = View.GONE
        }
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

    override fun onResume() {
        super.onResume()
        MainClassDetailActivity.isAnnouncement = false

        mPersonalEducationList.clear()
        mPersonalEducationList.addAll(DataDummy.educationData)
        mPersonalEducationAdapter.notifyDataSetChanged()

        checkEducationEmpty()
    }
}