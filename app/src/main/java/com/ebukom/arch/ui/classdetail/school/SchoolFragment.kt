package com.ebukom.arch.ui.classdetail.school

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementActivity
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoActivity
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleActivity
import kotlinx.android.synthetic.main.fragment_school.view.*
import java.lang.ClassCastException

class SchoolFragment : Fragment() {

    lateinit var callback: OnMoreCallback
    var classId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_school, container, false)

        classId = arguments?.getString("classId")

        view.cvSchoolInfoAnnouncement.setOnClickListener {
            if (classId != null) {
                val intent = Intent(context, SchoolAnnouncementActivity::class.java)
                intent.putExtra("classId", classId)
                startActivity(intent)
            }
        }
        view.cvSchoolInfoSchedule.setOnClickListener {
            startActivity(Intent(context, SchoolScheduleActivity::class.java))
        }
        view.cvSchoolInfoPhoto.setOnClickListener {
            startActivity(Intent(context, SchoolPhotoActivity::class.java))
        }

        return view
    }


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
