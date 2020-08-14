package com.ebukom.arch.ui.classdetail.personal.personalnotenew

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.chooseclass.ChooseClassActivity
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAddTemplateActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_personal_note_new.*
import kotlinx.android.synthetic.main.activity_personal_note_new.toolbar
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*

class PersonalNoteNewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_new)

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

        if (sharePref.getInt("level", 0) == 1) {
            tvPersonalNoteNewTitle.text = "Buat Catatan untuk Guru"
        }

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
        rvPersonalNoteNewAttachment.apply {
            layoutManager = LinearLayoutManager(
                this@PersonalNoteNewActivity,
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
//        templateText.add(ClassDetailTemplateTextDao("Anak Sakit"))
//        templateText.add(ClassDetailTemplateTextDao("Anak Bertengkar"))
//        for (i: Int in 1..10) templateText.add(ClassDetailTemplateTextDao("Perubahan Kesulitan Bernafas"))
        rvPersonalNoteNewTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@PersonalNoteNewActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter =
                ClassDetailTemplateTextAdapter(
                    templateText
                )
        }

        // Text watcher
        etPersonalNoteNewContent.addTextChangedListener(textWatcher)

        // Tambah template
        tvPersonalNoteNewTemplateAdd.setOnClickListener {
            val intent = Intent(this, SchoolAnnouncementAddTemplateActivity::class.java)

            intent.putExtra("layout", "note")
            startActivity(intent)
        }

        // "SELANJUTNYA"
        btnPersonalNoteNewNext.setOnClickListener {
            loading.visibility = View.VISIBLE
            Handler().postDelayed({
                loading.visibility = View.GONE
                startActivity(Intent(this, PersonalNoteNewNextActivity::class.java))
            }, 1000)
        }
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
                    val builder = AlertDialog.Builder(this@PersonalNoteNewActivity)
                    val view = layoutInflater.inflate(R.layout.alert_edit_text, null)

                    bottomSheetDialog.dismiss()

                    view.tvAlertEditText.setText("Link")
                    view.etAlertEditText.setHint("Masukkan Link")
                    builder.setView(view)

                    builder.setPositiveButton("LAMPIRKAN", null)
                    builder.setNegativeButton("BATALKAN") { dialog, which ->
                        dialog.dismiss()
                    }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()

                    val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    positiveButton.setOnClickListener {
                        if (view.etAlertEditText.text.toString().isEmpty()) {
                            view.tvAlertEditTextErrorMessage.visibility = View.VISIBLE
                        } else {
                            dialog.dismiss()
                        }
                    }
                    positiveButton.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorSuperDarkBlue
                        )
                    )

                    val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    negativeButton.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorGray
                        )
                    )
                }
                view.clBottomSheetClassDetailAttachmentUseCamera.setOnClickListener(object :
                    View.OnClickListener {
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

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etPersonalNoteNewContent.text.toString().isNotEmpty()) {
                btnPersonalNoteNewNext.setEnabled(true)
                btnPersonalNoteNewNext.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnPersonalNoteNewNext.setEnabled(false)
                btnPersonalNoteNewNext.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
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