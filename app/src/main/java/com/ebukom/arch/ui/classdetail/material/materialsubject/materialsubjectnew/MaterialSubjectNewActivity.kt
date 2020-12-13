package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectnew

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailMaterialRecapDao
import com.ebukom.arch.dao.ClassDetailMaterialSubjectSectionDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd.MaterialSubjectAddActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd.MaterialSubjectRecapActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfile.MaterialSubjectFileAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_material_subject_new.*
import kotlinx.android.synthetic.main.activity_material_subject_new.toolbar
import kotlinx.android.synthetic.main.bottom_sheet_choose_class.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import timber.log.Timber

class MaterialSubjectNewActivity : AppCompatActivity() {

    private val mSectionList: ArrayList<ClassDetailMaterialSubjectSectionDao> =
        arrayListOf()
    private val mFileList: ArrayList<ClassDetailAttachmentDao> =
        arrayListOf()
    lateinit var mMaterialSubjectAdapter: MaterialSubjectSectionAdapter
    lateinit var material: String
    var classId: String? = null
    var subjectId: String? = null
    var subjectName: String? = null
    var sectionTitle = ""
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject_new)

        val sharePref: SharedPreferences = this.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if (sharePref.getInt("level", 0) == 1) {
            btnMaterialSubject.visibility = View.GONE
        }

        initToolbar()
        initRecycler()

        /**
         * Get intent from MainClassDetailActivity
         */
        classId = intent?.extras?.getString("classId")
        subjectId = intent?.extras?.getString("subjectId")
        subjectName = intent?.extras?.getString("subjectName")

        /**
         * Toolbar title setup
         */
        if (subjectName == "Rekap Pembelajaran\nOnline") {
            subjectName = "Rekap Pembelajaran Online"
            btnMaterialSubject.text = "Tambahkan " + subjectName
        } else {
            btnMaterialSubject.text = "Tambahkan Materi " + subjectName
        }
        tvToolbarTitle.text = subjectName

        checkEmpty()

        /**
         * Intent to another page based on condition
         */
        btnMaterialSubject.setOnClickListener {
            var intent = Intent(this, MaterialSubjectAddActivity::class.java)
            if (subjectName == "Rekap Pembelajaran Online") {
                intent = Intent(this, MaterialSubjectRecapActivity::class.java)
            }
            intent.putExtra("classId", classId)
            intent.putExtra("subjectId", subjectId)
            intent.putExtra("subjectName", subjectName)
            startActivity(intent)
        }

        if (classId != null && subjectId != null) {
            db.collection("material_subjects").document(subjectId!!)
                .collection("subject_sections")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    initRecycler()

                    if (value!!.documents != null) {
                        mSectionList.clear()
                        for (document in value.documents) {
                            mSectionList.add(
                                ClassDetailMaterialSubjectSectionDao(
                                    document["name"] as String,
                                    arrayListOf(),
                                    (document["date"] as Timestamp).toDate().toString(),
                                    document.id,
                                    subjectId!!,
                                    classId!!,
                                    (document["date"] as Timestamp)
                                )
                            )
                        }
                        mMaterialSubjectAdapter.notifyDataSetChanged()
                        checkEmpty()
                    }
                }
        }
    }

    private fun initRecycler() {
        /**
         * Section list
         */
//        mMaterialSubjectList.clear()
        mMaterialSubjectAdapter = MaterialSubjectSectionAdapter(mSectionList, this)
        rvMaterialSubject.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mMaterialSubjectAdapter
        }

        checkEmpty()
    }

    private fun checkEmpty() {
        if (mSectionList.isEmpty()) {
            ivMaterialSubjectEmpty.visibility = View.VISIBLE
            tvMaterialSubjectEmpty.visibility = View.VISIBLE
        } else {
            ivMaterialSubjectEmpty.visibility = View.GONE
            tvMaterialSubjectEmpty.visibility = View.GONE
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

    fun popUpMenu(sectionId: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)
        view.tvEditInfo.text = "Edit Materi"
        view.tvDeleteInfo.text = "Hapus Materi"
        view.tvCancelInfo.text = "Tutup"
        bottomSheetDialog.setContentView(view)

        /**
         * Edit material
         */
        view.tvEditInfo.setOnClickListener {

            bottomSheetDialog.dismiss()

            db.collection("material_subjects").document(subjectId!!).collection("subject_sections")
                .document(sectionId).addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    sectionTitle = value!!["name"] as String

                    for (data in value["files"] as List<HashMap<Any, Any>>) {
                        mFileList.add(
                            ClassDetailAttachmentDao(
                                data["path"] as String,
                                (data["category"] as Long).toInt()
                            )
                        )
                    }
                }

            val intent = Intent(this, MaterialSubjectAddActivity::class.java)
            intent.putExtra("action", "edit")
            intent.putExtra("sectionId", sectionId)
            intent.putExtra("subjectId", subjectId)
            startActivity(intent)
        }

        /**
         * Delete material
         */
        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val db = FirebaseFirestore.getInstance()

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus materi ini?")
            builder.setNegativeButton("BATALKAN") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { _, _ ->
                db.collection("material_subjects").document(subjectId!!)
                    .collection("subject_sections")
                    .document(sectionId).delete().addOnSuccessListener {
                        Log.d("TAG", "Section deleted successfully")
                    }.addOnFailureListener {
                        Log.d("TAG", "Section is failed to be deleted")
                    }
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorRed
                )
            )
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )
        }
        view.tvCancelInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()

    }
}