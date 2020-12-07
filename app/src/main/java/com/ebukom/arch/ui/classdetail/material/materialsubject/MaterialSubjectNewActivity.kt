package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMaterialSubjectSectionDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_material_subject_new.*
import kotlinx.android.synthetic.main.activity_material_subject_new.toolbar
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import timber.log.Timber

class MaterialSubjectNewActivity : AppCompatActivity() {

    private val mMaterialList: ArrayList<ClassDetailMaterialSubjectSectionDao> = arrayListOf()
    lateinit var mMaterialAdapter: MaterialSubjectSectionAdapter
    lateinit var material: String
    var classId: String? = null
    var subjectId: String? = null
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject_new)

        initToolbar()

        /**
         * Get intent from MainClassDetailActivity
         */
        classId = intent?.extras?.getString("classId")
        subjectId = intent?.extras?.getString("subjectId")
        checkEmpty()

        val sharePref: SharedPreferences = this.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if (sharePref.getInt("level", 0) == 1) {
            btnMaterialSubject.visibility = View.GONE
        }

        val intent = Intent(this, MaterialSubjectAddActivity::class.java)
        btnMaterialSubject.setOnClickListener {
            startActivity(intent)
        }

        if (classId != null && subjectId != null) {
            db.collection("material_subjects").document(subjectId!!).collection("subject_section")
                .whereArrayContains("class_ids", classId!!)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    initRecycler()

                    for (document in value!!.documents) {
                        mMaterialList.add(
                            ClassDetailMaterialSubjectSectionDao(
                                document["name"] as String,
                                document["file"] as String,
                                document["video"] as String,
                                document["link"] as String,
                                document.id,
                                subjectId!!
                            )
                        )
                    }

                    checkEmpty()
                }
        }
    }

    private fun initRecycler() {
        /**
         * Section list
         */
        mMaterialList.clear()
        mMaterialAdapter = MaterialSubjectSectionAdapter(mMaterialList, this)
        rvMaterialSubject.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mMaterialAdapter
        }
        mMaterialAdapter.notifyDataSetChanged()
    }

    private fun checkEmpty() {
        if (mMaterialList.isNotEmpty()) {
            ivMaterialSubjectEmpty.visibility = View.INVISIBLE
            tvMaterialSubjectEmpty.visibility = View.INVISIBLE
        } else {
            ivMaterialSubjectEmpty.visibility = View.VISIBLE
            tvMaterialSubjectEmpty.visibility = View.VISIBLE
        }
    }

    fun popUpMenu(item: ClassDetailTemplateTextDao, pos: Int) {

        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)

        view.tvEditInfo.text = "Edit Materi"
        view.tvDeleteInfo.text = "Hapus Materi"

        bottomSheetDialog.setContentView(view)

        // Edit Material
        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            val intent = Intent(this, MaterialSubjectRecapActivity::class.java)
            intent.putExtra("page", "edit")
            intent.putExtra("material", material)
            intent.putExtra("data", item)
            intent.putExtra("pos", pos)
            startActivity(intent)
        }

        // Delete Material
        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@MaterialSubjectNewActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus materi ini?")

            builder.setNegativeButton("BATALKAN") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { dialog, _ ->
                dialog.dismiss()
//                when (intent.extras?.getString("material", "0")) {
//                    "0" -> {
//                        DataDummy.mathMaterial.remove(item)
//                        mMaterialList.remove(item)
//                        mMaterialAdapter.notifyDataSetChanged()
//                        checkEmpty()
//                    }
//                    "1" -> {
//                        DataDummy.scienceMaterial.remove(item)
//                        mMaterialList.remove(item)
//                        mMaterialAdapter.notifyDataSetChanged()
//                        checkEmpty()
//                    }
//                    "2" -> {
//                        DataDummy.englishMaterial.remove(item)
//                        mMaterialList.remove(item)
//                        mMaterialAdapter.notifyDataSetChanged()
//                        checkEmpty()
//                    }
//                }
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

        // Cancel
        view.tvCancelInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
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