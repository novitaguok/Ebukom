package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.SparseIntArray
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAddTemplateActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnewnext.SchoolAnnouncementNewNextActivity
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_announcement_new_next.*
import kotlinx.android.synthetic.main.activity_school_anouncement_new.*
import kotlinx.android.synthetic.main.activity_school_anouncement_new.toolbar
import kotlinx.android.synthetic.main.alert_edit_text.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_school_announcement_template.view.*
import kotlinx.android.synthetic.main.item_announcement_attachment.view.*
import java.util.*


class SchoolAnnouncementNewActivity : AppCompatActivity() {

    private val mAttachmentList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()
    private val mAttachmentAdapter = ClassDetailAttachmentAdapter(mAttachmentList)
    private val mTemplateList: ArrayList<ClassDetailTemplateTextDao> = arrayListOf()
    lateinit var mTemplateAdapter: ClassDetailTemplateAdapter
    var classId: String? = ""
    var eventStart = Timestamp(Date())
    var eventEnd = Timestamp(Date())
    var enabled = false
    var isSetTime = false
    lateinit var bottomSheetDialog: BottomSheetDialog
    val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_anouncement_new)

        initToolbar()
        initRecycler()

        // Intent from SchoolAnnouncementActivity
        val layout = intent?.extras?.getString("layout")
        classId = intent.getStringExtra("classId")

        // Announcement template
        bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(
            R.layout.bottom_sheet_class_detail_school_announcement_template,
            null
        )
        mTemplateAdapter = ClassDetailTemplateAdapter(mTemplateList, this)
        db.collection("announcement_templates").get().addOnSuccessListener {
            mTemplateList.clear()
            for (data in it.documents) {
                mTemplateList.add(
                    ClassDetailTemplateTextDao(
                        data["title"] as String,
                        data["content"] as String
                    )
                )
            }
            mAttachmentAdapter.notifyDataSetChanged()
        }
        btnSchoolAnnouncementNewUseTemplate.setOnClickListener {
            view.rvBottomSheetSchoolAnnouncementTemplate.apply {
                layoutManager =
                    LinearLayoutManager(
                        this.context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                adapter = mTemplateAdapter
            }

            view.tvBottomSheetSchoolAnnouncementTemplateAdd.setOnClickListener {
                startActivity(Intent(this, SchoolAnnouncementAddTemplateActivity::class.java))
            }
            view.tvSchoolAnnouncementTemplateCancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        // Showing datetime picker dialog
        btnSchoolAnnouncementNewTime.setOnClickListener {
            pickTime()
        }

        if (layout == "edit") {
            tvToolbarTitle.text = "Edit Pengumuman"

//            db.collection()
        } else {
            // Go to next page
            btnSchoolAnnouncementNewNext.setOnClickListener {
                val title = etSchoolAnnouncementNewTitle.text.toString()
                val content = etSchoolAnnouncementNewContent.text.toString()
                val intent = Intent(this, SchoolAnnouncementNewNextActivity::class.java)

                if (!isSetTime) {
                    eventStart = Timestamp.now()
                    eventEnd = Timestamp.now()
                }

                intent.putExtra("classId", classId)
                intent.putExtra("title", title)
                intent.putExtra("content", content)
                intent.putExtra("eventStart", eventStart)
                intent.putExtra("eventEnd", eventEnd)
                intent.putExtra("attachments", mAttachmentList)

                startActivity(intent)
                finish()
            }
        }

        // Text watcher
        etSchoolAnnouncementNewTitle.addTextChangedListener(textWatcher)
        etSchoolAnnouncementNewContent.addTextChangedListener(textWatcher)
    }

    private fun pickTime() {
        //            val calendar = CivilCalendar(TimeZone.getDefault(), Locale.ENGLISH).also {
        //                it.year = 2021                       // determines starting year
        //                it.month = 0                         // determines starting month
        //                it.firstDayOfWeek = Calendar.SUNDAY  // sets first day of week to Monday
        //            }

        val callback = RangeDaysPickCallback { start, end ->
            // TODO: 12/26/20 Put after selected date
            eventStart = Timestamp(Date(start.year - 1900, start.month, start.date))
            eventEnd = Timestamp(Date(end.year - 1900, end.month, end.date))

            btnSchoolAnnouncementNewTime.text =
                "${start.date} ${start.monthNameShort} - ${end.date} ${end.monthNameShort}"
    //                btnSchoolAnnouncementNewTime.text = "${start.weekDayName} ${start.month} ${start.date}"
            btnSchoolAnnouncementNewTime.setTextColor(Color.parseColor("#808080"))

            isSetTime = true
        }

        val themeFactory = object : LightThemeFactory() {

            override val calendarViewPickedDayInRangeBackgroundColor: Int
                get() = getColor(R.color.red300)

            override val calendarViewPickedDayBackgroundColor: Int
                get() = getColor(R.color.colorRed)

            override val calendarViewPickedDayInRangeLabelTextColor: Int
                get() = getColor(R.color.gray900)

            override val calendarViewWeekLabelTextColors: SparseIntArray
                get() = SparseIntArray(7).apply {
                    val red = getColor(R.color.colorRed)
                    val blue = getColor(R.color.colorSuperDarkBlue)
                    put(Calendar.SUNDAY, red)
                    put(Calendar.MONDAY, blue)
                    put(Calendar.TUESDAY, blue)
                    put(Calendar.WEDNESDAY, blue)
                    put(Calendar.THURSDAY, blue)
                    put(Calendar.FRIDAY, blue)
                    put(Calendar.SATURDAY, blue)
                }

            override val actionBarTodayTextColor: Int
                get() = getColor(R.color.transparent)

            override val actionBarPositiveTextColor: Int
                get() = getColor(R.color.colorRed)

            override val actionBarNegativeTextColor: Int
                get() = getColor(R.color.colorGray)

            override val selectionBarRangeDaysItemBackgroundColor: Int
                get() = getColor(R.color.red300)

            override val gotoViewTextColor: Int
                get() = getColor(R.color.black)

            override val selectionBarBackgroundColor: Int
                get() = getColor(R.color.colorRed)

            override val calendarViewMonthLabelTextColor: Int
                get() = getColor(R.color.black)
        }

        val today =
            CivilCalendar()  // Causes a Civil date picker, also today as the starting date

        val datePicker = PrimeDatePicker.dialogWith(today)  // or dialogWith(today)
            .pickRangeDays(callback)        // Passing callback is optional, can be set later using setDayPickCallback()
            .applyTheme(themeFactory)          // Optional
            .build()

        datePicker.show(supportFragmentManager, "SchoolAnnouncementNewActivity")
    }

    private fun initRecycler() {
        /**
         * Attachment list
         */
        rvMaterialSubjectAddAttachment.apply {
            layoutManager = LinearLayoutManager(
                this@SchoolAnnouncementNewActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mAttachmentAdapter
        }
        checkAttachmentEmpty()
    }

    /**
     * Text watcher
     */
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etSchoolAnnouncementNewTitle.text.toString()
                    .isNotEmpty() && etSchoolAnnouncementNewContent.text.toString()
                    .isNotEmpty()
            ) {
                btnSchoolAnnouncementNewNext.isEnabled = true
                btnSchoolAnnouncementNewNext.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorSuperDarkBlue
                    )
                )
            } else {
                btnSchoolAnnouncementNewNext.isEnabled = false
                btnSchoolAnnouncementNewNext.setBackgroundColor(
                    Color.parseColor("#828282")
                )
            }
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

    private fun insertAttachment(view: View, path: String) {
        mAttachmentList.clear()
        mAttachmentList.addAll(DataDummy.announcementAttachmentData)
        mAttachmentAdapter.notifyDataSetChanged()
        view.tvItemAnnouncementAttachment?.text = path

        checkAttachmentEmpty()
    }

    fun deleteAttachment(item: ClassDetailAttachmentDao) {
        val builder = AlertDialog.Builder(this@SchoolAnnouncementNewActivity)

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
            tvSchoolAnnouncementNewAttachmentTitle.visibility = View.GONE
        } else {
            tvSchoolAnnouncementNewAttachmentTitle.visibility = View.VISIBLE
        }
    }

    fun setText(announcementTitle: String, announcementContent: String) {
        etSchoolAnnouncementNewTitle.setText(announcementTitle)
        etSchoolAnnouncementNewContent.setText(announcementContent)
        bottomSheetDialog.dismiss()

    }
}

