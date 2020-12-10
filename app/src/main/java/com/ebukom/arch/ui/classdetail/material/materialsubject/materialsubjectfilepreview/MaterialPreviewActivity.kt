package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfilepreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_material_preview.*
import kotlinx.android.synthetic.main.activity_register_parent.toolbar
import timber.log.Timber

class MaterialPreviewActivity : AppCompatActivity() {

    var fileId: String? = null
    var subjectId: String? = null
    var sectionId: String? = null
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_preview)

        initToolbar()

        fileId = intent?.extras?.getString("fileId")
        subjectId = intent?.extras?.getString("subjectId")
        sectionId = intent?.extras?.getString("sectionId")

        if (subjectId != null && sectionId != null && fileId != null) {

            db.collection("material_subjects").document(subjectId!!).collection("subject_sections")
                .document(sectionId!!).addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    tvToolbarTitle.text = value?.get("name") as String
                }

            db.collection("material_subjects").document(subjectId!!).collection("subject_sections")
                .document(sectionId!!).collection("files").document(fileId!!)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

//                ivMaterialPreview.setImageURI(value[""])
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