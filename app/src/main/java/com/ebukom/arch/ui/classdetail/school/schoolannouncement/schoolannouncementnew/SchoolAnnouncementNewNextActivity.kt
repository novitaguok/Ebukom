package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailItemCheckDao
import com.ebukom.arch.ui.classdetail.ClassDetailCheckAdapter
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.*
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.toolbar
import kotlinx.android.synthetic.main.item_check.view.*
import kotlin.collections.ArrayList

class SchoolAnnouncementNewNextActivity : AppCompatActivity() {

    val list = ArrayList<ClassDetailItemCheckDao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_new_next)

        initToolbar()

        list.add(ClassDetailItemCheckDao("Semua Kelas"))
        list.add(ClassDetailItemCheckDao("Kelas 1 Aurora"))
        list.add(ClassDetailItemCheckDao("Kelas 1 Astronot"))
        list.add(ClassDetailItemCheckDao("Kelas 2 Aurora"))
        list.add(ClassDetailItemCheckDao("Kelas 3 Astronot"))

        rvSchoolAnnouncementNewNext.apply {
            layoutManager = LinearLayoutManager(
                this@SchoolAnnouncementNewNextActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter =
                ClassDetailCheckAdapter(
                    list
                )
        }

        // Checkbox is checked
        val view = layoutInflater.inflate(R.layout.item_check, null)
        if (view.cbItemCheck.isChecked) {
            btnSchoolAnnouncementNewNextDone.setEnabled(true)
            btnSchoolAnnouncementNewNextDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        } else {
            btnSchoolAnnouncementNewNextDone.setEnabled(false)
            btnSchoolAnnouncementNewNextDone.setBackgroundColor(
                Color.parseColor("#828282")
            )
        }

        //
        sSchoolAnnouncementNewNextAlarm.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                val builder = AlertDialog.Builder(this@SchoolAnnouncementNewNextActivity)
                val view = layoutInflater.inflate(R.layout.alert_datetime_picker, null)
//
//                view.tvAlertEditText.setText("Link")
//                view.tilAlertEditText.setHint("Masukkan Link")
//
//                builder.setView(view)
//                builder.setNegativeButton("BATALKAN") { dialog, which ->
//                    Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
//                }
//                builder.setPositiveButton("LAMPIRKAN") { dialog, which ->
//                    Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
//                }
//
//                val dialog: AlertDialog = builder.create()
//                dialog.show()

            }
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
