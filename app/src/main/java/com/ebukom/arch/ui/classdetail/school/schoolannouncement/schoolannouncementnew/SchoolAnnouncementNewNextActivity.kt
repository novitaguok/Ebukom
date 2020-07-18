package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailItemCheckDao
import com.ebukom.arch.ui.classdetail.ClassDetailCheckAdapter
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import kotlinx.android.synthetic.main.activity_personal_note_new_next.*
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.*
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import java.util.*
import kotlin.collections.ArrayList

class SchoolAnnouncementNewNextActivity : AppCompatActivity() {

    val list = ArrayList<ClassDetailItemCheckDao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_new_next)

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

        sSchoolAnnouncementNewNextAlarm.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
//                val builder = AlertDialog.Builder(this@SchoolAnnouncementNewNextActivity)
//                val view = layoutInflater.inflate(R.layout.alert_edit_text, null)
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
}
