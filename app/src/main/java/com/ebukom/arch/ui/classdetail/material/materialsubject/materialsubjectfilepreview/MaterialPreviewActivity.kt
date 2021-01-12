package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfilepreview

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ebukom.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_material_preview.*
import kotlinx.android.synthetic.main.activity_register_parent.toolbar
import timber.log.Timber

class MaterialPreviewActivity : AppCompatActivity() {

    var fileId: String? = null
    var subjectId: String? = null
    var sectionId: String? = null
    var filePath: String? = null
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_preview)

        initToolbar()

//        val layout = intent?.extras?.getString("layout")
        fileId = intent?.extras?.getString("fileId")
        subjectId = intent?.extras?.getString("subjectId")
        sectionId = intent?.extras?.getString("sectionId")

        // Intent from SchoolAnnouncementDetailActivity
        filePath = intent?.extras?.getString("filePath")

        if (!filePath.isNullOrEmpty()) {
            appbar.visibility = View.GONE
//            clMaterialPreview.setBackgroundColor(Color.parseColor("#ffff0000"))

//            val gsRef = storage.getReferenceFromUrl(filePath!!)
            Glide.with(this)
                .load(filePath)
                .centerCrop()
                .into(ivMaterialPreview)
        } else {
            ivMaterialPreviewClose.visibility = View.GONE
        }



        if (subjectId == "") {
            if (sectionId != null) {
                db.collection("material_education").document(sectionId!!)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            Timber.e(error)
                            return@addSnapshotListener
                        }

                        tvToolbarTitle.text = value?.get("name") as String

                        if (fileId != null) {
                            db.collection("material_education").document(sectionId!!)
                                .collection("files")
                                .document(fileId!!)
                                .addSnapshotListener { value, error ->
                                    if (error != null) {
                                        Timber.e(error)
                                        return@addSnapshotListener
                                    }

//                ivMaterialPreview.setImageURI(value[""])
                                }
                        }
                    }
            }
        } else {
            if (subjectId != null && sectionId != null) {
                db.collection("material_subjects").document(subjectId!!)
                    .collection("subject_sections")
                    .document(sectionId!!).addSnapshotListener { value, error ->
                        if (error != null) {
                            Timber.e(error)
                            return@addSnapshotListener
                        }

                        tvToolbarTitle.text = value?.get("name") as String

                        if (fileId != null) {
                            db.collection("material_subjects").document(subjectId!!)
                                .collection("subject_sections")
                                .document(sectionId!!).collection("files").document(fileId!!)
                                .addSnapshotListener { value, error ->
                                    if (error != null) {
                                        Timber.e(error)
                                        return@addSnapshotListener
                                    }

//                ivMaterialPreview.setImageURI(value[""])
                                }
                        }
                    }
            }
        }

        btnMaterialPreviewSave.setOnClickListener {
            val builder = AlertDialog.Builder(this@MaterialPreviewActivity)

            builder.setMessage("File berhasil disimpan")
            builder.setPositiveButton("OK", null)

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
            positiveButton.setOnClickListener {
                finish()
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