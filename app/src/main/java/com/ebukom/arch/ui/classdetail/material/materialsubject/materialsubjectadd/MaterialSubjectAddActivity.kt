package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_material_subject_add.*
import kotlinx.android.synthetic.main.activity_material_subject_add.rvMaterialSubjectAddAttachment
import kotlinx.android.synthetic.main.activity_material_subject_add.toolbar
import kotlinx.android.synthetic.main.activity_material_subject_add.tvToolbarTitle
import kotlinx.android.synthetic.main.activity_material_subject_recap.*
import kotlinx.android.synthetic.main.activity_material_subject_recap.view.*
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

        subjectId = intent?.extras?.getString("subjectId")
        classId = intent?.extras?.getString("classId")
        sectionId = intent?.extras?.getString("sectionId")
        subjectName = intent?.extras?.getString("subjectName")
        action = intent?.extras?.getString("action")
        if (action == "edit") {
            db.collection("material_subjects").document(subjectId!!).collection("subject_sections")
                .document(sectionId!!)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    tvToolbarTitle.text = "Edit Materi " + value?.get("name") as String
                    etMaterialSubjectAddTitle.setText(value["name"] as String)

                    initRecycler()

                    db.collection("material_subjects").document(subjectId!!)
                        .collection("subject_sections")
                        .document(sectionId!!).collection("files")
                        .addSnapshotListener { value, error ->
                            if (error != null) {
                                Timber.e(error)
                                return@addSnapshotListener
                            }

                            for (document in value!!.documents) {
                                mFileList.add(
                                    ClassDetailAttachmentDao(
                                        document["path"] as String,
                                        (document["category"] as Long).toInt(),
                                        document.id
                                    )
                                )
                            }

                            mFileAdapter.notifyDataSetChanged()
                            checkEmpty()
                        }
                }

            btnMaterialSubjectAddDone.setOnClickListener {
                val data = hashMapOf<String, Any>(
                    "name" to etMaterialSubjectAddTitle.text.toString(),
                    "date" to Timestamp(Date())
                )

                loading.visibility = View.VISIBLE
                db.collection("material_subjects").document(subjectId!!)
                    .collection("subject_sections")
                    .document(sectionId!!).update(data).addOnCompleteListener {
                        if (it.isSuccessful) {
                            if (mFileList.isEmpty()) {
                                finish()
                            }

                            mFileList.forEach {
                                db.collection("material_subjects").document(subjectId!!)
                                    .collection("subject_sections").document(sectionId!!)
                                    .collection("files")
                                    .add(
                                        mapOf<String, Any>(
                                            "category" to it.category,
                                            "path" to it.path
                                        )
                                    ).addOnSuccessListener {
                                        Log.d("TAG", "Section inserted successfully")
                                        finish()
                                    }.addOnFailureListener {
                                        Log.d("TAG", "Section failed to be inserted")
                                        finish()
                                    }
                            }
                            loading.visibility = View.GONE
                        } else {
                            loading.visibility = View.GONE
                            finish()
                        }
                    }.addOnFailureListener {
                        Log.d("TAG", "Material failed to be inserted")
                    }
            }

            checkEmpty()
        } else {
            if (subjectName == "Rekap Pembelajaran\nOnline") {
                subjectName = "Rekap Pembelajaran Online"
            }
            tvToolbarTitle.text = "Tambah materi " + subjectName

            btnMaterialSubjectAddDone.setOnClickListener {

                val data = hashMapOf<String, Any>(
                    "name" to etMaterialSubjectAddTitle.text.toString(),
                    "date" to Timestamp(Date())
                )

                loading.visibility = View.VISIBLE
                db.collection("material_subjects").document(subjectId!!)
                    .collection("subject_sections")
                    .add(data).addOnCompleteListener {
                        if (it.isSuccessful) {
                            var sectionId = it.result?.id

                            if (mFileList.isEmpty()) {
                                finish()
                            }

                            mFileList.forEach {
                                db.collection("material_subjects").document(subjectId!!)
                                    .collection("subject_sections").document(sectionId!!)
                                    .collection("files")
                                    .document().update(
                                        mapOf<String, Any>(
                                            "category" to it.category,
                                            "path" to it.path
                                        )
                                    ).addOnSuccessListener {
                                        Log.d("TAG", "Section inserted successfully")
                                        finish()
                                    }.addOnFailureListener {
                                        Log.d("TAG", "Section failed to be inserted")
                                        finish()
                                    }
                            }
                            loading.visibility = View.GONE
                        } else {
                            loading.visibility = View.GONE
                            finish()
                        }
                    }.addOnFailureListener {
                        Log.d("TAG", "Material failed to be inserted")
                    }
            }
        }

        checkEmpty()
        initRecycler()

        /**
         * File chooser
         */
        btnMaterialSubjectAddButton.setOnClickListener {

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

        checkSection()
    }

    private fun initRecycler() {
        mFileList.clear()
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

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
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
}