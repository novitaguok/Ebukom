package com.ebukom.arch.ui.classdetail.personal.personalnoteedit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_personal_note_edit.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*

class PersonalNoteEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_edit)

        initToolbar()

        // Attachment
        val attachment: MutableList<ClassDetailAttachmentDao> = ArrayList()
        attachment.add(
            ClassDetailAttachmentDao(
                "drive.google.com", 0
            )
        )
        attachment.add(
            ClassDetailAttachmentDao(
                "drive.google.com", 1
            )
        )
        attachment.add(
            ClassDetailAttachmentDao(
                "drive.google.com", 2
            )
        )


        rvPersonalNoteAttachment.apply {
            layoutManager = LinearLayoutManager(
                this@PersonalNoteEditActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter =
                ClassDetailAttachmentAdapter(
                    attachment
                )
        }

        // Template Title
        val templateText: MutableList<ClassDetailTemplateTextDao> = ArrayList()
//        templateText.add(ClassDetailTemplateTextDao("Field Trip"))
//        for (i: Int in 1..10) templateText.add(ClassDetailTemplateTextDao("Perubahan Seragam"))
        rvPersonalNoteEditTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@PersonalNoteEditActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter =
                ClassDetailTemplateTextAdapter(
                    templateText
                )
        }

        // Text watcher
        etPersonalNoteEditContent.addTextChangedListener(textWatcher)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.attachment_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.attachment -> {
                val bottomSheetDialog = BottomSheetDialog(this)
                val view =
                    layoutInflater.inflate(R.layout.bottom_sheet_class_detail_attachment, null)
                bottomSheetDialog.setContentView(view)

                view.clBottomClassDetailAttachmentPhoto.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    Toast.makeText(this, "A", Toast.LENGTH_LONG).show()
                }
                view.clBottomSheetClassDetailAttachmentFile.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
                }
                view.clBottomSheetClassDetailAttachmentLink.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
                }
                view.clBottomSheetClassDetailAttachmentUseCamera.setOnClickListener(object: View.OnClickListener {
                    override fun onClick(v: View?) {
                        bottomSheetDialog.dismiss()
                        openCamera()
                    }
                })

                bottomSheetDialog.show()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
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
            if (etPersonalNoteEditContent.text.toString()
                    .isNotEmpty()
            ) {
                btnPersonalNoteEditSave.setEnabled(true)
                btnPersonalNoteEditSave.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnPersonalNoteEditSave.setEnabled(false)
                btnPersonalNoteEditSave.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }

    fun openCamera() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val bp = (data?.extras?.get("data")) as Bitmap
//            blabla.setImageBitmap(bp)
        }
    }
}
