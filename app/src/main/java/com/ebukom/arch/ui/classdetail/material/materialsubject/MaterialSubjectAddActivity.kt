package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ebukom.R
import kotlinx.android.synthetic.main.activity_material_subject_add.*
import kotlinx.android.synthetic.main.activity_material_subject_add.toolbar
import kotlinx.android.synthetic.main.activity_material_subject_add.tvToolbarTitle

class MaterialSubjectAddActivity : AppCompatActivity() {
    var isSetTitle = false
    var isSetFile = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject_add)

        initToolbar()

        // Get intent from MaterialSubjectNewActivity
        val material = intent.extras?.getString("material", "0")
        when (material) {
            "0" -> {
                tvToolbarTitle.text = "Tambah Materi Matematika"
                intent.putExtra("material", "0")
            }
            "1" -> {
                tvToolbarTitle.text = "Tambah Materi Ilmu Pengetahuan Alam"
                intent.putExtra("material", "1")
            }
            "2" -> {
                tvToolbarTitle.text = "Tambah Materi Bahasa Inggris"
                intent.putExtra("material", "2")
            }
        }

        val page = intent.extras?.getString("page", "edit")
        when(page) {
            "edit" -> tvToolbarTitle.text = "Edit Materi"
        }

        // Textwatcher
        etMaterialSubjectAdd.addTextChangedListener(textWatcher)

        // File chooser
        btnMaterialSubjectChooseFile.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "*/*"
            startActivityForResult(fileIntent, 10)
        }

        checkSection()

        btnMaterialSubjectDone.setOnClickListener {
            finish()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etMaterialSubjectAdd.text.toString().isNotEmpty()) {
                isSetTitle = true
            }
            else {
                btnMaterialSubjectDone.setEnabled(false)
                btnMaterialSubjectDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                10 -> {
                    tvMaterialSubjectFilePath.text = data?.data?.path ?: ""
                    tvMaterialSubjectFilePath.visibility = View.VISIBLE
                    ivMaterialSubjectDelete.visibility = View.VISIBLE
                    btnMaterialSubjectChooseFile.setText("UBAH FILE MATERI")
                    btnMaterialSubjectChooseFile.background =
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.btn_red_rectangle
                        )

                    btnMaterialSubjectDone.isEnabled = true
                    btnMaterialSubjectDone.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorSuperDarkBlue
                        )
                    )

                    // Alert
                    ivMaterialSubjectDelete.setOnClickListener {
                        val builder = AlertDialog.Builder(this@MaterialSubjectAddActivity)

                        builder.setMessage("Apakah Anda yakin ingin menghapus materi ini?")

                        builder.setNegativeButton("BATALKAN") { dialog, which ->
                            Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
                        }
                        builder.setPositiveButton("HAPUS") { dialog, which ->
                            btnMaterialSubjectChooseFile.setText("PILIH FILE MATERI")
                            btnMaterialSubjectChooseFile.background =
                                ContextCompat.getDrawable(
                                    applicationContext,
                                    R.drawable.btn_darkblue_rectangle
                                )
                            tvMaterialSubjectFilePath.visibility = View.INVISIBLE
                            ivMaterialSubjectDelete.visibility = View.GONE

                            isSetFile = true
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
                }
                else -> {
                    btnMaterialSubjectDone.isEnabled = false
                    btnMaterialSubjectDone.setBackgroundColor(
                        Color.parseColor("#828282")
                    )
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun checkSection() {
        // Button disabled
        if (!isSetTitle && !isSetFile) {
            btnMaterialSubjectDone.isEnabled = false
            btnMaterialSubjectDone.setBackgroundColor(
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
}