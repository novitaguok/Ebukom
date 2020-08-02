package com.ebukom.arch.ui.classdetail.school.schoolschedule

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
import com.ebukom.arch.dao.ClassDetailScheduleDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulenew.SchoolScheduleNewActivity
import kotlinx.android.synthetic.main.fragment_school_schedule.*
import kotlinx.android.synthetic.main.fragment_school_schedule.view.*

class SchoolScheduleFragment : Fragment() {
    var objectList = ArrayList<ClassDetailScheduleDao>()
    lateinit var schoolScheduleAdapter: SchoolScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_school_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharePref: SharedPreferences = activity!!.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getInt("level", 0) == 1){
            btnSchoolScheduleNew.visibility = View.GONE
        }

        addData()
        schoolScheduleAdapter = SchoolScheduleAdapter(objectList, callback)
        rvSchoolSchedule.layoutManager = LinearLayoutManager(this.context)
        rvSchoolSchedule.adapter = schoolScheduleAdapter

        if (!objectList.isEmpty()) {
            view.ivSchoolScheduleEmpty.visibility = View.INVISIBLE
        } else {
            view.ivSchoolScheduleEmpty.visibility = View.VISIBLE
        }

        btnSchoolScheduleNew.setOnClickListener {
            (context as MainClassDetailActivity).startActivity(Intent((context as MainClassDetailActivity), SchoolScheduleNewActivity::class.java))
        }
    }

    private fun addData() {
        objectList.add(
            ClassDetailScheduleDao(
                "Jadwal",
                "Pelajaran",
                "Lihat Jadwal",
                0
            )
        )
        objectList.add(
            ClassDetailScheduleDao(
                "Jadwal",
                "Eskul",
                "Lihat Jadwal",
                1
            )
        )
        objectList.add(
            ClassDetailScheduleDao(
                "Kalender",
                "Akademik",
                "Lihat Kalender",
                2
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
