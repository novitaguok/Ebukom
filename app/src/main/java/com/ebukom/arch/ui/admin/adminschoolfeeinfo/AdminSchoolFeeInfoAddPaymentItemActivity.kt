package com.ebukom.arch.ui.admin.adminschoolfeeinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_payment_item.*

class AdminSchoolFeeInfoAddPaymentItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_school_fee_info_add_payment_item)

        // Attachment
//        val attachment: MutableList<ClassDetailAttachmentDao> = ArrayList()
//
//        attachment.add(
//            ClassDetailAttachmentDao(
//                "https://drive.google.com",
//                "drive.google.com", 0
//            )
//        )
//        attachment.add(
//            ClassDetailAttachmentDao(
//                "-",
//                "drive.google.com", 1
//            )
//        )
//        attachment.add(
//            ClassDetailAttachmentDao(
//                "-",
//                "drive.google.com", 2
//            )
//        )
//        rvPersonalNoteNewAttachment.apply {
//            layoutManager = LinearLayoutManager(
//                this@PersonalNoteNewActivity,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//            adapter =
//                ClassDetailAttachmentAdapter(
//                    attachment
//                )
//        }

        // Template Item
        val templateText: MutableList<ClassDetailTemplateTextDao> = ArrayList()
        templateText.add(ClassDetailTemplateTextDao("Pengembangan I/II"))
        templateText.add(ClassDetailTemplateTextDao("Kegiatan s.d. 2019-2020"))
        for (i: Int in 1..10) templateText.add(ClassDetailTemplateTextDao("Test tes"))
        rvAdminSchoolFeeInfoAddItemNewTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@AdminSchoolFeeInfoAddPaymentItemActivity,
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