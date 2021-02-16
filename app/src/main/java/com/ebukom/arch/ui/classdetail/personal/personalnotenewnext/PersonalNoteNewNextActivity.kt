package com.ebukom.arch.ui.classdetail.personal.personalnotenewnext

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailItemCheckThumbnailDao
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.dao.firebase.RegisterSchoolRequestDao
import com.ebukom.arch.ui.classdetail.ClassDetailCheckAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailCheckThumbnailAdapter
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.data.DataDummy
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.activity_personal_note_new.*
import kotlinx.android.synthetic.main.activity_personal_note_new_next.*
import kotlinx.android.synthetic.main.activity_personal_note_new_next.toolbar
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PersonalNoteNewNextActivity : AppCompatActivity(), ClassDetailCheckAdapter.OnCheckListener {

    private val mParentList: ArrayList<ClassDetailItemCheckThumbnailDao> = arrayListOf()
    lateinit var mParentAdapter: ClassDetailCheckThumbnailAdapter
    lateinit var content: String
    lateinit var dateTime: String
    lateinit var storageReference: StorageReference
    var attachments: List<ClassDetailAttachmentDao> = arrayListOf()
    var savedImageUri = arrayListOf<String>()
    val db = FirebaseFirestore.getInstance()
    var nm: String? = null
    var lev: Long = 0L
    var counter = 0
    var profilePic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_new_next)

        // Intent from PersonalNoteNewActivity
        content = intent?.extras?.getString("content", "") ?: ""
        attachments = intent?.getSerializableExtra("attachments") as List<ClassDetailAttachmentDao>
        dateTime = ""

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        val uid = sharePref.getString("uid", "") as String
        val level = sharePref.getLong("level", 0)
        val name = sharePref.getString("name", "")
        lev = level
        nm = name

        initToolbar()
        initRecycler()

        if (level == 1L) {
            tvToolbarTitle.text = "Buat Catatan untuk Guru"
            tvPersonalNoteNewNextAllParent.text = "Semua Guru"
        }

        // Get current user profile picture
        db.collection("users").document(uid).get().addOnSuccessListener {
            profilePic = it["profilePic"] as String
        }

        // Get parents data
        db.collection("users").get().addOnSuccessListener {
            if (level == 0L) {
                for (document in it!!.documents) {
                    if ((document["level"] as Long).toInt() == 1) {
                        mParentList.add(
                            ClassDetailItemCheckThumbnailDao(
                                document["name"] as String,
                                document["child"] as String,
                                document["profilePic"] as String,
                                userId = document.id
                            )
                        )
                    }
                }
                mParentAdapter.notifyDataSetChanged()
            } else {
                for (document in it!!.documents) {
                    if ((document["level"] as Long).toInt() == 0) {
                        mParentList.add(
                            ClassDetailItemCheckThumbnailDao(
                                document["name"] as String,
                                document["role.className"] as String,
                                document["profilePic"] as String,
                                userId = document.id
                            )
                        )
                    }
                }
                mParentAdapter.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            Timber.e(it)
        }

        cbPersonalNoteNewNextAllParent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) mParentList.forEach { it.isChecked = true }
            else mParentList.forEach { it.isChecked = false }
            rvPersonalNoteNewNext.adapter?.notifyDataSetChanged()
        }

        // Alarm Dialog Box
        sPersonalNoteNewNextAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) dateTime = ""
            else {
                val dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                    "Atur Waktu Notifikasi Berulang",
                    "SELESAI",
                    "BATALKAN"
                )

                dateTimeDialogFragment.setTimeZone(TimeZone.getDefault())

                val dateFormat = SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault())

                dateTimeDialogFragment.startAtCalendarView()
                dateTimeDialogFragment.set24HoursMode(true)
                dateTimeDialogFragment.minimumDateTime =
                    GregorianCalendar(2020, Calendar.JANUARY, 1).time

                try {
                    dateTimeDialogFragment.simpleDateMonthAndDayFormat =
                        SimpleDateFormat("dd MMMM", Locale.getDefault())
                } catch (e: SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException) {
                    Log.e("error", e.message)
                }


                dateTimeDialogFragment.setOnButtonClickListener(object :
                    SwitchDateTimeDialogFragment.OnButtonClickListener {
                    override fun onPositiveButtonClick(date: Date?) {
                        tvPersonalNoteNewNextAlarmContent.visibility = View.VISIBLE
                        tvPersonalNoteNewNextAlarmContent.text = dateFormat.format(date)
                        tvPersonalNoteNewNextAlarmContent.setTextColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.colorRed
                            )
                        )
                        dateTime = dateFormat.format(date)
                    }

                    override fun onNegativeButtonClick(date: Date?) {}
                })

                dateTimeDialogFragment.show(supportFragmentManager, "")
            }
        }

        // Share Personal Note Button
        btnPersonalNoteNewNextDone.setOnClickListener {
            if (attachments.size != 0) {
                for (i in 0..(attachments.size - 1)) {
                    if (attachments[i].category == 1) {
                        storageReference = FirebaseStorage.getInstance()
                            .getReference("images/note/${attachments[i].fileName}")
                    } else if (attachments[i].category == 2) {
                        storageReference =
                            FirebaseStorage.getInstance().reference.child("files/note/${attachments[i].fileName}")
                    } else {

                    }

                    // Upload and get the download URL
                    storageReference.putFile(Uri.parse(attachments[i].path))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                storageReference.downloadUrl.addOnSuccessListener {
                                    counter++
                                    if (task.isSuccessful) {
                                        savedImageUri.add(it.toString())
                                    } else {
                                        storageReference.delete()
                                        Toast.makeText(
                                            this,
                                            "Couldn't save " + attachments[i].fileName,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    if (counter == attachments.size) {
                                        saveDataToFirestore()
                                    }
                                }
                            } else {
                                counter++
                            }
                        }
                }
            } else {
                val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
                val uid = sharePref.getString("uid", "") as String
                var data = hashMapOf<String, Any>()

                if (lev == 0L) {
                    data = hashMapOf(
                        "content" to content,
                        "parent_ids" to mParentList.filter { it.isChecked }.map { it.userId },
                        "profilePic" to profilePic,
                        "teacher_ids" to arrayListOf<String>(uid),
                        "time" to tvPersonalNoteNewNextAlarmContent.text.toString(),
                        "upload_time" to Timestamp(Date()),
                        "attachments" to attachments
                    )
                } else {
                    data = hashMapOf(
                        "noteTitle" to nm!!,
                        "content" to content,
                        "parent_ids" to arrayListOf<String>(uid),
                        "profilePic" to profilePic,
                        "teacher_ids" to mParentList.filter { it.isChecked }.map { it.userId },
                        "time" to tvPersonalNoteNewNextAlarmContent.text.toString(),
                        "upload_time" to Timestamp(Date()),
                        "attachments" to attachments
                    )
                }

                db.collection("notes").add(
                    data
                ).addOnSuccessListener {
                    val contentId = it.id

                    // Send Notification
                    var notifData = hashMapOf<String, Any>(
//                        "content" to "Catatan Pribadi: \"" + content + "\"",
                        "content" to content,
                        "title" to sharePref.getString("name","Teacher") as String,
                        "date" to Timestamp(Date()),
                        "profilePic" to profilePic,
                        "from" to uid,
                        "to" to mParentList.map { it.userId },
                        "type" to 1,
                        "contentId" to contentId
                    )

                    db.collection("notifications").add(notifData)

                    Log.d("PersonalNoteNewActivity", "Note sent successfully")
                }.addOnFailureListener {
                    Log.d("PersonalNoteNewActivity", "Note is failed to be sent")
                }

                // Success dialog
                val builder = AlertDialog.Builder(this@PersonalNoteNewNextActivity)

                builder.setMessage("Catatan berhasil disampaikan")
                builder.setPositiveButton("OK", null)

                val dialog: AlertDialog = builder.create()
                dialog.show()

                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    dialog.dismiss()
                    finish()
                }
                positiveButton.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            }
        }
    }

    private fun initRecycler() {
        DataDummy.noteAttachmentData.clear()
        mParentAdapter =
            ClassDetailCheckThumbnailAdapter(mParentList, this@PersonalNoteNewNextActivity, this)
        rvPersonalNoteNewNext.apply {
            layoutManager = LinearLayoutManager(
                this@PersonalNoteNewNextActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mParentAdapter
        }
    }

    private fun saveDataToFirestore() {
        for (i in 0..attachments.size - 1) attachments[i].path = savedImageUri[i]

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        val uid = sharePref.getString("uid", "") as String
        var data = hashMapOf<String, Any>()

        if (lev == 0L) {
            data = hashMapOf(
                "content" to content,
                "parent_ids" to mParentList.filter { it.isChecked }.map { it.userId },
                "profilePic" to profilePic,
                "teacher_ids" to arrayListOf<String>(uid),
                "time" to tvPersonalNoteNewNextAlarmContent.text.toString(),
                "upload_time" to Timestamp(Date()),
                "attachments" to attachments
            )
        } else {
            data = hashMapOf(
                "noteTitle" to nm!!,
                "content" to content,
                "parent_ids" to arrayListOf<String>(uid),
                "profilePic" to profilePic,
                "teacher_ids" to mParentList.map { it.userId },
                "time" to tvPersonalNoteNewNextAlarmContent.text.toString(),
                "upload_time" to Timestamp(Date()),
                "attachments" to attachments
            )
        }

        db.collection("notes").add(
            data
        ).addOnSuccessListener {
            val contentId = it.id

            // Send Notification
            var notifData = hashMapOf<String, Any>(
//                "content" to "Catatan Pribadi: \"" + content + "\"",
                "content" to content,
                "title" to sharePref.getString("name","Teacher") as String,
                "date" to Timestamp(Date()),
                "profilePic" to profilePic,
                "from" to uid,
                "to" to mParentList.map { it.userId },
                "type" to 1,
                "contentId" to contentId
            )
            db.collection("notifications").add(notifData)

            Log.d("PersonalNoteNewActivity", "Note sent successfully")
        }.addOnFailureListener {
            Log.d("PersonalNoteNewActivity", "Note is failed to be sent")
        }

        // Success dialog
        val builder = AlertDialog.Builder(this@PersonalNoteNewNextActivity)

        builder.setMessage("Catatan berhasil disampaikan")
        builder.setPositiveButton("OK", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        positiveButton.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorSuperDarkBlue
            )
        )
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
        mParentList.forEach {
            if (it.isChecked) isCheckedItem = true
        }


        if (isCheckedItem) {
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

        builder.setMessage("Catatan berhasil disampaikan")
        builder.setPositiveButton("OK", null)

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