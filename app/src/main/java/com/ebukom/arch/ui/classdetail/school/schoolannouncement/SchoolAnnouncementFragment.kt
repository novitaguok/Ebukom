package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.chooseclass.ChooseClassAdapter
import com.ebukom.arch.ui.classdetail.school.SchoolPageAdapter
import kotlinx.android.synthetic.main.fragment_school_announcement.*

class SchoolAnnouncementFragment : Fragment() {

    var objectList = ArrayList<ClassDetailAnnouncementDao>()
    val schoolAnnouncementAdapter = SchoolAnnouncementAdapter(objectList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_school_announcement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        schoolAnnouncementAdapter.announcements = objectList
        rvSchoolAnnouncement.layoutManager = LinearLayoutManager(this.context)
        rvSchoolAnnouncement.adapter = schoolAnnouncementAdapter
    }

    private fun addData() {
        for (i in 0..10) {
            objectList.add(
                ClassDetailAnnouncementDao(
                    "Title",
                    "This is the content",
                    "9 KOMENTAR",
                    "02/02/02"
                )
            )
        }
    }
}
