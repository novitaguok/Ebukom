package com.ebukom.arch.ui.classdetail.material.materialeducation.materialeducationnew

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_material_education_new.*
import kotlinx.android.synthetic.main.activity_material_education_new.toolbar

class MaterialEducationNewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_education_new)

        initToolbar()

        // Text Watcher
        etMaterialEducationNewTitle.addTextChangedListener(textWatcher)
        etMaterialEducationNewContent.addTextChangedListener(textWatcher)

        // Done
        btnMaterialEducationNewDone.setOnClickListener {
            val title = etMaterialEducationNewTitle.text.toString()
            val content = etMaterialEducationNewContent.text.toString()

            DataDummy.educationData.add(ClassDetailAnnouncementDao(title, content))

            finish()
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

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etMaterialEducationNewTitle.text.toString()
                    .isNotEmpty() && etMaterialEducationNewContent.text.toString()
                    .isNotEmpty()
            ) {
                btnMaterialEducationNewDone.isEnabled = true
                btnMaterialEducationNewDone.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnMaterialEducationNewDone.isEnabled = false
                btnMaterialEducationNewDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }
}