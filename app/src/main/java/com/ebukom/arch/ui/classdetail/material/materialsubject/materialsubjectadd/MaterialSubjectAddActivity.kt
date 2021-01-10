package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_material_subject_add.*
import kotlinx.android.synthetic.main.activity_material_subject_add.rvMaterialSubjectAddAttachment
import kotlinx.android.synthetic.main.activity_material_subject_add.toolbar
import kotlinx.android.synthetic.main.activity_material_subject_add.tvToolbarTitle
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*
import kotlinx.android.synthetic.main.item_announcement_attachment.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class MaterialSubjectAddActivity : AppCompatActivity() {
    private var mFileList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    lateinit var mFileAdapter: ClassDetailAttachmentAdapter
    var isSetTitle = false
    var isSetFile = false
    var subjectId: String? = null
    var subjectName: String? = null
    var classId: String? = null
    var sectionId: String? = null
    var sectionName: String? = null
    var files = arrayListOf<ClassDetailAttachmentDao>()
    var action: String? = null
    val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject_add)

        initToolbar()
        checkSection()
        initRecycler()

        // Intent from another activities
        val layout = intent?.extras?.getString("layout")
        subjectId = intent?.extras?.getString("subjectId")
        classId = intent?.extras?.getString("classId")
        sectionId = intent?.extras?.getString("sectionId")
        subjectName = intent?.extras?.getString("subjectName")
        action = intent?.extras?.getString("action")


        /**
         * There are 2 kinds of layout for this activity:
         * create new education material and edit eduaction material
         */
        if (layout == "educationNew") {
            tvToolbarTitle.text = "Tambah Materi Mendidik Anak"
            btnMaterialSubjectAddDone.setOnClickListener {
                val data = hashMapOf<String, Any>(
                    "name" to etMaterialSubjectAddTitle.text.toString(),
                    "date" to Timestamp(Date())
                )
                loading.visibility = View.VISIBLE
                db.collection("material_education").add(data)
                    .addOnSuccessListener {
                        sectionId = it.id
                        mFileList.forEach {
                            val file = hashMapOf<String, Any>(
                                "title" to it.path,
                                "category" to it.category
                            )
                            db.collection("material_education").document(sectionId!!)
                                .collection("files").add(file)
                        }
                        loading.visibility = View.GONE
                        finish()
                    }.addOnFailureListener {
                        Log.d("TAG", "Material failed to be inserted")
                    }
            }
        } else if (layout == "educationEdit") {
            tvToolbarTitle.text = "Edit Materi Mendidik Anak"
            db.collection("material_education").document(sectionId!!).get().addOnSuccessListener {
                etMaterialSubjectAddTitle.setText(it["name"] as String)

                // Add file
                db.collection("material_education").document(sectionId!!).collection("files")
                    .get().addOnSuccessListener {
                        DataDummy.materialFileData.clear()
                        for (data in it!!.documents) {
                            DataDummy.materialFileData.add(
                                ClassDetailAttachmentDao(
                                    data["title"] as String,
                                    (data["category"] as Long).toInt()
                                )
                            )
                        }
                        mFileList.clear()
                        mFileList.addAll(DataDummy.materialFileData)
                        checkEmpty()
                    }.addOnFailureListener {
                        Timber.e(it)
                    }
            }.addOnFailureListener {
                Timber.e(it)
            }

            // Done
            btnMaterialSubjectAddDone.setOnClickListener {
                val data = hashMapOf<String, Any>(
                    "name" to etMaterialSubjectAddTitle.text.toString(),
                    "date" to Timestamp(Date())
                )
                loading.visibility = View.VISIBLE
                db.collection("material_education").document(sectionId!!).update(data)
                    .addOnSuccessListener {

                        /**
                         * Insert all file ID to array,
                         * in purpose to delete all documents in a collection,
                         * and re-add all files to collection
                         */
                        val collectionFiles =
                            db.collection("material_education").document(sectionId!!)
                                .collection("files")
                        deleteCollection(collectionFiles, 5) {
                            mFileList.forEach {
                                val file = hashMapOf<String, Any>(
                                    "title" to it.path,
                                    "category" to it.category
                                )
                                db.collection("material_education").document(sectionId!!)
                                    .collection("files").add(file)
                            }
                        }
                        loading.visibility = View.GONE
                        Log.d("FileInsert", "Material successfully inserted")
                        this@MaterialSubjectAddActivity.finish()
                    }.addOnFailureListener {
                    loading.visibility = View.GONE
                    Log.d("FileInsert", "Material failed to be inserted")
                    this@MaterialSubjectAddActivity.finish()
                }
            }
        } else if (layout == "subjectEdit") {
            db.collection("material_subjects").document(subjectId!!)
                .collection("subject_sections")
                .document(sectionId!!)
                .get()
                .addOnSuccessListener {
                    tvToolbarTitle.text = "Edit Materi " + it?.get("name") as String
                    etMaterialSubjectAddTitle.setText(it["name"] as String)

                    // Add file
                    db.collection("material_subjects").document(subjectId!!)
                        .collection("subject_sections")
                        .document(sectionId!!).collection("files")
                        .get().addOnSuccessListener {
                            DataDummy.materialFileData.clear()
                            for (data in it!!.documents) {
                                DataDummy.materialFileData.add(
                                    ClassDetailAttachmentDao(
                                        data["title"] as String,
                                        (data["category"] as Long).toInt()
                                    )
                                )
                            }
                            mFileList.clear()
                            mFileList.addAll(DataDummy.materialFileData)
                            checkEmpty()
                        }.addOnFailureListener {
                            Timber.e(it)
                        }

                }.addOnFailureListener {
                    Timber.e(it)
                }
            btnMaterialSubjectAddDone.setOnClickListener {
                val data = hashMapOf<String, Any>(
                    "name" to etMaterialSubjectAddTitle.text.toString(),
                    "date" to Timestamp(Date())
                )
                loading.visibility = View.VISIBLE
                db.collection("material_subjects").document(subjectId!!)
                    .collection("subject_sections")
                    .document(sectionId!!).update(data).addOnSuccessListener {

                        /**
                         * Insert all file ID to array,
                         * in purpose to delete all documents in a collection,
                         * and re-add all files to collection
                         */
                        val collectionFiles =
                            db.collection("material_subjects").document(subjectId!!)
                                .collection("subject_sections").document(sectionId!!)
                                .collection("files")
                        deleteCollection(collectionFiles, 5) {
                            mFileList.forEach {
                                val file = hashMapOf<String, Any>(
                                    "title" to it.path,
                                    "category" to it.category
                                )
                                db.collection("material_subjects")
                                    .document(subjectId!!)
                                    .collection("subject_sections")
                                    .document(sectionId!!)
                                    .collection("files")
                                    .add(file)
                            }
                        }

/*
                        val fileIds = arrayListOf<String>()
                        db.collection("material_subjects").document(subjectId!!)
                            .collection("subject_sections").document(sectionId!!)
                            .collection("files").get().addOnSuccessListener {
                                for (data in it.documents) {
                                    fileIds.add(data.id)
                                }

                                fileIds.forEach {
                                    db.collection("material_subjects").document(subjectId!!)
                                        .collection("subject_sections").document(sectionId!!)
                                        .collection("files").document(it).delete()
                                        .addOnSuccessListener {
                                            mFileList.forEach {
                                                val file = hashMapOf<String, Any>(
                                                    "title" to it.path,
                                                    "category" to it.category
                                                )
                                                db.collection("material_subjects")
                                                    .document(subjectId!!)
                                                    .collection("subject_sections")
                                                    .document(sectionId!!)
                                                    .collection("files")
                                                    .add(file)
                                            }
                                        }
                                }
                            }

*/
                        loading.visibility = View.GONE
                        Log.d("FileInsert", "Material successfully inserted")
                        this@MaterialSubjectAddActivity.finish()
                    }.addOnFailureListener {
                        loading.visibility = View.GONE
                        Log.d("FileInsert", "Material failed to be inserted")
                        this@MaterialSubjectAddActivity.finish()
                    }
            }
            checkEmpty()
        } else {
            if (subjectName == "Rekap Pembelajaran\nOnline") {
                subjectName = "Rekap Pembelajaran Online"
            }
            tvToolbarTitle.text = "Tambah Materi " + subjectName
            btnMaterialSubjectAddDone.setOnClickListener {
                val data = hashMapOf<String, Any>(
                    "name" to etMaterialSubjectAddTitle.text.toString(),
                    "date" to Timestamp(Date())
                )
                loading.visibility = View.VISIBLE
                db.collection("material_subjects").document(subjectId!!)
                    .collection("subject_sections")
                    .add(data).addOnSuccessListener {
                        sectionId = it.id
                        mFileList.forEach {
                            val file = hashMapOf<String, Any>(
                                "title" to it.path,
                                "category" to it.category
                            )
                            db.collection("material_subjects").document(subjectId!!)
                                .collection("subject_sections").document(sectionId!!)
                                .collection("files")
                                .add(file)
                        }
                        DataDummy.materialFileData.clear()
                        loading.visibility = View.GONE
                        finish()
                    }.addOnFailureListener {
                        loading.visibility = View.GONE
                        Log.d("TAG", "Material failed to be inserted")
                    }
            }
        }

        /**
         * File chooser
         */
        btnMaterialSubjectAddFile.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view =
                layoutInflater.inflate(R.layout.bottom_sheet_class_detail_attachment, null)
            view.clBottomSheetClassDetailAttachmentUseCamera.visibility = View.GONE
            view.tvClassDetailAttachmentPhoto.text = "Video"
            val icon: Drawable =
                view.tvClassDetailAttachmentPhoto.context.resources.getDrawable(R.drawable.ic_video_red)
            view.tvClassDetailAttachmentPhoto.setCompoundDrawablesWithIntrinsicBounds(
                icon,
                null,
                null,
                null
            )
            bottomSheetDialog.setContentView(view)

            view.clBottomClassDetailAttachmentPhoto.setOnClickListener {
                bottomSheetDialog.dismiss()
                val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
                fileIntent.type = "*/*"
                startActivityForResult(fileIntent, 10)
            }
            view.clBottomSheetClassDetailAttachmentFile.setOnClickListener {
                bottomSheetDialog.dismiss()
                val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
                fileIntent.type = "*/*"
                startActivityForResult(fileIntent, 11)
            }
            view.clBottomSheetClassDetailAttachmentLink.setOnClickListener {
                bottomSheetDialog.dismiss()
                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.alert_edit_text, null)

                view.tvAlertEditText.text = "Link"
                view.etAlertEditText.hint = "Masukkan link"

                bottomSheetDialog.dismiss()
                builder.setView(view)
                builder.setNegativeButton("BATALKAN") { dialog, which ->
                    dialog.dismiss()
                }
                builder.setPositiveButton("LAMPIRKAN") { dialog, which ->
                    val link = view.etAlertEditText?.text.toString()
                    DataDummy.materialFileData.add(ClassDetailAttachmentDao(link, 0))
                    insertFile(view, link)
//                    checkAttachmentEmpty()
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()

                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
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

            bottomSheetDialog.show()
        }

        /**
         * Text watcher
         */
        etMaterialSubjectAddTitle.addTextChangedListener(textWatcher)
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        DataDummy.materialFileData.clear()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val view = layoutInflater.inflate(R.layout.item_announcement_attachment, null)
        var path = data?.data?.path ?: ""
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                10 -> {
                    DataDummy.materialFileData.add(ClassDetailAttachmentDao(path, 1))
                    insertFile(view, path)
                }
                11 -> {
                    DataDummy.materialFileData.add(ClassDetailAttachmentDao(path, 2))
                    insertFile(view, path)
                }
                else -> {
                    val bp = (data?.extras?.get("data")) as Bitmap
//            blabla.setImageBitmap(bp)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initRecycler() {
        mFileList.clear()
        DataDummy.materialFileData.clear()
        mFileAdapter = ClassDetailAttachmentAdapter(mFileList)
        rvMaterialSubjectAddAttachment.apply {
            layoutManager = LinearLayoutManager(
                this@MaterialSubjectAddActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mFileAdapter
        }
        mFileAdapter.notifyDataSetChanged()
        checkEmpty()
    }

    private fun checkEmpty() {
        /**
         * Check if the recycler view is empty or not
         */
        if (mFileList.isNotEmpty()) {
            btnMaterialSubjectAddDone.isEnabled = true
            btnMaterialSubjectAddDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
            rvMaterialSubjectAddAttachment.visibility = View.VISIBLE
        } else {
            btnMaterialSubjectAddDone.isEnabled = false
            btnMaterialSubjectAddDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )
            rvMaterialSubjectAddAttachment.visibility = View.GONE
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etMaterialSubjectAddTitle.text.toString().isNotEmpty()) {
                isSetTitle = true
            } else {
                btnMaterialSubjectAddDone.setEnabled(false)
                btnMaterialSubjectAddDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }

    private fun checkSection() {
        if (!isSetTitle && !isSetFile) {
            btnMaterialSubjectAddDone.isEnabled = false
            btnMaterialSubjectAddDone.setBackgroundColor(
                Color.parseColor("#BDBDBD")
            )
        }
    }

    private fun insertFile(view: View, path: String) {
        mFileList.clear()
        mFileList.addAll(DataDummy.materialFileData)
        mFileAdapter.notifyDataSetChanged()
        view.tvItemAnnouncementAttachment?.text = path
        checkEmpty()
    }

    fun deleteAttachment(item: ClassDetailAttachmentDao) {
        val builder = android.app.AlertDialog.Builder(this@MaterialSubjectAddActivity)
        builder.setMessage("Apakah Anda yakin ingin menghapus file materi ini?")
        builder.setNegativeButton("BATALKAN") { dialog, which ->
            dialog.dismiss()
        }
        builder.setPositiveButton("HAPUS") { dialog, which ->
            DataDummy.materialFileData.remove(item)
            mFileList.remove(item)
            mFileAdapter.notifyDataSetChanged()

            checkEmpty()
        }
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
        val positiveButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        positiveButton.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorGray
            )
        )
        val negativeButton = dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorRed
            )
        )
    }

    fun deleteCollection(collection: CollectionReference, batchSize: Int, nextAction: () -> Unit) {
        try {
            // Retrieve a small batch of documents to avoid out-of-memory errors/
            var deleted = 0
            collection
                .limit(batchSize.toLong())
                .get()
                .addOnCompleteListener {
                    for (document in it.result!!.documents) {
                        document.getReference().delete()
                        ++deleted
                    }
                    if (deleted >= batchSize) {
                        // retrieve and delete another batch
                        deleteCollection(collection, batchSize, nextAction)
                    } else {
                        nextAction()
                    }
                }
        } catch (e: Exception) {
            System.err.println("Error deleting collection : " + e.message)
        }
    }
}