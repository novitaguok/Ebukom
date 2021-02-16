package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailCommentDao
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementmainpage.SchoolAnnouncementActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew.SchoolAnnouncementNewActivity
import com.ebukom.utils.toCalendar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_announcement_detail.*
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*
import kotlinx.android.synthetic.main.item_note.*
//import kotlinx.android.synthetic.main.dialog_file_preview.view.*
import timber.log.Timber
import java.util.*


class SchoolAnnouncementDetailActivity : AppCompatActivity() {

    private val mCommentList: ArrayList<ClassDetailCommentDao> = arrayListOf()
    private val mCommentAdapter = SchoolAnnouncementDetailAdapter(mCommentList, this)
    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    private val mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)
    val db = FirebaseFirestore.getInstance()
    var announcementId: String? = null
    var classId: String? = null
    var commentId: String? = null
    var notificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_detail)

        initToolbar()
        initRecycler()
        checkEmpty()

        /**
         * Share preference
         */
        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if (sharePref.getLong("level", 0) == 1L) {
            ivAnnouncementDetailMoreButton.visibility = View.GONE
        }

        /**
         * Get intent from Material Education FRAGMENT
         */
        when (intent.extras?.getString("layout", "0")) {
            "1" -> {
                tvSchoolAnnouncementDetailTitle.text = "Mendidik Anak Hyperaktif"
                tvSchoolAnnouncementDetailContent.text =
                    "Untuk mendidik anak yang hyperaktif, diperlukan suatu kemampuan yaitu kesabaran yang luar biasa. Selain itu, perlu diketahui juga cara menghadapi anak dengan cara yang menyenangkan dan baik."
            }
        }

        /**
         * Get Intent from SchoolAnnouncementActivity
         */
        announcementId = intent?.extras?.getString("announcementId")
        classId = intent?.extras?.getString("classId")
        notificationId = intent?.extras?.getString("notificationId")

        if (notificationId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("notifications").document(notificationId!!).delete()
        }

        /**
         * Read announcement data
         */
        if (classId != null) {
            db.collection("classes").document(classId!!).collection("announcements")
                .document(announcementId!!).addSnapshotListener { value, error ->

                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    // Toolbar title
                    var time = (value?.get("event_start") as? Timestamp)?.toDate()?.toCalendar()
                    var day = time?.get(Calendar.DAY_OF_WEEK) ?: 1
                    var date = time?.get(Calendar.DATE) ?: 1
                    var month = time?.get(Calendar.MONTH) ?: 1
                    var year = time?.get(Calendar.YEAR) ?: 1990
                    var dayName = ""
                    var monthName = ""
                    var pair = initTime(day, dayName, month, monthName)
                    dayName = pair.first
                    monthName = pair.second
                    tvSchoolAnnouncementDetailToolbar.text =
                        dayName + ", " + date + " " + monthName + " " + (year)

                    // Announcement time
                    time = (value?.get("time") as? Timestamp)?.toDate()?.toCalendar()
                    day = time?.get(Calendar.DAY_OF_WEEK) ?: 1
                    date = time?.get(Calendar.DATE) ?: 1
                    month = time?.get(Calendar.MONTH) ?: 1
                    year = time?.get(Calendar.YEAR) ?: 1990
                    dayName = ""
                    monthName = ""
                    pair = initTime(day, dayName, month, monthName)
                    dayName = pair.first
                    monthName = pair.second
                    tvSchoolAnnouncementDetailDate.text =
                        dayName + ", " + date + " " + monthName + " " + (year)
                    tvSchoolAnnouncementDetailTitle.text = value?.get("title") as? String
                    tvSchoolAnnouncementDetailContent.text = value?.get("content") as? String
                    tvSchoolAnnouncementDetailTeacher.text = value?.get("teacher.name") as? String

                    /**
                     * Load attachment(s)
                     */
                    if (value != null) {
                        val data = value.get("attachments") as List<HashMap<String, Any>?>?
                        if (data?.isNotEmpty() == true) {
                            data.forEach {
                                if (!it.isNullOrEmpty())
                                    mAttachmentList.add(
                                        ClassDetailAttachmentDao(
                                            it["path"] as String,
                                            (it["category"] as Long).toInt(),
                                            "",
                                            "",
                                            "",
                                            "",
                                            it["fileName"] as String
                                        )
                                    )
                                mAttachmentAdapter.notifyDataSetChanged()
                                checkEmpty()
                            }
                        }
                    }
                }

            /**
             * Load comment(s)
             */
            db.collection("classes").document(classId!!).collection("announcements")
                .document(announcementId!!).collection("comments")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    mCommentList.clear()
                    for (document in value!!.documents) {
                        mCommentList.add(
                            ClassDetailCommentDao(
                                document["user.name"] as String,
                                document["comment"] as String,
                                R.drawable.bg_square_blue_4dp,
                                (document["upload_time"] as Timestamp).toDate().toString(),
                                document.id
                            )
                        )

                        mCommentList.sortBy {
                            it.time
                        }
                        mCommentAdapter.notifyDataSetChanged()
                        checkEmpty()
                    }
                }

        }

        /**
         * Send comment
         */
        ivSchoolAnnouncementDetailComment.setOnClickListener {
            val uid = sharePref.getString("uid", "") as String
            val teacherName = sharePref.getString("name", "") as String
            val comment = etSchoolAnnouncementDetailComment.text.toString()
            val profilePic = R.drawable.bg_books
            val data = hashMapOf<Any, Any>(
                "comment" to comment,
                "user" to mapOf<String, Any>(
                    "name" to teacherName,
                    "id" to uid,
                    "picture" to profilePic
                ),
                "upload_time" to Timestamp(Date())
            )

            etSchoolAnnouncementDetailComment.text.clear()
            loading.visibility = View.VISIBLE
            if (classId != null) {
                db.collection("classes").document(classId!!).collection("announcements")
                    .document(announcementId!!).collection("comments").add(data)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            commentId = it.result?.id!!
                            Log.d("TAG", "comment is sent")

                            mCommentList.sortBy {
                                it.time
                            }
                            mCommentAdapter.notifyDataSetChanged()
                            checkEmpty()

                            loading.visibility = View.GONE
                        } else {
                            Log.d("TAG", "comment is failed to be sent")
                            finish()
                        }
                    }
            }
        }

        /**
         * Announcement 3 dots button
         */
        ivAnnouncementDetailMoreButton.setOnClickListener {
            popupMenuInfo()
        }
    }

    private fun initTime(
        day: Int?,
        dayName: String,
        month: Int?,
        monthName: String
    ): Pair<String, String> {
        var dayName1 = dayName
        var monthName1 = monthName
        when (day) {
            1 -> dayName1 = "Minggu"
            2 -> dayName1 = "Senin"
            3 -> dayName1 = "Selasa"
            4 -> dayName1 = "Rabu"
            5 -> dayName1 = "Kamis"
            6 -> dayName1 = "Jumat"
            7 -> dayName1 = "Sabtu"
        }

        when (month) {
            0 -> monthName1 = "Januari"
            1 -> monthName1 = "Febuari"
            2 -> monthName1 = "Maret"
            3 -> monthName1 = "April"
            4 -> monthName1 = "Mei"
            5 -> monthName1 = "Juni"
            6 -> monthName1 = "Juli"
            7 -> monthName1 = "Agustus"
            8 -> monthName1 = "September"
            9 -> monthName1 = "Oktober"
            10 -> monthName1 = "November"
            11 -> monthName1 = "Desember"
        }
        return Pair(dayName1, monthName1)
    }

    private fun initRecycler() {
        /**
         * Attachments list
         */
        rvSchoolAnnouncementDetailAttachment.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mAttachmentAdapter
        }
        mAttachmentAdapter.notifyDataSetChanged()

        /**
         * Comments list
         */
        rvSchoolAnnouncementDetailComment.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mCommentAdapter
        }
        mCommentAdapter.notifyDataSetChanged()
    }

    private fun checkEmpty() {
        /**
         * Check empty comment
         */
        if (mCommentList.isNotEmpty()) cvSchoolAnnouncementDetailComment.visibility =
            View.VISIBLE
        else cvSchoolAnnouncementDetailComment.visibility = View.GONE

        /**
         * Check empty attachment
         */
        if (mAttachmentList.isEmpty()) cvSchoolAnnouncementDetailAttachment.visibility =
            View.GONE
        else cvSchoolAnnouncementDetailAttachment.visibility = View.VISIBLE
    }

    fun popupMenuInfo() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)
        bottomSheetDialog.setContentView(view)

        /**
         * Edit info
         */
        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            val intent = Intent(this, SchoolAnnouncementNewActivity::class.java)
            intent.putExtra("announcementId", announcementId)
            intent.putExtra("classId", classId)
            intent.putExtra("layout", "edit")
            startActivity(intent)
        }

        /**
         * Delete info
         */
        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus pengumuman ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                // Delete notification
                db.collection("notifications").whereEqualTo("contentId", announcementId).get()
                    .addOnSuccessListener { notif ->
                        var notificationIds = arrayListOf<String>()
                        notif.forEach {
                            notificationIds.add(it.id)
                        }
                        if (notificationIds.isNotEmpty()) {
                            notificationIds.forEach {
                                db.collection("notifications").document(it).delete()
                                    .addOnSuccessListener {
                                        // Delete comments collection
                                        val collectionComments =
                                            db.collection("classes").document(classId!!)
                                                .collection("announcements")
                                                .document(announcementId!!).collection("comments")
                                        deleteCollection(collectionComments, 5) {}

                                        // Delete the announcement
                                        db.collection("classes").document(classId!!)
                                            .collection("announcements")
                                            .document(announcementId!!).delete()
                                            .addOnSuccessListener {
                                                Log.d("TAG", "announcement deleted")
                                                finish()
                                            }.addOnFailureListener {
                                                Log.d("TAG", "announcement is failed to be deleted")
                                                finish()
                                            }
                                    }
                            }
                        } else {
                            // Delete comments collection
                            val collectionComments =
                                db.collection("classes").document(classId!!)
                                    .collection("announcements")
                                    .document(announcementId!!).collection("comments")
                            deleteCollection(collectionComments, 5) {}

                            // Delete the announcement
                            db.collection("classes").document(classId!!)
                                .collection("announcements")
                                .document(announcementId!!).delete()
                                .addOnSuccessListener {
                                    Log.d("TAG", "announcement deleted")
                                    finish()
                                }.addOnFailureListener {
                                    Log.d("TAG", "announcement is failed to be deleted")
                                    finish()
                                }
                        }
                    }
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )

            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorRed
                )
            )
        }

        view.tvCancelInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show()
        }

        bottomSheetDialog.show()
    }

    fun popupMenuComment(commentId: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view =
            layoutInflater.inflate(R.layout.bottom_sheet_school_announcement_comment, null)
        bottomSheetDialog.setContentView(view)

        /**
         * Edit comment
         */
        view.tvEditComment.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)
            val view = layoutInflater.inflate(R.layout.alert_edit_text, null)
            var comment: String = ""

            bottomSheetDialog.dismiss()

            builder.setView(view)
            builder.setPositiveButton("SELESAI", null)
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                dialog.dismiss()
            }

            /**
             * Get comment by commentId
             */
            db.collection("classes").document(classId!!).collection("announcements")
                .document(announcementId!!).collection("comments").document(commentId)
                .get().addOnSuccessListener {
                    view.etAlertEditText.setText(it["comment"] as String)
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            /**
             * Update comment
             */
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                if (view.etAlertEditText.text.toString().isEmpty()) {
                    view.tvAlertEditTextErrorMessage.visibility = View.VISIBLE
                } else {
                    dialog.dismiss()

                    comment = view.etAlertEditText.text.toString()
                    db.collection("classes").document(classId!!).collection("announcements")
                        .document(announcementId!!).collection("comments").document(commentId)
                        .update("comment", comment).addOnSuccessListener {
                            Log.d("TAG", "comment updated")

                            mCommentList.sortBy {
                                it.time
                            }
                            mCommentAdapter.notifyDataSetChanged()
                            checkEmpty()
                        }.addOnFailureListener {
                            Log.d("TAG", "comment is failed to be updated")
                        }

                    mCommentList.sortBy {
                        it.time
                    }
                    mCommentAdapter.notifyDataSetChanged()
                    checkEmpty()
                }
            }
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )

            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorRed
                )
            )
        }

        /**
         * Delete comment
         */
        view.tvDeleteComment.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus komentar ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                db.collection("classes").document(classId!!).collection("announcements")
                    .document(announcementId!!).collection("comments").document(commentId)
                    .delete().addOnSuccessListener {
                        Log.d("TAG", "announcement deleted")

                        mCommentList.sortBy {
                            it.time
                        }
                        mCommentAdapter.notifyDataSetChanged()
                        checkEmpty()
                    }.addOnFailureListener {
                        Log.d("TAG", "announcement is failed to be deleted")
                    }

                mCommentList.sortBy {
                    it.time
                }
                mCommentAdapter.notifyDataSetChanged()
                checkEmpty()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )

            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorRed
                )
            )
        }

        /**
         * Cancel comment
         */
        view.tvCancelComment.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
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
