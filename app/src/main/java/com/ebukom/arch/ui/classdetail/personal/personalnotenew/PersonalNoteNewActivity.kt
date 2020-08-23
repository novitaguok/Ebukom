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
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.ebukom.arch.ui.classdetail.personal.personalnotenewnext.PersonalNoteNewNextActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAddTemplateActivity
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_personal_note_new.*
import kotlinx.android.synthetic.main.activity_personal_note_new.toolbar
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*
import kotlinx.android.synthetic.main.fragment_personal_sent_note.*
import kotlinx.android.synthetic.main.item_announcement_attachment.view.*

class PersonalNoteNewActivity : AppCompatActivity() {

    private val mNoteList: ArrayList<ClassDetailPersonalNoteDao> = arrayListOf()

    //    private val mNoteAdapter = PersonalNoteAdapter(mNoteList,)
    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    private val mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)
    private val mTemplateList: ArrayList<ClassDetailTemplateTextDao> = arrayListOf()
    private val mTemplateAdapter = ClassDetailTemplateTextAdapter(mTemplateList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_new)

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

        if (sharePref.getInt("level", 0) == 1) {
            tvPersonalNoteNewTitle.text = "Buat Catatan untuk Guru"
        }

        initToolbar()

        // Attachment List
        checkAttachmentEmpty()
        rvPersonalNoteNewAttachment.apply {
            layoutManager = LinearLayoutManager(
                this@PersonalNoteNewActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mAttachmentAdapter
        }

        // Template List
        mTemplateList.addAll(DataDummy.noteTemplateData)
        mTemplateAdapter.notifyDataSetChanged()
        rvPersonalNoteNewTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@PersonalNoteNewActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = mTemplateAdapter
        }

        // Text watcher
        etPersonalNoteNewContent.addTextChangedListener(textWatcher)

        // Add Template
        tvPersonalNoteNewTemplateAdd.setOnClickListener {
            var intent = Intent(this, SchoolAnnouncementAddTemplateActivity::class.java)
            intent.putExtra("layout", "note")
            startActivity(intent)
        }

        // To Next Activity
        btnPersonalNoteNewNext.setOnClickListener {
            val content = etPersonalNoteNewContent.text.toString()
            loading.visibility = View.VISIBLE
            Handler().postDelayed({
                loading.visibility = View.GONE
                val intent = Intent(this, PersonalNoteNewNextActivity::class.java)
                intent.putExtra("content", content)
                intent.putExtra("attachments", mAttachmentList)
                startActivity(intent)
                finish()
            }, 1000)
        }
    }

    private fun checkNoteEmpty() {
        if (mNoteList.isEmpty()) {
            tvPersonalEmpty.visibility = View.GONE
            ivPersonalEmpty.visibility = View.GONE
        } else {
            tvPersonalEmpty.visibility = View.VISIBLE
            ivPersonalEmpty.visibility = View.VISIBLE
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
                    val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
                    fileIntent.type = "*/*"
                    startActivityForResult(fileIntent, 10)
                }
                view.clBottomSheetClassDetailAttachmentFile.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
                    fileIntent.type = "*/*"
                    startActivityForResult(fileIntent, 11)
                }
                view.clBottomSheetClassDetailAttachmentLink.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    val builder = AlertDialog.Builder(this)
                    val view = layoutInflater.inflate(R.layout.alert_edit_text, null)

                    view.tvAlertEditText.text = "Link"
                    view.etAlertEditText.hint = "Masukkan link"

                    bottomSheetDialog.dismiss()
                    builder.setView(view)
                    builder.setNegativeButton("BATALKAN") { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.setPositiveButton("LAMPIRKAN") { dialog, which ->
                        val link = view.etAlertEditText?.text.toString()
                        DataDummy.announcementAttachmentData.add(ClassDetailAttachmentDao(link, 0))
                        insertAttachment(view, link)

                        checkAttachmentEmpty()
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

                    val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    negativeButton.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorRed
                        )
                    )
                }
                view.clBottomSheetClassDetailAttachmentUseCamera.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    openCamera()
                }

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
                btnPersonalNoteNewNext.isEnabled = true
                btnPersonalNoteNewNext.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnPersonalNoteNewNext.isEnabled = false
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
        val view = layoutInflater.inflate(R.layout.item_announcement_attachment, null)
        var path = data?.data?.path ?: ""

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                10 -> {
                    DataDummy.noteAttachmentData.add(ClassDetailAttachmentDao(path, 1))
                    insertAttachment(view, path)
                }
                11 -> {
                    DataDummy.noteAttachmentData.add(ClassDetailAttachmentDao(path, 2))
                    insertAttachment(view, path)
                }
                else -> {
                    val bp = (data?.extras?.get("data")) as Bitmap
//            blabla.setImageBitmap(bp)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun insertAttachment(view: View, path: String) {
        mAttachmentAdapter.notifyDataSetChanged()
        mAttachmentList.clear()
        mAttachmentList.addAll(DataDummy.noteAttachmentData)
        view.tvItemAnnouncementAttachment?.text = path

        checkAttachmentEmpty()
    }

    private fun checkAttachmentEmpty() {
        if (mAttachmentList.isEmpty()) {
            tvPersonalNoteNewAttachmentTitle.visibility = View.GONE
        } else {
            tvPersonalNoteNewAttachmentTitle.visibility = View.VISIBLE
        }
    }

    fun deleteAttachment(item: ClassDetailAttachmentDao) {
        val builder = AlertDialog.Builder(this@PersonalNoteNewActivity)

        builder.setMessage("Apakah Anda yakin ingin menghapus lampiran ini?")

        builder.setNegativeButton("BATALKAN") { dialog, which ->
            dialog.dismiss()
        }
        builder.setPositiveButton("HAPUS") { dialog, which ->
            DataDummy.noteAttachmentData.remove(item)
            mAttachmentList.remove(item)
            mAttachmentAdapter.notifyDataSetChanged()

            checkAttachmentEmpty()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorGray
            )
        )

        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorRed
            )
        )
    }

    override fun onResume() {
        super.onResume()

        mTemplateList.clear()
        mTemplateList.addAll(DataDummy.noteTemplateData)
        mTemplateAdapter.notifyDataSetChanged()
    }

    fun onClickedTemplate(item: ClassDetailTemplateTextDao) {
        etPersonalNoteNewContent.setText(item.content)
    }
}