package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementEditDao
import kotlinx.android.synthetic.main.activity_school_announcement_edit.*

class SchoolAnnouncementEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_edit)

        val data : MutableList<ClassDetailAnnouncementEditDao> = ArrayList()

        data.add(ClassDetailAnnouncementEditDao("Field Trip"))
        for (i: Int in 1..10) data.add(ClassDetailAnnouncementEditDao("Perubahan Seragam"))

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val adapter = SchoolAnnouncementEditAdapter(data)

        rvSchoolAnnouncementEditTemplate.layoutManager = layoutManager
        rvSchoolAnnouncementEditTemplate.adapter = adapter
    }
}
