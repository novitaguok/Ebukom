package com.ebukom.arch.ui.classdetail.material.materialeducation.materialeducationdetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.classdetail.material.materialeducation.materialeducationedit.MaterialEducationEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailAdapter
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_material_education_detail.*
import kotlinx.android.synthetic.main.activity_material_education_detail.toolbar
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*

class MaterialEducationDetailActivity : AppCompatActivity() {
    private var pos: Int = -1
    private val mCommentList: ArrayList<ClassDetailAnnouncementCommentDao> = arrayListOf()
    private val mCommentAdapter = SchoolAnnouncementDetailAdapter(mCommentList, this)
    private val mEducationList: ArrayList<ClassDetailAnnouncementDao> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_education_detail)

        initToolbar()

        pos = intent?.extras?.getInt("pos", -1)?: -1

        checkEmptyComment()
        mEducationList.clear()
        mEducationList.addAll(DataDummy.educationData)

        val data = intent?.extras?.getSerializable("data") as ClassDetailAnnouncementDao
        tvMaterialEducationDetailTitle.text = data.announcementTitle
        tvMaterialEducationDetailContent.text = data.announcementContent
        tvMaterialEducationDetailTeacher.text = "Eni Trikuswanti"
        tvMaterialEducationDetailDate.text = "02/02/02"
        rvMaterialEducationDetailComment.apply {
            layoutManager =
                LinearLayoutManager(
                    this@MaterialEducationDetailActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mCommentAdapter
        }
        mCommentList.addAll(data.comments)
        mCommentAdapter.notifyDataSetChanged()

        // Comment
        ivMaterialEducationDetailComment.setOnClickListener {
            var comment = etMaterialEducationDetailComment.text.toString()

            etMaterialEducationDetailComment.text.clear()
            mCommentList.add(ClassDetailAnnouncementCommentDao("Ade Andreansyah", comment, R.drawable.bg_solid_gray))
            mCommentAdapter.notifyDataSetChanged()

            DataDummy.educationData[pos].comments = mCommentList
            mEducationList.clear()
            mEducationList.addAll(DataDummy.educationData)

            checkEmptyComment()
        }
        mEducationList.clear()
        mEducationList.addAll(DataDummy.educationData)
        checkEmptyComment()

        // More Button
        ivMaterialEducationDetailMoreButton.setOnClickListener {
            popupMenu1()
        }
    }

    fun popupMenu1() { // Material
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)

        view.tvEditInfo.text = "Edit Materi"
        view.tvDeleteInfo.text = "Hapus Materi"

        bottomSheetDialog.setContentView(view)

        // Edit
        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            val intent = Intent(this, MaterialEducationEditActivity::class.java)
            intent.putExtra("pos", pos)
            startActivity(intent)
        }

        // Delete
        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@MaterialEducationDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus materi ini?")

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                DataDummy.educationData.removeAt(pos)
                finish()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
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

    fun popupMenu2() { // Comment
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement_comment, null)

        bottomSheetDialog.setContentView(view)

        view.tvEditComment.setOnClickListener {
            val builder = AlertDialog.Builder(this@MaterialEducationDetailActivity)
            val view = layoutInflater.inflate(R.layout.alert_edit_text, null)

            bottomSheetDialog.dismiss()
            builder.setView(view)
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("SELESAI") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        view.tvDeleteComment.setOnClickListener {
            val builder = AlertDialog.Builder(this@MaterialEducationDetailActivity)

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
        }

        view.tvCancelComment.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show()
        }

        bottomSheetDialog.show()
    }

    private fun checkEmptyComment() {
        if (mCommentList.isNotEmpty()) cvMaterialEducationDetailComment.visibility = View.VISIBLE
        else cvMaterialEducationDetailComment.visibility = View.GONE
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        // Education List
        mEducationList.clear()
        mEducationList.addAll(DataDummy.educationData)
        mCommentAdapter.notifyDataSetChanged()

        checkEmptyComment()
    }
}