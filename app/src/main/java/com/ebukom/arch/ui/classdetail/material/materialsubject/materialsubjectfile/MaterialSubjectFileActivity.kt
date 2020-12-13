package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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

        sectionId = intent?.extras?.getString("sectionId")
        sectionName = intent?.extras?.getString("sectionName")
        subjectId = intent?.extras?.getString("subjectId")

        tvToolbarTitle.text = sectionName

        db.collection("material_subjects").document(subjectId!!).collection("subject_sections")
            .document(sectionId!!).addSnapshotListener { value, error ->
                if (error != null) {
                    Timber.e(error)
                    return@addSnapshotListener
                }

                initRecycler()

                if ((value?.get("files") as List<HashMap<Any, Any>>).isNotEmpty()) {
                    for (data in value?.get("files") as List<HashMap<Any, Any>>) {
                        mFileList.add(
                            ClassDetailAttachmentDao(
                                data["path"] as String,
                                (data["category"] as Long).toInt()
                            )
                        )
                    }
                }

                mFileAdapter.notifyDataSetChanged()
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

    fun popUpMenu(pos: Int) {
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

                mFileList.removeAt(pos)
                mFileAdapter.notifyDataSetChanged()

                val data = hashMapOf<String, Any>(
                    "files" to mFileList
                )

                db.collection("material_subjects").document(subjectId!!)
                    .collection("subject_sections")
                    .document(sectionId!!).update(data)
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