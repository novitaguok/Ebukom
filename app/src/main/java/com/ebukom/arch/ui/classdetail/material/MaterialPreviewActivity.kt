package com.ebukom.arch.ui.classdetail.material

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ebukom.R
import kotlinx.android.synthetic.main.activity_material_preview.*
import kotlinx.android.synthetic.main.activity_register_parent.*
import kotlinx.android.synthetic.main.activity_register_parent.toolbar

class MaterialPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_preview)

        initToolbar()

        btnMaterialPreviewSave.setOnClickListener {
            val builder = AlertDialog.Builder(this@MaterialPreviewActivity)

            builder.setMessage("File berhasil disimpan")
            builder.setPositiveButton("OK", null)

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
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