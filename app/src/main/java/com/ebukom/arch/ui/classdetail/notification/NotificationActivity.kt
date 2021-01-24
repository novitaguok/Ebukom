package com.ebukom.arch.ui.classdetail.notification

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailCommentDao
import com.ebukom.arch.dao.ClassDetailNotificationDao
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_notification.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class NotificationActivity : AppCompatActivity() {

    private val mNotificationList = ArrayList<ClassDetailNotificationDao>()
    lateinit var mNotificationdapter: NotificationAdapter
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        initToolbar()
        initRecycler()
        checkEmpty()

        db.collection("notifications").addSnapshotListener { value, error ->
            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            for (data in value!!.documents) {
                val sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
                val uid = sharePref.getString("uid", "") as String
                if ((data["to"] as List<String>).contains(uid)) {
//                if (data["from"] as String == uid) {
                    if ((data["type"] as Long).toInt() == 0) { // announcement
                        mNotificationList.add(
                            ClassDetailNotificationDao(
                                data["title"] as String,
                                "Ada pengumuman sekolah baru,\ncek ya supaya tidak terlewat",
                                "",
                                Timestamp(Date()),
                                data.id,
                                0,
                                data["contentId"] as String,
                                data["classId"] as String
                            )
                        )

                    } else if ((data["type"] as Long).toInt() == 1) { // note
                        mNotificationList.add(
                            ClassDetailNotificationDao(
                                data["title"] as String,
                                "Catatan Pribadi: \"" + data["content"] as String + "\"",
                                data["profilePic"] as String,
                                Timestamp(Date()),
                                data.id,
                                1,
                                data["contentId"] as String
                            )
                        )
                    }
                }
                mNotificationList.sortBy {
                    it.time
                }
                mNotificationdapter.notifyDataSetChanged()
                checkEmpty()
            }
        }

        toolbarNotificationClear.setOnClickListener {
            val collection =
                db.collection("notifications")
            deleteCollection(collection, 5) {
                mNotificationdapter.notifyDataSetChanged()
                checkEmpty()
            }
        }
    }

    private fun checkEmpty() {
        if (mNotificationList.isNotEmpty()) {
            tvNotificationEmpty.visibility = View.INVISIBLE
            ivNotificationEmpty.visibility = View.INVISIBLE
        }
    }

    private fun initRecycler() {
        mNotificationList.clear()
        mNotificationdapter = NotificationAdapter(mNotificationList, this)
        rvNotification.layoutManager = LinearLayoutManager(this)
        rvNotification.adapter = mNotificationdapter
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
