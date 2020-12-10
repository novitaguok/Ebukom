package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.activity_material_subject_add.*
import kotlinx.android.synthetic.main.activity_material_subject_recap.*
import kotlinx.android.synthetic.main.activity_material_subject_recap.toolbar
import kotlinx.android.synthetic.main.activity_school_photo_edit.*
import java.text.SimpleDateFormat
import java.util.*

class MaterialSubjectRecapActivity : AppCompatActivity() {
    var isSetLink = false
    var isSetDate = false
    lateinit var material: String
    var dateTime = ""
    val db = FirebaseFirestore.getInstance()
    var classId: String? = null
    var subjectId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject_recap)

        initToolbar()

        subjectId = intent?.extras?.getString("subjectId", subjectId)

        btnMaterialSubjectRecapDate.setOnClickListener {
            val dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Tanggal Pembelajaran Online",
                "SELESAI",
                "BATALKAN"
            )

            dateTimeDialogFragment.setTimeZone(TimeZone.getDefault())

            val dateFormat = SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault())

            dateTimeDialogFragment.startAtCalendarView()
            dateTimeDialogFragment.set24HoursMode(true)
            dateTimeDialogFragment.minimumDateTime =
                GregorianCalendar(2020, Calendar.JANUARY, 1).time

            try {
                dateTimeDialogFragment.simpleDateMonthAndDayFormat =
                    SimpleDateFormat("dd MMMM", Locale.getDefault())
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

        etMaterialSubjectRecapLink.addTextChangedListener(textWatcher)

        btnMaterialSubjectRecapDone.setOnClickListener {

            val data = hashMapOf<String, Any>(
//                "name" to etMaterialSubjectRecapLink.text.toString(),
                "link" to etMaterialSubjectRecapLink.text.toString(),
                "date" to Timestamp(Date())
            )

            db.collection("material_subjects").document(subjectId!!).collection("subject_sections")
                .add(data).addOnSuccessListener {
                    Log.d("TAG", "Material inserted successfully")
                }.addOnFailureListener {
                    Log.d("TAG", "Material failed to be inserted")
                }
//
//            db.collection("material_subjects").document(subjectId!!).collection("subject_sections")
//                .document()
//                .add(data).addOnSuccessListener {
//                    Log.d("TAG", "Material inserted successfully")
//                }.addOnFailureListener {
//                    Log.d("TAG", "Material failed to be inserted")
//                }

            finish()
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