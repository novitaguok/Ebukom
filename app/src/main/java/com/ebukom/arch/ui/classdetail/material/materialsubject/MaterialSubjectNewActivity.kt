package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_material_subject.*
import kotlinx.android.synthetic.main.activity_school_announcement_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.item_subject.*

class MaterialSubjectNewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject)

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
    }

    fun popUpMenu() {

        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)

        view.tvEditInfo.setText("Edit Materi")
        view.tvDeleteInfo.setText("Hapus Materi")

        bottomSheetDialog.setContentView(view)

        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Edit", Toast.LENGTH_LONG).show()
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
        }

        view.tvCancelInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show()
        }

        bottomSheetDialog.show()
    }

}