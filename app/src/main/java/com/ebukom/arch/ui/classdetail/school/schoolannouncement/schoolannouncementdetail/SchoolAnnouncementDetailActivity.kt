package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_school_announcement_detail.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_header.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*
import kotlinx.android.synthetic.main.item_announcement_detail_comment.view.*

class SchoolAnnouncementDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_detail)


        val list = ArrayList<ClassDetailAnnouncementCommentDao>()

        list.add(ClassDetailAnnouncementCommentDao("Ade Andreansyah", "Aku pusing banyak euy yang dibawa, udahlah mending skip cuy", R.drawable.ic_book_blue))
        list.add(ClassDetailAnnouncementCommentDao("Ade Andreansyah", "Aku pusing banyak euy yang dibawa, udahlah mending skip cuy", R.drawable.ic_book_blue))
        list.add(ClassDetailAnnouncementCommentDao("Ade Andreansyah", "Aku pusing banyak euy yang dibawa, udahlah mending skip cuy", R.drawable.ic_book_blue))
        list.add(ClassDetailAnnouncementCommentDao("Ade Andreansyah", "Aku pusing banyak euy yang dibawa, udahlah mending skip cuy", R.drawable.ic_book_blue))
        list.add(ClassDetailAnnouncementCommentDao("Ade Andreansyah", "Aku pusing banyak euy yang dibawa, udahlah mending skip cuy", R.drawable.ic_book_blue))

        val adapter = SchoolAnnouncementDetailAdapter(list, this)

        rvSchoolAnnouncementDetailComment.layoutManager = LinearLayoutManager(this)
        rvSchoolAnnouncementDetailComment.adapter = adapter

        ivAnnouncementDetailMoreButton.setOnClickListener {
            popupMenu1()
        }
    }

    fun popupMenu1() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)
        bottomSheetDialog.setContentView(view)

        view.editInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Edit", Toast.LENGTH_LONG).show()
        }

        view.deleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus pengumuman ini?")

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext,"Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        view.cancelInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show()
        }

        bottomSheetDialog.show()
    }

    fun popupMenu2() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement_comment, null)
        bottomSheetDialog.setContentView(view)

        view.editComment.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Edit", Toast.LENGTH_LONG).show()
        }

        view.deleteComment.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus pengumuman ini?")

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext,"Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        view.cancelComment.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show()
        }

        bottomSheetDialog.show()
    }
}
