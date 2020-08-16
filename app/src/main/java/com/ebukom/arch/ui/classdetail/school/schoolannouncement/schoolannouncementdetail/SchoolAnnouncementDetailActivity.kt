package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit.SchoolAnnouncementEditActivity
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_join_class.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.toolbar
import kotlinx.android.synthetic.main.alert_edit_text.*
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*
import kotlinx.android.synthetic.main.item_announcement.*

class SchoolAnnouncementDetailActivity : AppCompatActivity() {

    private var pos: Int = -1
    private val mCommentList: ArrayList<ClassDetailAnnouncementCommentDao> = arrayListOf()
    private val mCommentAdapter = SchoolAnnouncementDetailAdapter(mCommentList, this)
    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    private val mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)
    private val mAnnouncementList: ArrayList<ClassDetailAnnouncementDao> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_detail)

        initToolbar()

        mAnnouncementList.clear()
        mAnnouncementList.addAll(DataDummy.announcementData)

        // Shared Preference
        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getInt("level", 0) == 1){
            ivAnnouncementDetailMoreButton.visibility = View.GONE
        }

        // Get intent from Material Education FRAGMENT
        val layout = intent.extras?.getString("layout", "0")
        when (layout) {
            "1" -> {
                tvSchoolAnnouncementDetailTitle.text = "Mendidik Anak Hyperaktif"
                tvSchoolAnnouncementDetailContent.text =
                    "Untuk mendidik anak yang hyperaktif, diperlukan suatu kemampuan yaitu kesabaran yang luar biasa. Selain itu, perlu diketahui juga cara menghadapi anak dengan cara yang menyenangkan dan baik."
            }
        }

        pos = intent?.extras?.getInt("pos", -1)?: -1

        // Get Intent from SchoolAnnouncementFragment
        val data = intent?.extras?.getSerializable("data") as ClassDetailAnnouncementDao
        tvSchoolAnnouncementDetailTitle.text = data.announcementTitle
        tvSchoolAnnouncementDetailContent.text = data.announcementContent
        tvSchoolAnnouncementDetailTeacher.text = "Eni Trikuswanti"
        tvSchoolAnnouncementDetailDate.text = data.time
        mAttachmentList.addAll(data.attachments)
        mAttachmentAdapter.notifyDataSetChanged()
        rvSchoolAnnouncementAttachmentDetail.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = mAttachmentAdapter
        }
        if (mAttachmentList.isEmpty()) cvSchoolAnnouncementDetailAttachment.visibility = View.GONE

        // Comment List
        rvSchoolAnnouncementDetailComment.apply {
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

        ivSchoolAnnouncementDetailComment.setOnClickListener {
            var comment = etSchoolAnnouncementDetailComment.text.toString()

            etSchoolAnnouncementDetailComment.text.clear()
            mCommentList.add(ClassDetailAnnouncementCommentDao("Ade Andreansyah", comment, R.drawable.bg_solid_gray))
            mCommentAdapter.notifyDataSetChanged()

            DataDummy.announcementData[pos].comments = mCommentList
            mAnnouncementList.clear()
            mAnnouncementList.addAll(DataDummy.announcementData)

            checkEmptyComment()
        }
        mAnnouncementList.clear()
        mAnnouncementList.addAll(DataDummy.announcementData)
        checkEmptyComment()

        // Announcement's More Button
        ivAnnouncementDetailMoreButton.setOnClickListener {
            popupMenuInfo()
        }
    }

    private fun checkEmptyComment() {
        if (mCommentList.isNotEmpty()) cvSchoolAnnouncementDetailComment.visibility = View.VISIBLE
        else cvSchoolAnnouncementDetailComment.visibility = View.GONE
    }

    fun popupMenuInfo() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)
        bottomSheetDialog.setContentView(view)

        // Edit Info
        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            val intent = Intent(this, SchoolAnnouncementEditActivity::class.java)
            intent.putExtra("pos", pos)
            startActivity(intent)
        }

        // Delete Info
        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus pengumuman ini?")

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                DataDummy.announcementData.removeAt(pos)
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

    fun popupMenuComment(comPosition: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement_comment, null)
        bottomSheetDialog.setContentView(view)

        // Edit comment
        view.tvEditComment.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)
            val view = layoutInflater.inflate(R.layout.alert_edit_text, null)

            bottomSheetDialog.dismiss()
            view.etAlertEditText.setText(mCommentList[comPosition].comment)

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
                } else {
                    dialog.dismiss()

                    var comment = view.etAlertEditText.text.toString()
                    mCommentList[comPosition].comment = comment
                    mCommentAdapter.notifyDataSetChanged()

                    DataDummy.announcementData[pos].comments = mCommentList
                    mAnnouncementList.clear()
                    mAnnouncementList.addAll(DataDummy.announcementData)

                    checkEmptyComment()
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
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus komentar ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                mCommentList.removeAt(comPosition)
                mCommentAdapter.notifyDataSetChanged()

                DataDummy.announcementData[pos].comments = mCommentList
                mAnnouncementList.clear()
                mAnnouncementList.addAll(DataDummy.announcementData)

                checkEmptyComment()
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
}
