package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import kotlinx.android.synthetic.main.activity_material_subject_add.*
import kotlinx.android.synthetic.main.activity_material_subject_add.rvMaterialSubjectAddAttachment
import kotlinx.android.synthetic.main.activity_material_subject_add.toolbar

class MaterialSubjectAddActivity : AppCompatActivity() {
    private var pos: Int = -1
    var isSetTitle = false
    var isSetFile = false
    lateinit var material : String
    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    private val mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_subject_add)

        initToolbar()

        material = intent.extras?.getString("material", "0")?: "0"

//        mAttachmentList.add(ClassDetailAttachmentDao("test", 1))
//        mAttachmentList.add(ClassDetailAttachmentDao("test", 2))
//        mAttachmentList.add(ClassDetailAttachmentDao("test", 3))
//        mAttachmentList.add(ClassDetailAttachmentDao("test", 4))

        checkAttachmentEmpty()

        // Attachment List
        rvMaterialSubjectAddAttachment.apply {
            layoutManager = LinearLayoutManager(
                this@MaterialSubjectAddActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mAttachmentAdapter
        }

        // Text Watcher
        etMaterialSubjectAddTitle.addTextChangedListener(textWatcher)

        // File Chooser
//        btnMaterialSubjectChooseFile.setOnClickListener {
//            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
//            fileIntent.type = "*/*"
//            startActivityForResult(fileIntent, 10)
//        }

        checkSection()
    }

    private fun checkAttachmentEmpty() {
        if (mAttachmentList.isNotEmpty()) {
            btnMaterialSubjectAddDone.isEnabled = true
            btnMaterialSubjectAddDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etMaterialSubjectAddTitle.text.toString().isNotEmpty()) {
                isSetTitle = true
            } else {
                btnMaterialSubjectAddDone.setEnabled(false)
                btnMaterialSubjectAddDone.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }

    private fun checkSection() {
        // Button disabled
        if (!isSetTitle && !isSetFile) {
            btnMaterialSubjectAddDone.isEnabled = false
            btnMaterialSubjectAddDone.setBackgroundColor(
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