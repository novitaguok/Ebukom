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
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit.SchoolAnnouncementEditActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_join_class.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.toolbar
import kotlinx.android.synthetic.main.alert_edit_text.*
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*

class SchoolAnnouncementDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_detail)

        initToolbar()

        // Get intent from Material Education FRAGMENT
        val layout = intent.extras?.getString("layout", "0")
        when (layout) {
            "1" -> {
                tvSchoolAnnouncementDetailTitle.text = "Mendidik Anak Hyperaktif"
                tvSchoolAnnouncementDetailContent.text =
                    "Untuk mendidik anak yang hyperaktif, diperlukan suatu kemampuan yaitu kesabaran yang luar biasa. Selain itu, perlu diketahui juga cara menghadapi anak dengan cara yang menyenangkan dan baik."
            }
        }

        val list = ArrayList<ClassDetailAnnouncementCommentDao>()

        list.add(
            ClassDetailAnnouncementCommentDao(
                "Ade Andreansyah",
                "Aku pusing banyak euy yang dibawa, udahlah mending skip cuy",
                R.drawable.ic_book_blue
            )
        )
        list.add(
            ClassDetailAnnouncementCommentDao(
                "Ade Andreansyah",
                "Aku pusing banyak euy yang dibawa, udahlah mending skip cuy",
                R.drawable.ic_book_blue
            )
        )
        list.add(
            ClassDetailAnnouncementCommentDao(
                "Ade Andreansyah",
                "Aku pusing banyak euy yang dibawa, udahlah mending skip cuy",
                R.drawable.ic_book_blue
            )
        )
        list.add(
            ClassDetailAnnouncementCommentDao(
                "Ade Andreansyah",
                "Aku pusing banyak euy yang dibawa, udahlah mending skip cuy",
                R.drawable.ic_book_blue
            )
        )
        list.add(
            ClassDetailAnnouncementCommentDao(
                "Ade Andreansyah",
                "Aku pusing banyak euy yang dibawa, udahlah mending skip cuy",
                R.drawable.ic_book_blue
            )
        )

        // Shared Preference
        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getInt("level", 0) == 1){
            ivAnnouncementDetailMoreButton.visibility = View.GONE
        }

        // Adapter
        val adapter = SchoolAnnouncementDetailAdapter(list, this)
        rvSchoolAnnouncementDetailComment.layoutManager = LinearLayoutManager(this)
        rvSchoolAnnouncementDetailComment.adapter = adapter

        ivAnnouncementDetailMoreButton.setOnClickListener {
            popupMenuInfo()
        }
    }

    fun popupMenuInfo() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)
        bottomSheetDialog.setContentView(view)

        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            startActivity(Intent(this, SchoolAnnouncementEditActivity::class.java))
        }

        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus pengumuman ini?")

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

        view.tvCancelInfo.setOnClickListener {
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
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)
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
            val builder = AlertDialog.Builder(this@SchoolAnnouncementDetailActivity)

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
