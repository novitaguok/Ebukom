package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_material_subject_add.*
import kotlinx.android.synthetic.main.activity_material_subject_add.rvMaterialSubjectAddAttachment
import kotlinx.android.synthetic.main.activity_material_subject_add.toolbar
import kotlinx.android.synthetic.main.activity_material_subject_add.tvToolbarTitle
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*
import kotlinx.android.synthetic.main.item_attachment.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class MaterialSubjectAddActivity : AppCompatActivity() {
    private var mFileList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    lateinit var mFileAdapter: ClassDetailAttachmentAdapter
    lateinit var fileName: String
    lateinit var storageReference: StorageReference
    var isSetTitle = false
    var isSetFile = false
    var subjectId: String? = null
    var subjectName: String? = null
    var classId: String? = null
    var sectionId: String? = null
    var savedImageUri = arrayListOf<String>()
    var counter = 0
    var filePath: String? = null
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
                loading.visibility = View.VISIBLE
                for (i in 0..(mFileList.size - 1)) {
                    if (mFileList[i].category == 1) {
                        storageReference = FirebaseStorage.getInstance()
                            .getReference("videos/material/education/${mFileList[i].fileName}")
                    } else if (mFileList[i].category == 2) {
                        storageReference =
                            FirebaseStorage.getInstance().reference.child("files/material/education/${mFileList[i].fileName}")
                    } else {

                    }

                    // Upload and get the download URL
                    storageReference.putFile(Uri.parse(mFileList[i].path))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                storageReference.downloadUrl.addOnSuccessListener {
                                    counter++
                                    if (task.isSuccessful) {
                                        savedImageUri.add(it.toString())
                                    } else {
                                        storageReference.delete()
                                        Toast.makeText(this, "Couldn't save " + mFileList[i].fileName, Toast.LENGTH_LONG).show()
                                    }
                                    if (counter == mFileList.size) {
                                        for (i in 0..mFileList.size - 1) mFileList[i].path = savedImageUri[i]

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
                                                        "title" to it.fileName,
                                                        "category" to it.category,
                                                        "path" to it.path!!
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
                                }
                            } else {
                                counter++
                            }
                        }
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
                                    data["path"] as String,
                                    (data["category"] as Long).toInt(),
                                    "",
                                    "",
                                    "",
                                    "",
                                    data["title"] as String
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
                loading.visibility = View.VISIBLE
                for (i in 0..(mFileList.size - 1)) {
                    if (mFileList[i].category == 1) {
                        storageReference = FirebaseStorage.getInstance()
                            .getReference("videos/material/education/${mFileList[i].fileName}")
                    } else if (mFileList[i].category == 2) {
                        storageReference =
                            FirebaseStorage.getInstance().reference.child("files/material/education/${mFileList[i].fileName}")
                    } else {

                    }

                    // Upload and get the download URL
                    storageReference.putFile(Uri.parse(mFileList[i].path))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                storageReference.downloadUrl.addOnSuccessListener {
                                    counter++
                                    if (task.isSuccessful) {
                                        savedImageUri.add(it.toString())
                                    } else {
                                        storageReference.delete()
                                        Toast.makeText(this, "Couldn't save " + mFileList[i].fileName, Toast.LENGTH_LONG).show()
                                    }
                                    if (counter == mFileList.size) {
                                        for (i in 0..mFileList.size - 1) mFileList[i].path = savedImageUri[i]
                                        val data = hashMapOf<String, Any>(
                                            "name" to etMaterialSubjectAddTitle.text.toString(),
                                            "date" to Timestamp(Date())
                                        )

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
                                                            "title" to it.fileName,
                                                            "category" to it.category,
                                                            "path" to it.path!!
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
                                }
                            } else {
                                counter++
                            }
                        }
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
                                        data["path"] as String,
                                        (data["category"] as Long).toInt(),
                                        "",
                                        "",
                                        "",
                                        "",
                                        data["title"] as String
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
                loading.visibility = View.VISIBLE

                for (i in 0..(mFileList.size - 1)) {
                    if (mFileList[i].category == 1) {
                        storageReference = FirebaseStorage.getInstance()
                            .getReference("videos/material/subject/${mFileList[i].fileName}")
                    } else if (mFileList[i].category == 2) {
                        storageReference =
                            FirebaseStorage.getInstance().reference.child("files/material/subject/${mFileList[i].fileName}")
                    } else {

                    }

                    // Upload and get the download URL
                    storageReference.putFile(Uri.parse(mFileList[i].path))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                storageReference.downloadUrl.addOnSuccessListener {
                                    counter++
                                    if (task.isSuccessful) {
                                        savedImageUri.add(it.toString())
                                    } else {
                                        storageReference.delete()
                                        Toast.makeText(this, "Couldn't save " + mFileList[i].fileName, Toast.LENGTH_LONG).show()
                                    }
                                    if (counter == mFileList.size) {
                                        for (i in 0..mFileList.size - 1) mFileList[i].path = savedImageUri[i]

                                        val data = hashMapOf<String, Any>(
                                            "name" to etMaterialSubjectAddTitle.text.toString(),
                                            "date" to Timestamp(Date())
                                        )
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
                                                            "title" to it.fileName,
                                                            "category" to it.category,
                                                            "path" to it.path!!
                                                        )
                                                        db.collection("material_subjects")
                                                            .document(subjectId!!)
                                                            .collection("subject_sections")
                                                            .document(sectionId!!)
                                                            .collection("files")
                                                            .add(file)
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
                                }
                            } else {
                                counter++
                            }
                        }
                }
            }
            checkEmpty()
        } else {
            if (subjectName == "Rekap Pembelajaran\nOnline") {
                subjectName = "Rekap Pembelajaran Online"
            }
            tvToolbarTitle.text = "Tambah Materi " + subjectName

            // Done
            btnMaterialSubjectAddDone.setOnClickListener {
                loading.visibility = View.VISIBLE

                for (i in 0..(mFileList.size - 1)) {
                    if (mFileList[i].category == 1) {
                        storageReference = FirebaseStorage.getInstance()
                            .getReference("videos/material/subject/${mFileList[i].fileName}")
                    } else if (mFileList[i].category == 2) {
                        storageReference =
                            FirebaseStorage.getInstance().reference.child("files/material/subject/${mFileList[i].fileName}")
                    } else {

                    }

                    // Upload and get the download URL
                    storageReference.putFile(Uri.parse(mFileList[i].path))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                storageReference.downloadUrl.addOnSuccessListener {
                                    counter++
                                    if (task.isSuccessful) {
                                        savedImageUri.add(it.toString())
                                    } else {
                                        storageReference.delete()
                                        Toast.makeText(this, "Couldn't save " + mFileList[i].fileName, Toast.LENGTH_LONG).show()
                                    }
                                    if (counter == mFileList.size) {
                                        for (i in 0..mFileList.size - 1) mFileList[i].path = savedImageUri[i]

                                        val data = hashMapOf<String, Any>(
                                            "name" to etMaterialSubjectAddTitle.text.toString(),
                                            "date" to Timestamp(Date())
                                        )
                                        db.collection("material_subjects").document(subjectId!!)
                                            .collection("subject_sections")
                                            .add(data).addOnSuccessListener {
                                                sectionId = it.id
                                                mFileList.forEach {
                                                    val file = hashMapOf<String, Any>(
                                                        "title" to it.fileName,
                                                        "category" to it.category,
                                                        "path" to it.path!!
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
                            } else {
                                counter++
                            }
                        }
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
                fileIntent.type = "video/*"
                startActivityForResult(fileIntent, 10)
            }
            view.clBottomSheetClassDetailAttachmentFile.setOnClickListener {
                bottomSheetDialog.dismiss()
                val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
                fileIntent.type = "application/*"
                startActivityForResult(fileIntent, 11)
            }
            view.clBottomSheetClassDetailAttachmentLink.setOnClickListener {
                bottomSheetDialog.dismiss()
                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.alert_edit_text, null)

                view.tvAlertEditText.text = "Link"
//                view.etAlertEditText.hint = "Masukkan link"
                view.tilAlertEditText.hint = "Masukkan link"

                bottomSheetDialog.dismiss()
                builder.setView(view)
                builder.setNegativeButton("BATALKAN") { dialog, which ->
                    dialog.dismiss()
                }
                builder.setPositiveButton("LAMPIRKAN") { dialog, which ->
                    val link = view.etAlertEditText?.text.toString()
                    DataDummy.materialFileData.add(ClassDetailAttachmentDao(link, 0))
                    insertFile()
                    checkEmpty()
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
        fileName = data?.data!!.path.toString()
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                10 -> {
                    filePath = data.data!!.toString() // URI
                    DataDummy.materialFileData.add(
                        ClassDetailAttachmentDao(
                            filePath,
                            1,
                            "",
                            "",
                            "",
                            "",
                            fileName.substringAfterLast("/")
                        )
                    )
                    insertFile()
                }
                11 -> {
                    filePath = data.data!!.toString() // URI
                    DataDummy.materialFileData.add(
                        ClassDetailAttachmentDao(
                            filePath,
                            2,
                            "",
                            "",
                            "",
                            "",
                            fileName.substringAfterLast("/")
                        )
                    )
                    insertFile()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initRecycler() {
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

    private fun insertFile() {
        mFileList.clear()
        mFileList.addAll(DataDummy.materialFileData)
        mFileAdapter.notifyDataSetChanged()
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