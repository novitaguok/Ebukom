package com.ebukom.arch.ui.classdetail.personal.personalnotedetail

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
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit.SchoolAnnouncementEditActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_join_class.*
import kotlinx.android.synthetic.main.activity_personal_note_detail.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.toolbar
import kotlinx.android.synthetic.main.alert_edit_text.*
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_choose_class.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*
import kotlinx.android.synthetic.main.item_announcement.view.*

class PersonalNoteDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_detail)

        initToolbar()

        val list = ArrayList<ClassDetailAnnouncementCommentDao>()

        list.add(
            ClassDetailAnnouncementCommentDao(
                "Yusi Yuli",
                "Tidak paham Bu...",
                R.drawable.ic_book_blue
            )
        )
        list.add(
            ClassDetailAnnouncementCommentDao(
                "Eni Trikuswanti",
                "Baik Bu, Ibu Yuli bisa hubungi saya ketika ada yang tidak dipahami ya...",
                R.drawable.ic_book_blue
            )
        )

        // Shared Preference
        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getInt("level", 0) == 1){
            ivPersonalNoteDetailMoreButton.visibility = View.GONE
        }

        // Recycler View
        val adapter = SchoolAnnouncementDetailAdapter(list, this)
        rvPersonalNoteDetailComment.layoutManager = LinearLayoutManager(this)
        rvPersonalNoteDetailComment.adapter = adapter

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

    fun popupMenuComment() {
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
}
