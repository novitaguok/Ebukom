package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_material_subject_file.*
import kotlinx.android.synthetic.main.bottom_sheet_choose_class.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MaterialSubjectFileActivity : AppCompatActivity() {

    private val mFileList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    lateinit var mFileAdapter: MaterialSubjectFileAdapter
    var sectionId: String? = null
    var subjectId: String? = null
    var sectionName: String? = null
    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject_file)

        initToolbar()
        initRecycler()

        val layout = intent?.extras?.getString("layout")
        sectionId = intent?.extras?.getString("sectionId")
        sectionName = intent?.extras?.getString("sectionName")
        subjectId = intent?.extras?.getString("subjectId")

        tvToolbarTitle.text = sectionName

        if (layout == "education") {
            // Load data
            db.collection("material_education").document(sectionId!!).collection("files")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    initRecycler()

                    for (data in value!!.documents) {
                        mFileList.add(
                            ClassDetailAttachmentDao(
                                data["title"] as String,
                                (data["category"] as Long).toInt(),
                                data.id,
                                "",
                                sectionId!!
                            )
                        )
                        mFileAdapter.notifyDataSetChanged()
                    }

                }
        } else {
            db.collection("material_subjects").document(subjectId!!).collection("subject_sections")
                .document(sectionId!!).collection("files").addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    initRecycler()

                    for (file in value!!.documents) {
                        mFileList.add(
                            ClassDetailAttachmentDao(
                                file["title"] as String,
                                (file["category"] as Long).toInt(),
                                file.id,
                                subjectId!!,
                                sectionId!!
                            )
                        )
                        mFileAdapter.notifyDataSetChanged()
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

    private fun initRecycler() {
        /**
         * Attachment list
         */
        mFileList.clear()
        mFileAdapter = MaterialSubjectFileAdapter(mFileList)
        rvMaterialSubjectFile.apply {
            layoutManager = LinearLayoutManager(
                this@MaterialSubjectFileActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mFileAdapter
        }
        mFileAdapter.notifyDataSetChanged()
//        checkAttachmentEmpty()
    }

    fun popUpMenu(fileId: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_choose_class, null)
        view.tvDeleteClass.text = "Hapus Materi"
        bottomSheetDialog.setContentView(view)

        view.tvDeleteClass.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus materi ini?")

            builder.setNegativeButton("BATALKAN") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { _, _ ->
                loading.visibility = View.VISIBLE

                db.collection("material_subjects").document(subjectId!!)
                    .collection("subject_sections")
                    .document(sectionId!!).collection("files").document(fileId).delete()
                    .addOnSuccessListener {
                        loading.visibility = View.GONE
                        Log.d("TAG", "Deleted successfully")
                    }.addOnFailureListener {
                        Log.d("TAG", "Delete failed")
                    }
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
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

        view.tvCancelClass.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
}