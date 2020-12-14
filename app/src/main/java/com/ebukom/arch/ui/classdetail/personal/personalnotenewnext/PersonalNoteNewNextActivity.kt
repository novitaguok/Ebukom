package com.ebukom.arch.ui.classdetail.personal.personalnotenewnext

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.ebukom.data.DataDummy
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
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
    lateinit var attachments: List<ClassDetailAttachmentDao>
    val db = FirebaseFirestore.getInstance()
    var nm: String? = null
    var lev: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_new_next)

        // Intent from PersonalNoteNewActivity
        content = intent?.extras?.getString("content", "") ?: ""
        attachments = intent?.getSerializableExtra("attachments") as List<ClassDetailAttachmentDao>
        dateTime = ""

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        val level = sharePref.getLong("level", 0)
        val name = sharePref.getString("name", "")
        lev = level
        nm = name

        initToolbar()
        initRecycler()

        if (level == 1L) {
            tvToolbarTitle.text = "Buat Catatan untuk Guru"
            tvPersonalNoteNewNextAllParent.text = "Semua Guru"

            // Share Personal Note Button
            btnPersonalNoteNewNextDone.setOnClickListener {
                var content = etPersonalNoteNewContent.text.toString()
                sendNote(content)
            }
        } else {
            // Share Personal Note Button
            btnPersonalNoteNewNextDone.setOnClickListener {
                var content = etPersonalNoteNewContent.text.toString()
                sendNote(content)
            }

        }

        /**
         * Get parents data
         */
        db.collection("users").addSnapshotListener { value, error ->
            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            initRecycler()

            if (level == 0L) {
                mParentList.clear()
                for (document in value!!.documents) {
                    if ((document["level"] as Long).toInt() == 1) {
                        mParentList.clear()
                        mParentList.add(
                            ClassDetailItemCheckThumbnailDao(
                                document["name"] as String,
                                document["childNames"] as String,
                                0,
                                userId = document.id
                            )
                        )
                    }
                }
            } else {
                mParentList.clear()
                for (document in value!!.documents) {
                    if ((document["level"] as Long).toInt() == 0) {
                        mParentList.clear()
                        mParentList.add(
                            ClassDetailItemCheckThumbnailDao(
                                document["name"] as String,
                                document["role.className"] as String,
                                0,
                                userId = document.id
                            )
                        )
                    }
                }
            }
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

        cbPersonalNoteNewNextAllParent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) mParentList.forEach { it.isChecked = true }
            else mParentList.forEach { it.isChecked = false }
            rvPersonalNoteNewNext.adapter?.notifyDataSetChanged()
        }
    }

    private fun initRecycler() {
        mParentAdapter =
            ClassDetailCheckThumbnailAdapter(mParentList, this@PersonalNoteNewNextActivity)
        rvPersonalNoteNewNext.apply {
            layoutManager = LinearLayoutManager(
                this@PersonalNoteNewNextActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mParentAdapter
        }
    }

    private fun sendNote(content: String) {
        mParentList.forEach {
            if (it.isChecked) {
                DataDummy.noteSentData.add(
                    ClassDetailPersonalNoteDao(
                        R.drawable.bg_solid_gray,
                        it.name,
                        getString(R.string.announcement_content),
                        arrayListOf(),
                        dateTime,
                        attachments
                    )
                )
                it.isChecked = false
            }
        }

        var data = hashMapOf<String, Any>()
        if (lev == 0L) {
            data = hashMapOf(
                "noteContent" to content,
                "parent_ids" to mParentList,
                "profilePicture" to 0,
                "teacher_ids" to arrayListOf<RegisterSchoolRequestDao>(),
                "time" to tvPersonalNoteNewNextAlarmContent.text.toString(),
                "upload_time" to Timestamp(Date())
            )
        } else {
            data = hashMapOf(
                "noteTitle" to nm!!,
                "noteContent" to content,
                "parent_ids" to arrayListOf<RegisterSchoolRequestDao>(),
                "profilePicture" to 0,
                "teacher_ids" to mParentList,
                "time" to tvPersonalNoteNewNextAlarmContent.text.toString(),
                "upload_time" to Timestamp(Date())
            )
        }

        db.collection("notes").add(
            data
        ).addOnSuccessListener {
            Log.d("PersonalNoteNewActivity", "Note sent successfully")
        }.addOnFailureListener {
            Log.d("PersonalNoteNewActivity", "Note is failed to be sent")
        }

        val builder = AlertDialog.Builder(this@PersonalNoteNewNextActivity)

        builder.setMessage("Catatan berhasil disampaikan ke Ibu Ratu Cinta")
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

        builder.setMessage("Catatan berhasil disampaikan ke Ibu Ratu Cinta")
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