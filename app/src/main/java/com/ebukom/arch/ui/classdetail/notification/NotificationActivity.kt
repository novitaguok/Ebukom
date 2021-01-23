package com.ebukom.arch.ui.classdetail.notification

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_notification.*
import timber.log.Timber

class NotificationActivity : AppCompatActivity() {

    private val mNotificationList = ArrayList<ClassDetailAnnouncementCommentDao>()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        initToolbar()

        db.collection("notifications").addSnapshotListener { value, error ->
            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            for (data in value!!.documents) {

            }
        }

        mNotificationList.add(
            ClassDetailAnnouncementCommentDao(
                "Eni Trikuswanti",
                "Menambahkan catatan pribadi untuk Anda, silakan cek ya.",
                R.drawable.ic_notification
            )
        )
        mNotificationList.add(
            ClassDetailAnnouncementCommentDao(
                "Nisa Nisa",
                "Informasi biaya pendidikan Anda sudah dibagikan, silakan dicek.",
                R.drawable.ic_notification
            )
        )
        mNotificationList.add(
            ClassDetailAnnouncementCommentDao(
                "Cek Informasi Sekolah Ya",
                "Hari ini Anda belum cek info sekolah, silakan dicek ya.",
                R.drawable.ic_notification
            )
        )

        toolbarNotificationClear.setOnClickListener {
            val collection =
                db.collection("notifications")
            deleteCollection(collection, 5) {}
        }

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

    fun deleteCollection(collection: CollectionReference, batchSize: Int, nextAction: () -> Unit) {
        try {
            // Retrieve a small batch of documents to avoid out-of-memory errors/
            var deleted = 0
            collection
                .limit(batchSize.toLong())
                .get()
                .addOnCompleteListener {
                    for (document in it.result!!.documents) {
                        document.getReference().delete()
                        ++deleted
                    }
                    if (deleted >= batchSize) {
                        // retrieve and delete another batch
                        deleteCollection(collection, batchSize, nextAction)
                    } else {
                        nextAction()
                    }
                }
        } catch (e: Exception) {
            System.err.println("Error deleting collection : " + e.message)
        }

    }
}
