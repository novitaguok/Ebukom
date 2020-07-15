package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_school_announcement_edit.*
import kotlinx.android.synthetic.main.activity_school_announcement_edit.rvSchoolAnnouncementAttachment
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*

class SchoolAnnouncementEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_edit)

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
                this@SchoolAnnouncementEditActivity,
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
        rvSchoolAnnouncementEditTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SchoolAnnouncementEditActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter =
                ClassDetailTemplateTextAdapter(
                    templateText
                )
        }

        // Attach Icon
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_class_detail_attachment, null)
        bottomSheetDialog.setContentView(view)
        ivSchoolAnnouncementEditTitle.setOnClickListener {
            bottomSheetDialog.show()
        }
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

            val builder = AlertDialog.Builder(this@SchoolAnnouncementEditActivity)
            val view = layoutInflater.inflate(R.layout.alert_edit_text, null)

            view.tvAlertEditText.setText("Link")
            view.tilAlertEditText.setHint("Masukkan Link")

            builder.setView(view)
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext,"Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("LAMPIRKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        view.clBottomSheetClassDetailAttachmentUseCamera.setOnClickListener {

        }
    }

}
