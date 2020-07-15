package com.ebukom.arch.ui.classdetail.personal

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailItemCheckDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailCheckAdapter
import kotlinx.android.synthetic.main.activity_personal_note_new_next.*
import kotlinx.android.synthetic.main.activity_school_anouncement_new.*

class PersonalNoteNewNextActivity : AppCompatActivity() {

    val list = ArrayList<ClassDetailItemCheckDao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_new_next)

        list.add(ClassDetailItemCheckDao("Semua Wali Murid"))
        list.add(ClassDetailItemCheckDao("Jumaidah Estetika"))
        list.add(ClassDetailItemCheckDao("Siti Nur Mudhaya"))
        list.add(ClassDetailItemCheckDao("Rizki Azhar"))
        list.add(ClassDetailItemCheckDao("Putri Tryatna"))

        rvPersonalNoteNewNext.apply {
            layoutManager = LinearLayoutManager(
                this@PersonalNoteNewNextActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter =
                ClassDetailCheckAdapter(
                    list
                )
        }
    }
}