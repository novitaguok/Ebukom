package com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulenew

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailScheduleDao
import com.ebukom.data.DataDummy
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_school_schedule_new.*
import timber.log.Timber
import java.io.File


class SchoolScheduleNewActivity : AppCompatActivity() {
    lateinit var storageReference: StorageReference
    lateinit var fileName: String
    private var isSetPelajaran = false
    private var isSetEskul = false
    private var isSetCalendar = false
    private var filePath: String? = null
    var classId: String? = null
    var className: String? = null
    val db = FirebaseFirestore.getInstance()
    var storage = Firebase.storage
    var academic = ""
    var eskul = ""
    var calendar = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_schedule_new)

        initToolbar()
        DataDummy.scheduleData.clear()

        classId = intent?.extras?.getString("classId")
        db.collection("classes").document(classId!!).get().addOnSuccessListener {
            className = it["class_name"] as String
        }

        // File chooser
        btnSchoolScheduleNewSubject.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "application/*"
            startActivityForResult(fileIntent, 10)
        }
        btnSchoolScheduleNewEskul.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "application/*"
            startActivityForResult(fileIntent, 11)
        }
        btnSchoolScheduleNewCalendar.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "application/*"
            startActivityForResult(fileIntent, 12)
        }

        // Insert data to firestore
        btnSchoolScheduleNewDone.setOnClickListener {
            if (academic != "") {
                storageReference = FirebaseStorage.getInstance()
                    .getReference("files/schedule/academic/${className}")
                storageReference.putFile(Uri.parse(academic)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        storageReference.downloadUrl.addOnSuccessListener {
                            if (task.isSuccessful) {
                                academic = it.toString()
                                db.collection("classes").document(classId!!).get()
                                    .addOnSuccessListener {
                                        if (it["schedules_academic"] as String? != null || it["schedules_academic"] as String? != "") {
                                            db.collection("classes").document(classId!!)
                                                .set(hashMapOf("schedules_academic" to academic), SetOptions.merge())
                                                .addOnSuccessListener {
                                                    Log.d(
                                                        "Schedule",
                                                        "Academic schedule inserted successfully"
                                                    )
                                                    academic = ""
                                                    addScheduleDone()
                                                }
                                        } else {
                                            db.collection("classes").document(classId!!)
                                                .update(hashMapOf<String, Any>("schedules_academic" to academic))
                                                .addOnSuccessListener {
                                                    Log.d(
                                                        "Schedule",
                                                        "Academic schedule inserted successfully"
                                                    )
                                                    academic = ""
                                                    addScheduleDone()
                                                }
                                        }
                                    }
                            } else {
                                storageReference.delete()
                            }
                        }
                    }
                }
            }

            if (eskul != "") {
                storageReference = FirebaseStorage.getInstance()
                    .getReference("files/schedule/eskul/${className}")
                storageReference.putFile(Uri.parse(eskul)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        storageReference.downloadUrl.addOnSuccessListener {
                            if (task.isSuccessful) {
                                eskul = it.toString()
                                db.collection("classes").document(classId!!).get()
                                    .addOnSuccessListener {
                                        if (it["schedules_eskul"] as String? != null || it["schedules_eskul"] as String? != "") {
                                            db.collection("classes").document(classId!!)
                                                .set(hashMapOf("schedules_eskul" to eskul), SetOptions.merge())
                                                .addOnSuccessListener {
                                                    Log.d(
                                                        "Schedule",
                                                        "Eskul schedule inserted successfully"
                                                    )
                                                    eskul = ""
                                                    addScheduleDone()
                                                }
                                        } else {
                                            db.collection("classes").document(classId!!)
                                                .update(hashMapOf<String, Any>("schedules_eskul" to eskul))
                                                .addOnSuccessListener {
                                                    Log.d(
                                                        "Schedule",
                                                        "Eskul schedule inserted successfully"
                                                    )
                                                    eskul = ""
                                                    addScheduleDone()
                                                }
                                        }
                                    }
                            } else {
                                storageReference.delete()
                            }
                        }
                    }
                }
            }

            if (calendar != "") {
                storageReference = FirebaseStorage.getInstance()
                    .getReference("files/schedule/calendar/${className}")
                storageReference.putFile(Uri.parse(calendar)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        storageReference.downloadUrl.addOnSuccessListener {
                            if (task.isSuccessful) {
                                calendar = it.toString()
                                db.collection("classes").document(classId!!).get()
                                    .addOnSuccessListener {
                                        if (it["schedules.calendar"] as String? != null || it["schedules_calendar"] as String? != "") {
                                            db.collection("classes").document(classId!!)
                                                .set(hashMapOf("schedules_calendar" to calendar), SetOptions.merge())
                                                .addOnSuccessListener {
                                                    Log.d(
                                                        "Schedule",
                                                        "Schedule calendar inserted successfully"
                                                    )
                                                    calendar = ""
                                                    addScheduleDone()
                                                }
                                        } else {
                                            db.collection("classes").document(classId!!)
                                                .update(hashMapOf<String, Any>("schedules_calendar" to calendar))
                                                .addOnSuccessListener {
                                                    Log.d(
                                                        "Schedule",
                                                        "Schedule calendar inserted successfully"
                                                    )
                                                    calendar = ""
                                                    addScheduleDone()
                                                }
                                        }
                                    }
                            } else {
                                storageReference.delete()
                            }
                        }
                    }
                }
            }
        }

        checkSection()
    }

    private fun addScheduleDone() {
        if (academic == "" && eskul == "" && calendar == "") {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fileName = data?.data!!.path.toString()
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                10 -> { // academic
                    academic = data?.data!!.toString() // URI
                    newSchedule(
                        academic!!,
                        requestCode,
                        "academic",
                        fileName.substringAfterLast("/")
                    )
                }
                11 -> { // eskul
                    eskul = data?.data!!.toString() // URI
                    newSchedule(eskul!!, requestCode, "eskul", fileName.substringAfterLast("/"))
                }
                12 -> { // calendar
                    calendar = data?.data!!.toString() // URI
                    newSchedule(
                        calendar!!,
                        requestCode,
                        "calendar",
                        fileName.substringAfterLast("/")
                    )
                }
                else -> {
                    btnSchoolScheduleNewDone.isEnabled = false
                    btnSchoolScheduleNewDone.setBackgroundColor(
                        Color.parseColor("#BDBDBD")
                    )
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun newSchedule(path: String, section: Int = 0, type: String, fileName: String) {
        var btn: Button? = findViewById(R.id.btnSchoolScheduleNewSubject)
        var tv: TextView? = findViewById(R.id.tvSchoolScheduleNewSubjectPath)
        var iv: ImageView? = findViewById(R.id.ivSchoolScheduleNewSubjectDelete)
        var alert: String? = ""

        if (section == 10) {
            btn = findViewById(R.id.btnSchoolScheduleNewSubject)
            tv = findViewById(R.id.tvSchoolScheduleNewSubjectPath)
            iv = findViewById(R.id.ivSchoolScheduleNewSubjectDelete)
            alert = "jadwal"
//            DataDummy.scheduleData.add(ClassDetailScheduleDao(path, type, fileName))
            isSetPelajaran = true
        } else if (section == 11) {
            btn = findViewById(R.id.btnSchoolScheduleNewEskul)
            tv = findViewById(R.id.tvSchoolScheduleNewEskulPath)
            iv = findViewById(R.id.ivSchoolScheduleNewEskulDelete)
            alert = "jadwal"
//            DataDummy.scheduleData.add(ClassDetailScheduleDao(path, type, fileName))
            isSetEskul = true
        } else if (section == 12) {
            btn = findViewById(R.id.btnSchoolScheduleNewCalendar)
            tv = findViewById(R.id.tvSchoolScheduleNewCalendarPath)
            iv = findViewById(R.id.ivSchoolScheduleNewCalendarDelete)
            alert = "kalender"
//            DataDummy.scheduleData.add(ClassDetailScheduleDao(path, type, fileName))
            isSetCalendar = true
        }

        tv?.text = fileName
        tv?.visibility = View.VISIBLE
        iv?.visibility = View.VISIBLE
        btn?.text = "UBAH FILE"
        btn?.background =
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.btn_red_rectangle
            )

        btnSchoolScheduleNewDone.isEnabled = true
        btnSchoolScheduleNewDone.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorSuperDarkBlue
            )
        )

        // Delete schedule
        iv?.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolScheduleNewActivity)
            builder.setMessage("Apakah Anda yakin ingin menghapus $alert ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
//                DataDummy.scheduleData.remove(item)
//                mList.remove(item)
//                mAdapter.addAll(mList)

                alert = alert?.toUpperCase()
                btn?.setText("PILIH FILE $alert")
                btn?.background =
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.btn_blue_rectangle_8dp
                    )
                tv?.visibility = View.INVISIBLE
                iv?.visibility = View.GONE

                if (section == 10) isSetPelajaran = false
                else if (section == 11) isSetEskul = false
                else if (section == 12) isSetCalendar = false

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
        if (!isSetCalendar
            && !isSetEskul
            && !isSetPelajaran
        ) {
            btnSchoolScheduleNewDone.isEnabled = false
            btnSchoolScheduleNewDone.setBackgroundColor(
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
