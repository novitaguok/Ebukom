package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_announcement_edit.*
import kotlinx.android.synthetic.main.activity_school_announcement_edit.toolbar
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*
import kotlinx.android.synthetic.main.item_announcement_attachment.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class SchoolAnnouncementEditActivity : AppCompatActivity() {
    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    lateinit var mAttachmentAdapter: ClassDetailAttachmentAdapter
    val db = FirebaseFirestore.getInstance()
    var classId: String? = null
    var announcementId: String? = null
    var attachmentIdList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_edit)

        initToolbar()
        initRecycler()
        checkEmpty()

        /**
         * Get announcement data
         */
        classId = intent?.extras?.getString("classId", classId)
        announcementId = intent?.extras?.getString("announcementId", announcementId)

        /**
         * Get announcement by announcementId
         */
        db.collection("classes").document(classId!!).collection("announcements")
            .document(announcementId!!)
            .get().addOnSuccessListener {

                initRecycler()

                etSchoolAnnouncementEditTitle.setText(it["title"] as String)
                etSchoolAnnouncementEditContent.setText(it["content"] as String)

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

        /**
         * Text watcher
         */
        etSchoolAnnouncementEditTitle.addTextChangedListener(textWatcher)
        etSchoolAnnouncementEditContent.addTextChangedListener(textWatcher)

        /**
         * Update done
         */
        btnSchoolAnnouncementEditSave.setOnClickListener {
            val data = hashMapOf<String, Any>(
                "title" to etSchoolAnnouncementEditTitle.text.toString(),
                "content" to etSchoolAnnouncementEditContent.text.toString(),
                "attachments" to mAttachmentList
            )

            db.collection("classes").document(classId!!).collection("announcements")
                .document(announcementId!!).update(data).addOnSuccessListener {
                    Log.d("TAG", "announcement inserted")
                    mAttachmentAdapter.notifyDataSetChanged()
                    loading.visibility = View.GONE
                    finish()
                }.addOnFailureListener {
                    Log.d("TAG", "announcement failed")
                    loading.visibility = View.GONE
                    finish()
                }

            finish()
        }
    }

    private fun checkEmpty() {
        /**
         * Check if attachment list is empty
         */
        if (mAttachmentList.isEmpty()) tvSchoolAnnouncementEditAttachmentTitle.visibility =
            View.GONE
        else tvSchoolAnnouncementEditAttachmentTitle.visibility = View.VISIBLE
    }

    private fun initRecycler() {
        /**
         * Attachment list
         */
        mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)
        rvSchoolAnnouncementEditAttachment.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SchoolAnnouncementEditActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mAttachmentAdapter
        }
        mAttachmentAdapter.notifyDataSetChanged()
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

                        checkEmpty()
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

    /**
     * Delete attachment
     */
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
        mAttachmentList.addAll(DataDummy.announcementAttachmentData)
        view.tvItemAnnouncementAttachment?.text = path

        this.checkEmpty()
    }

    override fun onResume() {
        super.onResume()

        // Announcement List
//        mAttachmentList.clear()
//        mAttachmentList.addAll()
        mAttachmentAdapter.notifyDataSetChanged()
    }
}
