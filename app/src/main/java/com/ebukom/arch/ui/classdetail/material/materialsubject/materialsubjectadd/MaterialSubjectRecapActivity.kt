package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfile.MaterialSubjectFileAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.activity_material_subject_recap.*
import kotlinx.android.synthetic.main.activity_material_subject_recap.toolbar
import java.text.SimpleDateFormat
import java.util.*

class MaterialSubjectRecapActivity : AppCompatActivity() {
    private val mFileList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    lateinit var mFileAdapter: MaterialSubjectFileAdapter
    var isSetLink = false
    var isSetDate = false
    lateinit var material: String
    var dateTime = ""
    val db = FirebaseFirestore.getInstance()
    var classId: String? = null
    var sectionId: String? = null
    var subjectId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject_recap)

        initToolbar()

        // Intent from another activity
        val layout = intent?.extras?.getString("layout")
        subjectId = intent?.extras?.getString("subjectId", subjectId)
        sectionId = intent?.extras?.getString("sectionId")

        // Set date
        btnMaterialSubjectRecapDate.setOnClickListener {
            val dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Tanggal Pembelajaran Online",
                "SELESAI",
                "BATALKAN"
            )
            val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("in", "ID"))
            dateTimeDialogFragment.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"))
            dateTimeDialogFragment.startAtCalendarView()
            dateTimeDialogFragment.set24HoursMode(true)
            dateTimeDialogFragment.minimumDateTime =
                GregorianCalendar(2020, Calendar.JANUARY, 1).time

            try {
                dateTimeDialogFragment.simpleDateMonthAndDayFormat =
//                    SimpleDateFormat("dd MMMM", Locale.getDefault())
                    SimpleDateFormat("EEE, d MMMM yyyy", Locale("in", "ID"))
            } catch (e: SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException) {
                Log.e("error", e.message)
            }


            dateTimeDialogFragment.setOnButtonClickListener(object :
                SwitchDateTimeDialogFragment.OnButtonClickListener {
                override fun onPositiveButtonClick(date: Date?) {
                    btnMaterialSubjectRecapDate.text = dateFormat.format(date)
                    dateTime = dateFormat.format(date)
                    isSetDate = true
                }

                override fun onNegativeButtonClick(date: Date?) {}
            })

            dateTimeDialogFragment.show(supportFragmentManager, "")
        }

        // Enable button
        if (etMaterialSubjectRecapLink.text.toString().isNotEmpty() && isSetLink && isSetDate) {
            btnMaterialSubjectRecapDone.isEnabled = true
            btnMaterialSubjectRecapDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        } else {
            btnMaterialSubjectRecapDone.isEnabled = false
            btnMaterialSubjectRecapDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )
        }

        // Text watcher
        etMaterialSubjectRecapLink.addTextChangedListener(textWatcher)

        // Insert to database
        if (layout == "recapEdit") {
//            btnMaterialSubjectRecapDone.setOnClickListener {
//                mFileList.add(
//                    ClassDetailAttachmentDao(
//                        etMaterialSubjectRecapLink.text.toString(),
//                        0
//                    )
//                )
//
//                val data = hashMapOf<String, Any>(
////                "files" to mFileList,
//                    "name" to btnMaterialSubjectRecapDate.text,
//                    "date" to Timestamp(Date())
//                )
//
//                loading.visibility = View.VISIBLE
//                val file = hashMapOf<String, Any>(
//                    "title" to mFileList[0].path,
//                    "category" to mFileList[0].category
//                )
//                db.collection("material_subjects").document(subjectId!!)
//                    .collection("subject_sections")
//                    .add(data).addOnSuccessListener {
//                        sectionId = it.id
//                        db.collection("material_subjects").document(subjectId!!)
//                            .collection("subject_sections").document(sectionId!!).collection("files")
//                            .add(file)
//                        Log.d("Recap", "Material successfully inserted")
//                    }.addOnFailureListener {
//                        Log.d("Recap", "Material failed to be inserted")
//                    }
//                finish()
//            }
        } else {
            btnMaterialSubjectRecapDone.setOnClickListener {
                mFileList.add(
                    ClassDetailAttachmentDao(
                        etMaterialSubjectRecapLink.text.toString(),
                        0
                    )
                )

                val data = hashMapOf<String, Any>(
//                "files" to mFileList,
                    "name" to btnMaterialSubjectRecapDate.text,
                    "date" to Timestamp(Date())
                )

                loading.visibility = View.VISIBLE
                val file = hashMapOf<String, Any>(
                    "title" to mFileList[0].fileName,
                    "category" to mFileList[0].category
                )
                db.collection("material_subjects").document(subjectId!!)
                    .collection("subject_sections")
                    .add(data).addOnSuccessListener {
                        sectionId = it.id
                        db.collection("material_subjects").document(subjectId!!)
                            .collection("subject_sections").document(sectionId!!)
                            .collection("files")
                            .add(file)
                        Log.d("Recap", "Material successfully inserted")
                    }.addOnFailureListener {
                        Log.d("Recap", "Material failed to be inserted")
                    }
                finish()
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etMaterialSubjectRecapLink.text.toString().isNotEmpty()) {
                isSetLink = true

                if (isSetDate) {
                    btnMaterialSubjectRecapDone.isEnabled = true
                    btnMaterialSubjectRecapDone.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorSuperDarkBlue
                        )
                    )
                } else {
                    btnMaterialSubjectRecapDone.isEnabled = false
                    btnMaterialSubjectRecapDone.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorGray
                        )
                    )
                }
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