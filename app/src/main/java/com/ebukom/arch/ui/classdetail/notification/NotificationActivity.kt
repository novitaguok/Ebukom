package com.ebukom.arch.ui.classdetail.notification

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailAdapter
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.*

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val list = ArrayList<ClassDetailAnnouncementCommentDao>()

        list.add(ClassDetailAnnouncementCommentDao("Eni Trikuswanti", "Menambahkan catatan pribadi untuk Anda, silakan cek ya.", R.drawable.ic_notification))
        list.add(ClassDetailAnnouncementCommentDao("Nisa Nisa", "Informasi biaya pendidikan Anda sudah dibagikan, silakan dicek.", R.drawable.ic_notification))
        list.add(ClassDetailAnnouncementCommentDao("Cek Informasi Sekolah Ya", "Hari ini Anda belum cek info sekolah, silakan dicek ya.", R.drawable.ic_notification))

        val adapter = NotificationAdapter(list)

        rvNotification.layoutManager = LinearLayoutManager(this)
        rvNotification.adapter = adapter

        if (list.isNotEmpty()) {
            tvNotificationEmpty.visibility = View.INVISIBLE
            ivNotificationEmpty.visibility = View.INVISIBLE
        }
    }
}
