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
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*

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

        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Edit", Toast.LENGTH_LONG).show()
        }

        view.tvDeleteInfo.setOnClickListener {
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

        view.tvCancelInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show()
        }

        bottomSheetDialog.show()
    }

    fun popupMenu2() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement_comment, null)
        bottomSheetDialog.setContentView(view)

        view.tvEditComment.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)
            val view = layoutInflater.inflate(R.layout.alert_edit_text, null)

            bottomSheetDialog.dismiss()

//            builder.setTitle("Edit Kometar")
            builder.setView(view)
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext,"Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("SELESAI") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        view.tvDeleteComment.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus komentar ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext,"Next?", Toast.LENGTH_SHORT).show()
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
}
