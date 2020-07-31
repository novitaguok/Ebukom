package com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolscheduleedit

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ebukom.R
import kotlinx.android.synthetic.main.activity_school_schedule_edit.*
import kotlinx.android.synthetic.main.activity_school_schedule_new.*
import kotlinx.android.synthetic.main.activity_school_schedule_new.loading
import kotlinx.android.synthetic.main.activity_school_schedule_new.toolbar

class SchoolScheduleEditActivity : AppCompatActivity() {
    var isSetPelajaran = false
    var isSetEskul = false
    var isSetCalender = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_schedule_edit)

        initToolbar()

        // File chooser
        btnSchoolScheduleEditSubject.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "*/*"
            startActivityForResult(fileIntent, 10)
        }
        btnSchoolScheduleEditEskul.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "*/*"
            startActivityForResult(fileIntent, 11)
        }
        btnSchoolScheduleEditCalendar.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "*/*"
            startActivityForResult(fileIntent, 12)
        }

        // "BAGIKAN JADWAL % KALENDER" button
        btnSchoolScheduleEditDone.setOnClickListener {
            Handler().postDelayed({
                loading.visibility = View.GONE
                finish()
            }, 1000)
        }

        checkSection()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                10, 11, 12 -> {
                    val path: String = data?.data?.path ?: ""
                    newSchedule(path, requestCode)
                }
                else -> {
                    btnSchoolScheduleEditDone.isEnabled = false
                    btnSchoolScheduleEditDone.setBackgroundColor(
                        Color.parseColor("#BDBDBD")
                    )
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun newSchedule(path: String, section: Int = 0) {
        var btn: Button? = findViewById(R.id.btnSchoolScheduleEditSubject)
        var tv: TextView? = findViewById(R.id.tvSchoolScheduleEditSubjectPath)
        var iv: ImageView? = findViewById(R.id.ivSchoolScheduleEditSubjectDelete)
        var alert: String? = ""

        if (section == 10) {
            btn = findViewById(R.id.btnSchoolScheduleEditSubject)
            tv = findViewById(R.id.tvSchoolScheduleEditSubjectPath)
            iv = findViewById(R.id.ivSchoolScheduleEditSubjectDelete)
            alert = "jadwal"
            isSetPelajaran = true
        } else if (section == 11) {
            btn = findViewById(R.id.btnSchoolScheduleEditEskul)
            tv = findViewById(R.id.tvSchoolScheduleEditEskulPath)
            iv = findViewById(R.id.ivSchoolScheduleEditEskulDelete)
            alert = "jadwal"
            isSetEskul = true
        } else if (section == 12) {
            btn = findViewById(R.id.btnSchoolScheduleEditCalendar)
            tv = findViewById(R.id.tvSchoolScheduleEditCalendarPath)
            iv = findViewById(R.id.ivSchoolScheduleEditCalendarDelete)
            alert = "kalender"
            isSetCalender = true;
        }

        tv?.text = path
        tv?.visibility = View.VISIBLE
        iv?.visibility = View.VISIBLE
        btn?.setText("UBAH FILE")
        btn?.background =
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.btn_red_rectangle
            )

        btnSchoolScheduleEditDone.isEnabled = true
        btnSchoolScheduleEditDone.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorSuperDarkBlue
            )
        )

        // Alert
        iv?.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolScheduleEditActivity)

            builder.setMessage("Apakah Anda yakin ingin menghapus $alert ini?")

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                alert = alert?.toUpperCase()
                btn?.setText("PILIH FILE $alert")
                btn?.background =
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.btn_darkblue_rectangle
                    )
                tv?.visibility = View.INVISIBLE
                iv?.visibility = View.GONE

                if (section == 10) isSetPelajaran = false
                else if (section == 11) isSetEskul = false
                else if (section == 12) isSetCalender = false

                checkSection()
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

        checkSection()
    }

    fun checkSection() {
        // Button disabled
        if (!isSetCalender
            && !isSetEskul
            && !isSetPelajaran
        ) {
            btnSchoolScheduleEditDone.isEnabled = false
            btnSchoolScheduleEditDone.setBackgroundColor(
                Color.parseColor("#BDBDBD")
            )
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
