package com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotoedit

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPhotoDao
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoAdapter
import com.ebukom.data.DataDummy
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_announcement_edit.*
import kotlinx.android.synthetic.main.activity_school_photo_edit.*
import kotlinx.android.synthetic.main.activity_school_photo_edit.loading
import kotlinx.android.synthetic.main.activity_school_photo_new.toolbar
import timber.log.Timber

class SchoolPhotoEditActivity : AppCompatActivity() {

    private val mPhotoList: ArrayList<ClassDetailPhotoDao> = DataDummy.photoData
    lateinit var mPhotoAdapter : SchoolPhotoAdapter
    val db = FirebaseFirestore.getInstance()
    var classId: String? = null
    var photoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_photo_edit)

        initToolbar()

        /**
         * Intent from SchoolPhotoActivity
         */
        classId = intent?.extras?.getString("classId")
        photoId = intent?.extras?.getString("photoId")

        db.collection("classes").document(classId!!).collection("photos").document(photoId!!).addSnapshotListener { value, error ->
            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            etSchoolPhotoEditTitle.setText(value?.get("title") as String)
            etSchoolPhotoEditLink.setText(value?.get("link") as String)
        }

        /**
         * Text watcher
         */
        etSchoolPhotoEditTitle.addTextChangedListener(textWatcher)
        etSchoolPhotoEditLink.addTextChangedListener(textWatcher)

        /**
         * Save edited photo information
         */
        btnSchoolPhotoEditSave.setOnClickListener {
            val data = hashMapOf<String, Any>(
                "title" to etSchoolPhotoEditTitle.text.toString(),
                "link" to etSchoolPhotoEditLink.text.toString()
            )

            loading.visibility = View.VISIBLE
            db.collection("classes").document(classId!!).collection("photos")
                .document(photoId!!)
                .update(data).addOnSuccessListener {
                    Log.d("TAG", "data edited successfully")
                    loading.visibility = View.GONE
                    finish()
                }.addOnFailureListener {
                    Log.d("TAG", "data edit failed")
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
            if (etSchoolPhotoEditTitle.text.toString()
                    .isNotEmpty() && etSchoolPhotoEditLink.text.toString()
                    .isNotEmpty()
            ) {
                btnSchoolPhotoEditSave.isEnabled = true
                btnSchoolPhotoEditSave.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnSchoolPhotoEditSave.isEnabled = false
                btnSchoolPhotoEditSave.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }
}
