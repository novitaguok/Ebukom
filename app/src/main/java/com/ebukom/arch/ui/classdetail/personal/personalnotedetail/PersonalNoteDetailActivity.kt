package com.ebukom.arch.ui.classdetail.personal.personalnotedetail

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailAdapter
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_personal_note_detail.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.toolbar
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_choose_class.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PersonalNoteDetailActivity : AppCompatActivity() {

    private val mCommentList: ArrayList<ClassDetailAnnouncementCommentDao> = arrayListOf()
    private val mCommentAdapter = SchoolAnnouncementDetailAdapter(mCommentList, this)
    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    private val mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)
    var noteId: String? = null
    var noteTitle: String? = null
    var noteContent: String? = null
    var noteUploadTime = Timestamp(Date())
    var commentId: String? = null
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_detail)

        initToolbar()
        initRecycler()
        checkEmpty()

        // Shared Preference
        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if (sharePref.getLong("level", 0) == 1L) {
            ivPersonalNoteDetailMoreButton.visibility = View.GONE
        }

        // Get Intent from SchoolAnnouncementFragment
        noteId = intent?.extras?.getString("noteId")
        noteTitle = intent?.extras?.getString("noteTitle")
        noteContent = intent?.extras?.getString("noteContent")
        noteUploadTime = intent?.getParcelableExtra("noteUploadTime")!!

        val uploadTime = noteUploadTime.toDate().toCalendar()
        val day = uploadTime.get(Calendar.DAY_OF_WEEK)
        val date = uploadTime.get(Calendar.DATE)
        val month = uploadTime.get(Calendar.MONTH)
        val year = uploadTime.get(Calendar.YEAR)
        var dayName = ""
        var monthName = ""

        when (day) {
            1 -> dayName = "Minggu"
            2 -> dayName = "Senin"
            3 -> dayName = "Selasa"
            4 -> dayName = "Rabu"
            5 -> dayName = "Kamis"
            6 -> dayName = "Jumat"
            7 -> dayName = "Sabtu"
        }

        when (month) {
            0 -> monthName = "Januari"
            1 -> monthName = "Febuari"
            2 -> monthName = "Maret"
            3 -> monthName = "April"
            4 -> monthName = "Mei"
            5 -> monthName = "Juni"
            6 -> monthName = "Juli"
            7 -> monthName = "Agustus"
            8 -> monthName = "September"
            9 -> monthName = "Oktober"
            10 -> monthName = "November"
            11 -> monthName = "Desember"
        }

        tvPersonalNoteDetailTitle.text = noteTitle
        tvPersonalNoteDetailContent.text = noteContent
        tvPersonalNoteDetailTime.text = dayName + ", " + date!! + " " + monthName + " " + (year!!)

        /**
         * Read note data
         */
        db.collection("notes").document(noteId!!).addSnapshotListener { value, error ->

            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            /**
             * Load attachment(s)
             */
            if (mAttachmentList.isNotEmpty()) {
                for (data in value!!["attachments"] as List<HashMap<Any, Any>>) {
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
        }

        /**
         * Load comment(s)
         */
        db.collection("notes").document(noteId!!).collection("comments")
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

        /**
         * Send comment
         */
        ivPersonalNoteDetailComment.setOnClickListener {
            val uid = sharePref.getString("uid", "") as String
            val teacherName = sharePref.getString("name", "") as String
            val comment = etPersonalNoteDetailComment.text.toString()
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

            etPersonalNoteDetailComment.text.clear()
            loading.visibility = View.VISIBLE
            db.collection("notes").document(noteId!!).collection("comments").add(data)
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

        // More Button
        ivPersonalNoteDetailMoreButton.setOnClickListener {
            popupMenuInfo()
        }
    }

    fun popupMenuInfo() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_choose_class, null)
        bottomSheetDialog.setContentView(view)

        view.tvDeleteClass.text = "Hapus Catatan"
        view.tvDeleteClass.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_delete_red,
            0,
            0,
            0
        )

        /**
         * Delete info
         */
        view.tvDeleteClass.setOnClickListener {
            val builder = AlertDialog.Builder(this@PersonalNoteDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus pengumuman ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                db.collection("notes").document(noteId!!).delete().addOnSuccessListener {
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

        view.tvCancelClass.setOnClickListener {
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
            val builder = AlertDialog.Builder(this@PersonalNoteDetailActivity)
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
            db.collection("notes").document(noteId!!).collection("comments").document(commentId)
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
                    db.collection("notes").document(noteId!!).collection("comments").document(commentId)
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
            val builder = AlertDialog.Builder(this@PersonalNoteDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus komentar ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                db.collection("notes").document(noteId!!).collection("comments").document(commentId)
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

    private fun initRecycler() {
        /**
         * Attachments list
         */
        rvPersonalNoteDetailAttachment.apply {
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
        rvPersonalNoteDetailComment.apply {
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
        if (mCommentList.isNotEmpty()) cvPersonalNoteDetailComment.visibility = View.VISIBLE
        else cvPersonalNoteDetailComment.visibility = View.GONE

        /**
         * Check empty attachment
         */
        if (mAttachmentList.isNotEmpty()) cvPersonalNoteDetailAttachment.visibility = View.VISIBLE
        else cvPersonalNoteDetailAttachment.visibility = View.GONE
    }

    fun Date.toCalendar() : Calendar{
        val cal = Calendar.getInstance()
        cal.time = this
        return cal
    }
}
