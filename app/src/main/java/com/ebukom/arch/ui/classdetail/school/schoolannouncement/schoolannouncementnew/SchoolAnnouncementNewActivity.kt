package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAddTemplateActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_school_anouncement_new.*
import kotlinx.android.synthetic.main.activity_school_anouncement_new.rvSchoolAnnouncementAttachment
import kotlinx.android.synthetic.main.activity_school_anouncement_new.rvSchoolAnnouncementNewTemplate
import kotlinx.android.synthetic.main.activity_school_anouncement_new.toolbar
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*
import kotlinx.android.synthetic.main.item_announcement_attachment.view.*

class SchoolAnnouncementNewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_anouncement_new)

        initToolbar()

        // Attachment
        val attachment: MutableList<ClassDetailAttachmentDao> = ArrayList()
        attachment.add(
            ClassDetailAttachmentDao(
                "https://drive.google.com",
                "drive.google.com", 0
            )
        )
        attachment.add(
            ClassDetailAttachmentDao(
                "-",
                "drive.google.com", 1
            )
        )
        attachment.add(
            ClassDetailAttachmentDao(
                "-",
                "drive.google.com", 2
            )
        )
        rvSchoolAnnouncementAttachment.apply {
            layoutManager = LinearLayoutManager(
                this@SchoolAnnouncementNewActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter =
                ClassDetailAttachmentAdapter(
                    attachment
                )
        }

        // Template Title
        val templateText: MutableList<ClassDetailTemplateTextDao> = ArrayList()
        templateText.add(ClassDetailTemplateTextDao("Field Trip"))
        for (i: Int in 1..10) templateText.add(ClassDetailTemplateTextDao("Perubahan Seragam"))
        rvSchoolAnnouncementNewTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SchoolAnnouncementNewActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter =
                ClassDetailTemplateTextAdapter(
                    templateText
                )
        }

        // Delete Attachment
        val view = layoutInflater.inflate(R.layout.item_announcement_attachment, null)
        view.ivItemAnnouncementAttachmentDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementNewActivity)

            builder.setMessage("Apakah Anda yakin ingin menghapus lampiran ini?")

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

        // "Tambah Template"
        tvSchoolAnnouncementNewTemplateAdd.setOnClickListener {
            startActivity(Intent(this, SchoolAnnouncementAddTemplateActivity::class.java))
        }

        // Next page
        btnSchoolAnnouncementNewNext
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.attachment_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.attachment -> {
                val bottomSheetDialog = BottomSheetDialog(this)
                val view =
                    layoutInflater.inflate(R.layout.bottom_sheet_class_detail_attachment, null)
                bottomSheetDialog.setContentView(view)

                view.clBottomClassDetailAttachmentPhoto.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    Toast.makeText(this, "A", Toast.LENGTH_LONG).show()
                }
                view.clBottomSheetClassDetailAttachmentFile.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
                }
                view.clBottomSheetClassDetailAttachmentLink.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
                }
                view.clBottomSheetClassDetailAttachmentUseCamera.setOnClickListener {
                }
                bottomSheetDialog.show()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
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


