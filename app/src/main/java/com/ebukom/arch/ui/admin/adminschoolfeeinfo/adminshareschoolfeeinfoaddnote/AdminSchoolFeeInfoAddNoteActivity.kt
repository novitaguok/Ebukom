package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfoaddnote

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.ebukom.arch.ui.classdetail.personal.personalnotenew.PersonalNoteAddTemplateActivity
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_note.*
import kotlinx.android.synthetic.main.activity_admin_school_fee_info_add_note.toolbar

class AdminSchoolFeeInfoAddNoteActivity : AppCompatActivity() {

    private val mTemplateList: ArrayList<ClassDetailTemplateTextDao> = arrayListOf()
    private val mTemplateAdapter = ClassDetailTemplateTextAdapter(mTemplateList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_school_fee_info_add_note)

        initToolbar()

        // Template Note
        rvAdminSchoolFeeInfoAddNoteTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@AdminSchoolFeeInfoAddNoteActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = mTemplateAdapter
        }

        // Add Note Template
        tvAdminSchoolFeeInfoAddNoteAddTemplate.setOnClickListener {
            startActivity(Intent(this, PersonalNoteAddTemplateActivity::class.java))
        }

        // Text Watcher
        etAdminSchoolFeeInfoAddNote.addTextChangedListener(textWatcher)

        // Done
        btnAdminSchoolFeeInfoAddNoteDone.setOnClickListener {
            loading.visibility = View.VISIBLE
            Handler().postDelayed({
                loading.visibility = View.GONE
                finish()
            }, 1000)
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
            if (etAdminSchoolFeeInfoAddNote.text.toString().isNotEmpty()) {
                btnAdminSchoolFeeInfoAddNoteDone.isEnabled = true
                btnAdminSchoolFeeInfoAddNoteDone.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnAdminSchoolFeeInfoAddNoteDone.isEnabled = false
                btnAdminSchoolFeeInfoAddNoteDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        mTemplateList.clear()
        mTemplateList.addAll(DataDummy.adminNoteTemplateData)
        mTemplateAdapter.notifyDataSetChanged()
    }
}