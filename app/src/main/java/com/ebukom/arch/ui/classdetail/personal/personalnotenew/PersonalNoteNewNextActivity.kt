package com.ebukom.arch.ui.classdetail.personal.personalnotenew

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailItemCheckDao
import com.ebukom.arch.ui.classdetail.ClassDetailCheckAdapter
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import kotlinx.android.synthetic.main.activity_personal_note_new_next.*
import kotlinx.android.synthetic.main.activity_personal_note_new_next.toolbar
import kotlinx.android.synthetic.main.fragment_personal.view.*

class PersonalNoteNewNextActivity : AppCompatActivity(), ClassDetailCheckAdapter.OnCheckListener {

    val list = ArrayList<ClassDetailItemCheckDao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_new_next)

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

        if(sharePref.getInt("level", 0) == 1){
            tvToolbarTitle.text = "Buat Catatan untuk Guru"
            // Share Personal Note Button
            btnPersonalNoteNewNextDone.setOnClickListener {
                loading.visibility = View.VISIBLE
                Handler().postDelayed({
                    loading.visibility = View.GONE
                    popUpMenu()
                    finish()
                }, 1000)
            }
        } else {
            // Share Personal Note Button
            btnPersonalNoteNewNextDone.setOnClickListener {
                loading.visibility = View.VISIBLE
                Handler().postDelayed({
                    loading.visibility = View.GONE
                    finish()
                }, 1000)
            }
        }

        initToolbar()

        // RV items
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
                    list, this@PersonalNoteNewNextActivity
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

    override fun onCheckChange() {
        var isCheckedItem = false
        list.forEach {
            if(it.isChecked) isCheckedItem = true
        }

        if(isCheckedItem){
            btnPersonalNoteNewNextDone.isEnabled = true
            btnPersonalNoteNewNextDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        } else {
            btnPersonalNoteNewNextDone.isEnabled = false
            btnPersonalNoteNewNextDone.setBackgroundColor(
                Color.parseColor("#828282")
            )
        }
    }

    fun popUpMenu() {
        val builder = AlertDialog.Builder(this@PersonalNoteNewNextActivity)

        builder.setMessage("Catatan berhasil disampaikan ke Ibu Ratu Cinta")

        builder.setPositiveButton("OK") { dialog, which ->
            finish()
        }

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