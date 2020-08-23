package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAddTemplateActivity
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_school_announcement_edit.*
import kotlinx.android.synthetic.main.activity_school_announcement_edit.toolbar
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*
import kotlinx.android.synthetic.main.item_announcement_attachment.view.*

class SchoolAnnouncementEditActivity : AppCompatActivity() {

    private var pos: Int = -1
    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    private val mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)
    private val mTemplateList: ArrayList<ClassDetailTemplateTextDao> = arrayListOf()
    private val mTemplateAdapter = ClassDetailTemplateTextAdapter(mTemplateList)
    private val mAnnouncementList: ArrayList<ClassDetailAnnouncementDao> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_edit)

        initToolbar()

        mAnnouncementList.clear()
        mAnnouncementList.addAll(DataDummy.announcementData)

        // Get Announcement Data
        pos = intent?.extras?.getInt("pos", -1) ?: -1

        etSchoolAnnouncementEditTitle.setText(DataDummy.announcementData[pos].announcementTitle)
        etSchoolAnnouncementEditContent.setText(DataDummy.announcementData[pos].announcementContent)

        // Attachment List
        mAttachmentList.addAll(DataDummy.announcementData[pos].attachments)
        mAttachmentAdapter.notifyDataSetChanged()
        rvSchoolAnnouncementEditAttachment.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SchoolAnnouncementEditActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mAttachmentAdapter
        }
        if (mAttachmentList.isEmpty()) tvSchoolAnnouncementEditAttachmentTitle.visibility =
            View.GONE
        else tvSchoolAnnouncementEditAttachmentTitle.visibility = View.VISIBLE

        // Template List
        rvSchoolAnnouncementEditTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SchoolAnnouncementEditActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = mTemplateAdapter
        }
        mTemplateList.addAll(DataDummy.announcementTemplateData)
        mTemplateAdapter.notifyDataSetChanged()

        // Text watcher
        etSchoolAnnouncementEditTitle.addTextChangedListener(textWatcher)
        etSchoolAnnouncementEditContent.addTextChangedListener(textWatcher)

        // Add Template
        tvSchoolAnnouncementEditTemplateAdd.setOnClickListener {
            val intent = Intent(this, SchoolAnnouncementAddTemplateActivity::class.java)
            intent.putExtra("layout", "announcement")
            startActivity(intent)
        }

        // Save
        btnSchoolAnnouncementEditSave.setOnClickListener {
            var title = etSchoolAnnouncementEditTitle.text.toString()
            var content = etSchoolAnnouncementEditContent.text.toString()

            DataDummy.announcementData[pos].announcementTitle = title
            DataDummy.announcementData[pos].announcementContent = content
            DataDummy.announcementData[pos].attachments = mAttachmentList
            mAnnouncementList.clear()
            mAnnouncementList.addAll(DataDummy.announcementData)

            loading.visibility = View.VISIBLE
            Handler().postDelayed({
                loading.visibility = View.GONE
                finish()
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
            if (etSchoolAnnouncementEditTitle.text.toString()
                    .isNotEmpty() && etSchoolAnnouncementEditContent.text.toString()
                    .isNotEmpty()
            ) {
                btnSchoolAnnouncementEditSave.isEnabled = true
                btnSchoolAnnouncementEditSave.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnSchoolAnnouncementEditSave.isEnabled = false
                btnSchoolAnnouncementEditSave.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etSchoolAnnouncementEditTitle.text.toString()
                    .isNotEmpty() && etSchoolAnnouncementEditContent.text.toString()
                    .isNotEmpty()
            ) {
                btnSchoolAnnouncementEditSave.isEnabled = true
                btnSchoolAnnouncementEditSave.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnSchoolAnnouncementEditSave.isEnabled = false
                btnSchoolAnnouncementEditSave.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
        }
    }

    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val view = layoutInflater.inflate(R.layout.item_announcement_attachment, null)
        var path = data?.data?.path ?: ""

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                10 -> {
                    DataDummy.announcementAttachmentData.add(ClassDetailAttachmentDao(path, 1))
                    insertAttachment(view, path)
                }
                11 -> {
                    DataDummy.announcementAttachmentData.add(ClassDetailAttachmentDao(path, 2))
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

    fun deleteAttachment(item: ClassDetailAttachmentDao) {
        val builder = AlertDialog.Builder(this)

        builder.setMessage("Apakah Anda yakin ingin menghapus lampiran ini?")

        builder.setNegativeButton("BATALKAN") { dialog, which ->
            dialog.dismiss()
        }
        builder.setPositiveButton("HAPUS") { dialog, which ->
            DataDummy.announcementAttachmentData.remove(item)
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

    private fun checkAttachmentEmpty() {
        if (mAttachmentList.isEmpty()) {
            tvSchoolAnnouncementEditAttachmentTitle.visibility = View.GONE
        } else {
            tvSchoolAnnouncementEditAttachmentTitle.visibility = View.VISIBLE
        }
    }

    private fun insertAttachment(view: View, path: String) {
        mAttachmentAdapter.notifyDataSetChanged()
        mAttachmentList.clear()
        mAttachmentList.addAll(DataDummy.announcementAttachmentData)
        view.tvItemAnnouncementAttachment?.text = path

        checkAttachmentEmpty()
    }

    override fun onResume() {
        super.onResume()

        mTemplateList.clear()
        mTemplateList.addAll(DataDummy.announcementTemplateData)
        mTemplateAdapter.notifyDataSetChanged()
    }
}
