package com.ebukom.arch.ui.classdetail.material.materialeducation.materialeducationedit

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.material.materialeducation.MaterialEducationFragment
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailAdapter
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_material_education_edit.*

class MaterialEducationEditActivity(val callback: OnMoreCallback) : AppCompatActivity() {
    private var pos: Int = -1
    private val mEducationList: ArrayList<ClassDetailAnnouncementDao> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_education_edit)

        initToolbar()

        pos = intent?.extras?.getInt("pos", -1) ?: -1

        etMaterialEducationEditTitle.setText(DataDummy.educationData[pos].announcementTitle)
        etMaterialEducationEditContent.setText(DataDummy.educationData[pos].announcementContent)

        // Text Watcher
        etMaterialEducationEditTitle.addTextChangedListener(textWatcher)
        etMaterialEducationEditContent.addTextChangedListener(textWatcher)

        btnMaterialEducationEditDone.isEnabled = true
        btnMaterialEducationEditDone.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorSuperDarkBlue
            )
        )

        btnMaterialEducationEditDone.setOnClickListener {
            val title = etMaterialEducationEditTitle.text.toString()
            val content = etMaterialEducationEditContent.text.toString()

            DataDummy.educationData[pos].announcementTitle = title
            DataDummy.educationData[pos].announcementContent = content

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
            if (etMaterialEducationEditTitle.text.toString()
                    .isNotEmpty() && etMaterialEducationEditContent.text.toString().isNotEmpty()
            ) {
                btnMaterialEducationEditDone.isEnabled = true
                btnMaterialEducationEditDone.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnMaterialEducationEditDone.isEnabled = false
                btnMaterialEducationEditDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }

}