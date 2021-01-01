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
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit.SchoolAnnouncementEditActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_announcement_detail.*
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

class SchoolAnnouncementDetailActivity : AppCompatActivity() {

    private val mCommentList: ArrayList<ClassDetailAnnouncementCommentDao> = arrayListOf()
    private val mCommentAdapter = SchoolAnnouncementDetailAdapter(mCommentList, this)
    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    private val mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)
    val db = FirebaseFirestore.getInstance()
    var announcementId: String? = null
    var classId: String? = null
    var commentId: String? = null

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

                    val day = (value?.get("time") as Timestamp).toDate().day.toString()
                    val date = (value["time"] as Timestamp).toDate().date.toString()
                    val month = (value["time"] as Timestamp).toDate().month.toString()
                    val year = (value["time"] as Timestamp).toDate().year.toString()
                    var dayName = ""
                    var monthName = ""

                    when (day) {
                        "0" -> dayName = "Minggu"
                        "1" -> dayName = "Senin"
                        "2" -> dayName = "Selasa"
                        "3" -> dayName = "Rabu"
                        "4" -> dayName = "Kamis"
                        "5" -> dayName = "Jumat"
                        "6" -> dayName = "Sabtu"
                    }

                    when (month) {
                        "0" -> monthName = "Januari"
                        "1" -> monthName = "Febuari"
                        "2" -> monthName = "Maret"
                        "3" -> monthName = "April"
                        "4" -> monthName = "Mei"
                        "5" -> monthName = "Juni"
                        "6" -> monthName = "Juli"
                        "7" -> monthName = "Agustus"
                        "8" -> monthName = "September"
                        "9" -> monthName = "Oktober"
                        "10" -> monthName = "November"
                        "11" -> monthName = "Desember"
                    }

                    tvSchoolAnnouncementDetailToolbar.text =
                        dayName + ", " + date + " " + monthName + " " + year
                    tvSchoolAnnouncementDetailDate.text = dayName
                    tvSchoolAnnouncementDetailTitle.text = value?.get("title") as String
                    tvSchoolAnnouncementDetailContent.text = value["content"] as String
                    tvSchoolAnnouncementDetailTeacher.text = value["teacher.name"] as String

                    /**
                     * Load attachment(s)
                     */
                    for (data in value["attachments"] as List<HashMap<Any, Any>>) {
                        mAttachmentList.add(
                            ClassDetailAttachmentDao(
                                data["path"] as String,
                                (data["category"] as Long).toInt()
                            )
                        )
                        mAttachmentAdapter.notifyDataSetChanged()
                        checkEmpty()
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
                            ClassDetailAnnouncementCommentDao(
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

            val intent = Intent(this, SchoolAnnouncementEditActivity::class.java)
            intent.putExtra("announcementId", announcementId)
            intent.putExtra("classId", classId)
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
                db.collection("classes").document(classId!!).collection("announcements")
                    .document(announcementId!!).delete().addOnSuccessListener {
                        Log.d("TAG", "announcement deleted")
                    }.addOnFailureListener {
                        Log.d("TAG", "announcement is failed to be deleted")
                    }
                finish()
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
}
