package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.app.Fragment
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.chooseclass.ChooseClassAdapter
import com.ebukom.arch.ui.classdetail.school.SchoolPageAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.fragment_school_announcement.*
import kotlinx.android.synthetic.main.item_announcement.*

class SchoolAnnouncementFragment : androidx.fragment.app.Fragment() {

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
