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
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_material_subject_new.*
import kotlinx.android.synthetic.main.activity_material_subject_new.toolbar
import kotlinx.android.synthetic.main.activity_material_subject_new.tvToolbarTitle
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*

class MaterialSubjectNewActivity : AppCompatActivity() {

    private val mMaterialList: ArrayList<ClassDetailTemplateTextDao> = arrayListOf()
    private val mMaterialAdapter = MaterialSubjectNewAdapter(mMaterialList, this)
    lateinit var material : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject_new)

        initToolbar()

        val sharePref: SharedPreferences = this.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if (sharePref.getInt("level", 0) == 1) {
            btnMaterialSubject.visibility = View.GONE
        }

        val intent = Intent(this, MaterialSubjectAddActivity::class.java)
        btnMaterialSubject.setOnClickListener {
            startActivity(intent)
        }

//        material  = intent.extras?.getString("material", "0")?: "0"
//        // Get intent from MaterialSubjectFragment
//        when (material) {
//            "0" -> {
//                val intent = Intent(this, MaterialSubjectRecapActivity::class.java)
//                tvToolbarTitle.text = "Matematika"
//                btnMaterialSubject.text = "Tambah Materi Matematika"
//
//                // Material
//                mMaterialList.addAll(DataDummy.mathMaterial)
//                mMaterialAdapter.notifyDataSetChanged()
//
//                // Intent
//                btnMaterialSubject.setOnClickListener {
//                    intent.putExtra("material", "0")
//                    startActivity(intent)
//                }
//            }
//            "1" -> {
//                val intent = Intent(this, MaterialSubjectRecapActivity::class.java)
//                tvToolbarTitle.text = "Ilmu Pengetahuan Alam"
//                btnMaterialSubject.text = "Tambah Materi IPA"
//
//                // Material
//                mMaterialList.addAll(DataDummy.scienceMaterial)
//                mMaterialAdapter.notifyDataSetChanged()
//
//                // Intent
//                btnMaterialSubject.setOnClickListener {
//                    intent.putExtra("material", "1")
//                    startActivity(intent)
//                }
//            }
//            "2" -> {
//                val intent = Intent(this, MaterialSubjectRecapActivity::class.java)
//                tvToolbarTitle.text = "Bahasa Inggris"
//                btnMaterialSubject.text = "Tambah Materi Bahasa Inggris"
//
//                // Material
//                mMaterialList.addAll(DataDummy.englishMaterial)
//                mMaterialAdapter.notifyDataSetChanged()
//
//                // Intent
//                btnMaterialSubject.setOnClickListener {
//                    intent.putExtra("material", "2")
//                    startActivity(intent)
//                }
//            }
//        }

        rvMaterialSubject.apply {
            layoutManager = LinearLayoutManager(
                this@MaterialSubjectNewActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mMaterialAdapter
        }
        checkMaterialEmpty()
    }

    private fun checkMaterialEmpty() {
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
            intent.putExtra("material",material)
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
                when (intent.extras?.getString("material", "0")) {
                    "0" -> {
                        DataDummy.mathMaterial.remove(item)
                        mMaterialList.remove(item)
                        mMaterialAdapter.notifyDataSetChanged()
                        checkMaterialEmpty()
                    }
                    "1" -> {
                        DataDummy.scienceMaterial.remove(item)
                        mMaterialList.remove(item)
                        mMaterialAdapter.notifyDataSetChanged()
                        checkMaterialEmpty()
                    }
                    "2" -> {
                        DataDummy.englishMaterial.remove(item)
                        mMaterialList.remove(item)
                        mMaterialAdapter.notifyDataSetChanged()
                        checkMaterialEmpty()
                    }
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

    override fun onResume() {
        super.onResume()

        checkMaterialEmpty()
        when (intent.extras?.getString("material", "0")) {
            "0" -> {
                mMaterialList.clear()
                mMaterialList.addAll(DataDummy.mathMaterial)
                mMaterialAdapter.notifyDataSetChanged()
            }
            "1" -> {
                mMaterialList.clear()
                mMaterialList.addAll(DataDummy.scienceMaterial)
                mMaterialAdapter.notifyDataSetChanged()
            }
            "2" -> {
                mMaterialList.clear()
                mMaterialList.addAll(DataDummy.englishMaterial)
                mMaterialAdapter.notifyDataSetChanged()
            }
        }
    }
}