package com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotonew

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_photo_new.*
import kotlinx.android.synthetic.main.activity_school_photo_new.loading
import kotlinx.android.synthetic.main.activity_school_photo_new.toolbar

class SchoolPhotoNewActivity : AppCompatActivity() {

    var classId: String? = null
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_photo_new)

        initToolbar()

        /**
         * Get intent from SchoolPhotoActivity
         */
        classId = intent?.extras?.getString("classId")

        /**
         * Text watcher
         */
        etSchoolPhotoNewTitle.addTextChangedListener(textWatcher)
        etSchoolPhotoNewLink.addTextChangedListener(textWatcher)

        /**
         * Save button
         */
        btnSchoolPhotoNewDone.setOnClickListener {
            var title = etSchoolPhotoNewTitle.text.toString()
            var link = etSchoolPhotoNewLink.text.toString()
            val data = hashMapOf(
                "title" to title,
                "link" to link
            )

            loading.visibility = View.VISIBLE
            db.collection("classes").document(classId!!).collection("photos").add(data).addOnSuccessListener {
                Log.d("TAG", "Photo uploaded successfully")
                loading.visibility = View.GONE
                finish()
            }.addOnFailureListener {
                Log.d("TAG", "Photo failed to be uploaded")
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

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etSchoolPhotoNewTitle.text.toString()
                    .isNotEmpty() && etSchoolPhotoNewLink.text.toString()
                    .isNotEmpty()
            ) {
                btnSchoolPhotoNewDone.isEnabled = true
                btnSchoolPhotoNewDone.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnSchoolPhotoNewDone.isEnabled = false
                btnSchoolPhotoNewDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }
}
