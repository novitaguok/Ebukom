package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementAttachmentDao
import com.ebukom.arch.dao.ClassDetailAnnouncementTemplateDao
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAttachmentAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementTemplateAdapter
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.REQUEST_CODE
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_school_anouncement_new.*
import kotlinx.android.synthetic.main.activity_school_anouncement_new.rvSchoolAnnouncementNewTemplate
import kotlinx.android.synthetic.main.bottom_sheet_announcement_attachment.view.*

class SchoolAnnouncementNewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_anouncement_new)

        // Attachment
        val attachment: MutableList<ClassDetailAnnouncementAttachmentDao> = ArrayList()

        attachment.add(
            ClassDetailAnnouncementAttachmentDao(
                "https://drive.google.com",
                "drive.google.com", 0
            )
        )
        attachment.add(
            ClassDetailAnnouncementAttachmentDao(
                "-",
                "drive.google.com", 1
            )
        )
        attachment.add(
            ClassDetailAnnouncementAttachmentDao(
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
            adapter = SchoolAnnouncementAttachmentAdapter(
                attachment
            )
        }

        // Template Title
        val template: MutableList<ClassDetailAnnouncementTemplateDao> = ArrayList()
        template.add(ClassDetailAnnouncementTemplateDao("Field Trip"))
        for (i: Int in 1..10) template.add(ClassDetailAnnouncementTemplateDao("Perubahan Seragam"))
        rvSchoolAnnouncementNewTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SchoolAnnouncementNewActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = SchoolAnnouncementTemplateAdapter(
                template
            )
        }

        // Attach Icon
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_announcement_attachment, null)
        bottomSheetDialog.setContentView(view)
        ivSchoolAnnouncementNewAttach.setOnClickListener {
            bottomSheetDialog.show()
        }
        view.clBottomSheetAnnouncementAttachmentPhoto.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "A", Toast.LENGTH_LONG).show()
        }
        view.clBottomSheetAnnouncementAttachmentFile.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
        }
        view.clBottomSheetAnnouncementAttachmentLink.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
        }
        view.clBottomSheetAnnouncementAttachmentUseCamera.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = data?.extras?.get("data") as Bitmap
//            imageView.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun popupMenu() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_announcement_attachment, null)

        bottomSheetDialog.setContentView(view)
//        ivAnnouncementMoreButton.setOnClickListener {
        bottomSheetDialog.show()
//        }
//
//        view.tvEditInfo.setOnClickListener {
//            bottomSheetDialog.dismiss()
//            Toast.makeText(this, "Edit Info", Toast.LENGTH_LONG).show()
//        }
    }
}
