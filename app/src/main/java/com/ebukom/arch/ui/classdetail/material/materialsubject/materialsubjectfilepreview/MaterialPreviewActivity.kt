package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfilepreview

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.MediaController
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ebukom.R
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_material_preview.*
import kotlinx.android.synthetic.main.activity_register_parent.toolbar
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MaterialPreviewActivity : AppCompatActivity() {

    var fileId: String? = null
    var subjectId: String? = null
    var sectionId: String? = null
    var filePath: String? = null
    var fileName: String? = null
    var activity: String? = null
    var category: Int = 0
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_preview)

        initToolbar()

        fileId = intent?.extras?.getString("fileId")
        subjectId = intent?.extras?.getString("subjectId")
        sectionId = intent?.extras?.getString("sectionId")
        filePath = intent?.extras?.getString("filePath")
        fileName = intent?.extras?.getString("fileName")
        activity = intent?.extras?.getString("activity")
        category = intent?.extras?.getInt("category")!!

        if (activity == "announcement") {
            appbar.visibility = View.GONE
            clMaterialPreview.setBackgroundColor(Color.parseColor("#80000000"))
            if (category == 1) {
                ivMaterialPreviewClose.visibility = View.VISIBLE
                pdfView?.visibility = View.GONE
                ivMaterialPreview.visibility = View.VISIBLE
                vvMaterialPreview.visibility = View.GONE
                Glide.with(this)
                    .load(filePath)
                    .centerCrop()
                    .into(ivMaterialPreview)
            } else if (category == 2) {
//                pdfView.visibility = View.VISIBLE
                ivMaterialPreviewClose.visibility = View.GONE
                ivMaterialPreview.visibility = View.GONE
                vvMaterialPreview.visibility = View.GONE
//                RetrivePDFfromUrl().execute(filePath)

                val file = File(Environment.getExternalStorageDirectory().absolutePath)
                val target = Intent(Intent.ACTION_VIEW)
                target.setDataAndType(Uri.parse(filePath), "application/*")
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                val intent = Intent.createChooser(target, "Open File")
                startActivity(intent)
            }
        } else {
            ivMaterialPreviewClose.visibility = View.GONE
            if (category == 1) {
                val mediaController = MediaController(this)
                mediaController.setAnchorView(vvMaterialPreview)
                pdfView?.visibility = View.GONE
                ivMaterialPreview.visibility = View.GONE
                vvMaterialPreview.visibility = View.VISIBLE
                vvMaterialPreview.setMediaController(mediaController)
                vvMaterialPreview.setVideoURI(Uri.parse(filePath))
                vvMaterialPreview.start()
            } else if (category == 2) {
//                pdfView.visibility = View.VISIBLE
                ivMaterialPreview.visibility = View.GONE
                vvMaterialPreview.visibility = View.GONE
//                RetrivePDFfromUrl().execute(filePath)
            }
        }

        if (subjectId == "") { // education
            if (sectionId != null) {
                db.collection("material_education").document(sectionId!!)
                    .get().addOnSuccessListener{
                        tvToolbarTitle.text = it.get("name") as String
                        if (fileId != null) {
                            db.collection("material_education").document(sectionId!!)
                                .collection("files")
                                .document(fileId!!).get()
                        }
                    }
            }
        } else { // subject
            if (subjectId != null && sectionId != null) {
                db.collection("material_subjects").document(subjectId!!)
                    .collection("subject_sections")
                    .document(sectionId!!).get().addOnSuccessListener {
                        tvToolbarTitle.text = it?.get("name") as String
                        if (fileId != null) {
                            db.collection("material_subjects").document(subjectId!!)
                                .collection("subject_sections")
                                .document(sectionId!!).collection("files").document(fileId!!)
                                .get()
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

    // For PDF viewer
    inner class RetrivePDFfromUrl :
        AsyncTask<String?, Void?, InputStream?>() {
        override fun doInBackground(vararg strings: String?): InputStream? {
            // Inputstream for getting out PDF
            var inputStream: InputStream? = null
            try {
                val url = URL(strings[0])
                // Creating connection
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
                if (urlConnection.getResponseCode() === 200) {
                    /**
                     * Response success, then get the input stream from url
                     * and store it to variable
                     */
                    inputStream = BufferedInputStream(urlConnection.getInputStream())
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            return inputStream
        }

        override fun onPostExecute(inputStream: InputStream?) {
            // After async, load pdf in pdf viewer
            pdfView.fromStream(inputStream).enableSwipe(true).enableDoubletap(true)
                .scrollHandle(DefaultScrollHandle(this@MaterialPreviewActivity)).load()
        }
    }
}