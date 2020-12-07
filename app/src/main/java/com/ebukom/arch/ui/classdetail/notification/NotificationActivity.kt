package com.ebukom.arch.ui.classdetail.notification

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {

    private val mNotificationList = ArrayList<ClassDetailAnnouncementCommentDao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        initToolbar()

        mNotificationList.add(ClassDetailAnnouncementCommentDao("Eni Trikuswanti", "Menambahkan catatan pribadi untuk Anda, silakan cek ya.", R.drawable.ic_notification))
        mNotificationList.add(ClassDetailAnnouncementCommentDao("Nisa Nisa", "Informasi biaya pendidikan Anda sudah dibagikan, silakan dicek.", R.drawable.ic_notification))
        mNotificationList.add(ClassDetailAnnouncementCommentDao("Cek Informasi Sekolah Ya", "Hari ini Anda belum cek info sekolah, silakan dicek ya.", R.drawable.ic_notification))

        toolbarNotificationClear.setOnClickListener {  }

        val adapter = NotificationAdapter(mNotificationList)
        rvNotification.layoutManager = LinearLayoutManager(this)
        rvNotification.adapter = adapter

        if (mNotificationList.isNotEmpty()) {
            tvNotificationEmpty.visibility = View.INVISIBLE
            ivNotificationEmpty.visibility = View.INVISIBLE
        }
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
