package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_material_subject_new.*
import kotlinx.android.synthetic.main.activity_material_subject_new.toolbar
import kotlinx.android.synthetic.main.activity_material_subject_new.tvToolbarTitle
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*

class MaterialSubjectNewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject_new)

        initToolbar()

        val list = ArrayList<ClassDetailTemplateTextDao>()

        list.add(ClassDetailTemplateTextDao("Perkalian dan Pembagian"))
        list.add(ClassDetailTemplateTextDao("Akar Kuadrat"))
        list.add(ClassDetailTemplateTextDao("Angka Romawi"))
        list.add(ClassDetailTemplateTextDao("Bilangan Prima"))

        val adapter = MaterialSubjectNewAdapter(list, this)

        rvMaterialSubject.layoutManager = LinearLayoutManager(this)
        rvMaterialSubject.adapter = adapter

        if (list.isNotEmpty()) {
            ivMaterialSubjectEmpty.visibility = View.INVISIBLE
            tvMaterialSubjectEmpty.visibility = View.INVISIBLE
        }

        // Get intent from MaterialSubjectFragment
        val material = intent.extras?.getString("material", "0")
        when (material) {
            "0" -> {
                val intent = Intent(this, MaterialSubjectAddActivity::class.java)
                tvToolbarTitle.text = "Matematika"
                btnMaterialSubject.text = "Tambah Materi Matematika"
                btnMaterialSubject.setOnClickListener {
                    intent.putExtra("material", "0")
                    startActivity(intent)
                }
            }
            "1" -> {
                val intent = Intent(this, MaterialSubjectAddActivity::class.java)
                tvToolbarTitle.text = "Ilmu Pengetahuan Alam"
                btnMaterialSubject.text = "Tambah Materi IPA"
                btnMaterialSubject.setOnClickListener {
                    intent.putExtra("material", "1")
                    startActivity(intent)
                }
            }
            "2" -> {
                val intent = Intent(this, MaterialSubjectAddActivity::class.java)
                tvToolbarTitle.text = "Bahasa Inggris"
                btnMaterialSubject.text = "Tambah Materi Bahasa Inggris"
                btnMaterialSubject.setOnClickListener {
                    intent.putExtra("material", "2")
                    startActivity(intent)
                }
            }
        }
    }

    fun popUpMenu() {

        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)

        view.tvEditInfo.setText("Edit Materi")
        view.tvDeleteInfo.setText("Hapus Materi")

        bottomSheetDialog.setContentView(view)

        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            val intent = Intent(this, MaterialSubjectAddActivity::class.java)
            intent.putExtra("page", "edit")
            startActivity(intent)
        }

        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@MaterialSubjectNewActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus materi ini?")

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show()
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