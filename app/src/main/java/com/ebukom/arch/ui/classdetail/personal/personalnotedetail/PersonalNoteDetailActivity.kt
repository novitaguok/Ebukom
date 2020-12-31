package com.ebukom.arch.ui.classdetail.personal.personalnotedetail

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_personal_note_detail.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.toolbar
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_choose_class.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*

class PersonalNoteDetailActivity : AppCompatActivity() {

    private var pos: Int = -1
    private val mCommentList: ArrayList<ClassDetailAnnouncementCommentDao> = arrayListOf()
    private val mCommentAdapter = SchoolAnnouncementDetailAdapter(mCommentList, this)
    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    private val mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)
    private val mNoteList: ArrayList<ClassDetailPersonalNoteDao> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_detail)

        initToolbar()

        // Shared Preference
        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getLong("level", 0) == 1L){
            ivPersonalNoteDetailMoreButton.visibility = View.GONE
        }

        pos = intent?.extras?.getInt("pos", -1)?: -1

        // Get Intent from SchoolAnnouncementFragment
        val data = intent?.extras?.getSerializable("data") as ClassDetailPersonalNoteDao
        tvPersonalNoteDetailTitle.text = data.noteTitle
        tvPersonalNoteDetailContent.text = data.noteContent
        tvPersonalNoteDetailTime.text = data.time

        // Attachment List
        mAttachmentList.addAll(data.attachments)
        mAttachmentAdapter.notifyDataSetChanged()
        rvPersonalNoteDetailAttachment.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mAttachmentAdapter
        }
        if (mAttachmentList.isEmpty()) cvPersonalNoteDetailAttachment.visibility = View.GONE

        // Comment List
        checkEmptyComment()
        rvPersonalNoteDetailComment.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mCommentAdapter
        }
        mCommentList.addAll(data.comments)
        mCommentAdapter.notifyDataSetChanged()

        ivPersonalNoteDetailComment.setOnClickListener {
            var comment = etPersonalNoteDetailComment.text.toString()

            etPersonalNoteDetailComment.text.clear()
            mCommentList.add(ClassDetailAnnouncementCommentDao("Ade Andreansyah", comment, R.drawable.bg_solid_gray))
            mCommentAdapter.notifyDataSetChanged()

            DataDummy.noteSentData[pos].comments = mCommentList
            mNoteList.clear()
            mNoteList.addAll(DataDummy.noteSentData)

            checkEmptyComment()
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
        view.tvDeleteClass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_delete_red, 0, 0,0)

        view.tvDeleteClass.setOnClickListener {
            val builder = AlertDialog.Builder(this@PersonalNoteDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus catatan ini?")

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
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

    fun popupMenuComment(pos: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement_comment, null)
        bottomSheetDialog.setContentView(view)

        // Edit comment
        view.tvEditComment.setOnClickListener {
            val builder = AlertDialog.Builder(this@PersonalNoteDetailActivity)
            val view = layoutInflater.inflate(R.layout.alert_edit_text, null)

            bottomSheetDialog.dismiss()

            builder.setView(view)

            builder.setPositiveButton("SELESAI", null)
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                if (view.etAlertEditText.text.toString().isEmpty()) {
                    view.tvAlertEditTextErrorMessage.visibility = View.VISIBLE
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

        // Delete comment
        view.tvDeleteComment.setOnClickListener {
            val builder = AlertDialog.Builder(this@PersonalNoteDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus komentar ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
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

        // Cancel
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

    private fun checkEmptyComment() {
        if (mCommentList.isNotEmpty()) cvPersonalNoteDetailComment.visibility = View.VISIBLE
        else cvPersonalNoteDetailComment.visibility = View.GONE
    }
}
