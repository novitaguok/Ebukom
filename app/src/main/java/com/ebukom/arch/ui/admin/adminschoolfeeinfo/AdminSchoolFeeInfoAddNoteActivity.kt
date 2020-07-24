package com.ebukom.arch.ui.admin.adminschoolfeeinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_note.*

class AdminSchoolFeeInfoAddNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_school_fee_info_add_note)

        // Template Note
        val templateText: MutableList<ClassDetailTemplateTextDao> = ArrayList()
        templateText.add(ClassDetailTemplateTextDao("Catatan 1"))
        templateText.add(ClassDetailTemplateTextDao("Catatan Field Trip"))
        for (i: Int in 1..10) templateText.add(ClassDetailTemplateTextDao("Test tes"))
        rvAdminSchoolFeeInfoAddNoteTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@AdminSchoolFeeInfoAddNoteActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter =
                ClassDetailTemplateTextAdapter(
                    templateText
                )
        }

    }
}