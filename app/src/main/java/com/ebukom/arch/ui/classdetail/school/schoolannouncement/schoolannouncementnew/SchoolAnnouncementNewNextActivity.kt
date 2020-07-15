package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailItemCheckDao
import com.ebukom.arch.ui.classdetail.ClassDetailCheckAdapter
import kotlinx.android.synthetic.main.activity_personal_note_new_next.*
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.*

class SchoolAnnouncementNewNextActivity : AppCompatActivity() {

    val list = ArrayList<ClassDetailItemCheckDao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_new_next)

        list.add(ClassDetailItemCheckDao("Semua Kelas"))
        list.add(ClassDetailItemCheckDao("Kelas 1 Aurora"))
        list.add(ClassDetailItemCheckDao("Kelas 1 Astronot"))
        list.add(ClassDetailItemCheckDao("Kelas 2 Aurora"))
        list.add(ClassDetailItemCheckDao("Kelas 3 Astronot"))

        rvSchoolAnnouncementNewNext.apply {
            layoutManager = LinearLayoutManager(
                this@SchoolAnnouncementNewNextActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter =
                ClassDetailCheckAdapter(
                    list
                )
        }
    }

}
