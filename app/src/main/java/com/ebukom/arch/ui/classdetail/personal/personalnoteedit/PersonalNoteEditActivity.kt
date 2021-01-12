package com.ebukom.arch.ui.classdetail.personal.personalnoteedit

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAddTemplateActivity
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_personal_note_edit.*
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*
import kotlinx.android.synthetic.main.item_attachment.view.*

class PersonalNoteEditActivity : AppCompatActivity() {

    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    lateinit var mAttachmentAdapter: ClassDetailAttachmentAdapter
    private val mTemplateList: ArrayList<ClassDetailTemplateTextDao> = arrayListOf()
    private val mNoteList: ArrayList<ClassDetailPersonalNoteDao> = arrayListOf()
    var noteId: String? = null
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_edit)

        initToolbar()
        initRecycler()
        checkEmpty()

        // Get intent from MainClassDetailActivity
        noteId = intent?.extras?.getString("noteId")

        /**
         * Get announcement by announcementId
         */
        db.collection("notes").document(noteId!!).get().addOnSuccessListener {

                initRecycler()

                etPersonalNoteEditContent.setText(it["content"] as String)

                for (data in it["attachments"] as List<HashMap<Any, Any>>) {
                    mAttachmentList.add(
                        ClassDetailAttachmentDao(
                            data["path"] as String,
                            (data["category"] as Long).toInt()
                        )
                    )
                    mAttachmentAdapter.notifyDataSetChanged()
                    checkEmpty()
                }

                checkEmpty()

            }

        // Text watcher
        etPersonalNoteEditContent.addTextChangedListener(textWatcher)

        // Done
        btnPersonalNoteEditSave.setOnClickListener {
            val data = hashMapOf<String, Any>(
                "content" to etPersonalNoteEditContent.text.toString(),
                "attachments" to mAttachmentList
            )

            db.collection("notes").document(noteId!!).update(data).addOnSuccessListener {
                    Log.d("TAG", "updated")
                    mAttachmentAdapter.notifyDataSetChanged()
                    loading.visibility = View.GONE
                    finish()
                }.addOnFailureListener {
                    Log.d("TAG", "failed to update")
                    loading.visibility = View.GONE
                    finish()
                }

            finish()
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
                    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
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

                        checkEmpty()
                    }

                    val dialog: androidx.appcompat.app.AlertDialog = builder.create()
                    dialog.show()

                    val positiveButton =
                        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                    positiveButton.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorSuperDarkBlue
                        )
                    )

                    val negativeButton =
                        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
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
        val view = layoutInflater.inflate(R.layout.item_attachment, null)
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

    fun deleteAttachment(item: ClassDetailAttachmentDao) {
        val builder = AlertDialog.Builder(this@PersonalNoteEditActivity)

        builder.setMessage("Apakah Anda yakin ingin menghapus lampiran ini?")

        builder.setNegativeButton("BATALKAN") { dialog, which ->
            dialog.dismiss()
        }
        builder.setPositiveButton("HAPUS") { dialog, which ->
            DataDummy.noteAttachmentData.remove(item)
            mAttachmentList.remove(item)
            mAttachmentAdapter.notifyDataSetChanged()

            checkEmpty()
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

    private fun insertAttachment(view: View, path: String) {
        mAttachmentAdapter.notifyDataSetChanged()
        mAttachmentList.clear()
        mAttachmentList.addAll(DataDummy.noteAttachmentData)
        view.tvItemAttachment?.text = path

        checkEmpty()
    }

    private fun initRecycler() {
        /**
         * Attachment list
         */
        mAttachmentList.clear()
        mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)
        rvPersonalNoteAttachment.apply {
            layoutManager =
                LinearLayoutManager(
                    this@PersonalNoteEditActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mAttachmentAdapter
        }
        mAttachmentAdapter.notifyDataSetChanged()
    }

    private fun checkEmpty() {
        /**
         * Check if attachment list is empty
         */
        if (mAttachmentList.isEmpty()) tvPersonalNoteEditAttachmentTitle.visibility =
            View.GONE
        else tvPersonalNoteEditAttachmentTitle.visibility = View.VISIBLE
    }
}
