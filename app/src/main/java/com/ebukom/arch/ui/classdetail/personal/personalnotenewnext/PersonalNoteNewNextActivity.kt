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
import com.ebukom.arch.ui.classdetail.ClassDetailCheckAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailCheckThumbnailAdapter
import com.ebukom.data.DataDummy
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.activity_personal_note_new_next.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PersonalNoteNewNextActivity : AppCompatActivity(), ClassDetailCheckAdapter.OnCheckListener {

    private val mParentList: ArrayList<ClassDetailItemCheckThumbnailDao> = arrayListOf()
    lateinit var mParentAdapter: ClassDetailCheckThumbnailAdapter
    lateinit var content: String
    lateinit var dateTime: String
    lateinit var attachments: List<ClassDetailAttachmentDao>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_note_new_next)

        // Intent from PersonalNoteNewActivity
        content = intent?.extras?.getString("content", "") ?: ""
        attachments = intent?.getSerializableExtra("attachments") as List<ClassDetailAttachmentDao>
        dateTime = ""

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

        if (sharePref.getInt("level", 0) == 1) {
            tvToolbarTitle.text = "Buat Catatan untuk Guru"
            tvPersonalNoteNewNextAllParent.text = "Semua Guru"

            // Share Personal Note Button
            btnPersonalNoteNewNextDone.setOnClickListener {
                sendNote()
            }
        } else {
            // Share Personal Note Button
            btnPersonalNoteNewNextDone.setOnClickListener {
                sendNote()
            }

        }

        initToolbar()

        // Parent Names
        mParentList.addAll(DataDummy.parentNameData)
        mParentAdapter = ClassDetailCheckThumbnailAdapter(mParentList, this@PersonalNoteNewNextActivity)
        rvPersonalNoteNewNext.apply {
            layoutManager = LinearLayoutManager(
                this@PersonalNoteNewNextActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mParentAdapter
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

    private fun sendNote() {
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