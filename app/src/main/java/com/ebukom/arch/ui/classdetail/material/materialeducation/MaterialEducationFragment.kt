package com.ebukom.arch.ui.classdetail.material.materialeducation

import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.fragment_material_education.*
import java.lang.ClassCastException

class MaterialEducationFragment : Fragment() {
    var objectList = ArrayList<ClassDetailAnnouncementDao>()
    lateinit var schoolAnnouncementAdapter: SchoolAnnouncementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material_education, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        schoolAnnouncementAdapter = SchoolAnnouncementAdapter(objectList, callback)
        rvMaterialEducation.layoutManager = LinearLayoutManager(this.context)
        rvMaterialEducation.adapter = schoolAnnouncementAdapter

        btnMaterialEducationNew.setOnClickListener {
            (context as MainClassDetailActivity).startActivity(Intent((context as MainClassDetailActivity), MaterialEducationNewActivity::class.java))
        }
    }

    private fun addData() {
        for (i in 0..10) {

            objectList.add(
                ClassDetailAnnouncementDao(
                    "Mendidik Anak Hyperaktif",
                    "Untuk mendidk anak yang hyperaktif, diperlukan suatu kemampuan aitu kesabaran yang luar biasa. Selain itu perlu diketahui juga cara yang...",
                    "Eni Trikuswanti",
                    "02/02/02"
                )
            )
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
    }
}