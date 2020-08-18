package com.ebukom.arch.ui.classdetail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.dao.ClassDetailPhotoDao
import com.ebukom.arch.dao.ClassDetailScheduleDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.material.MaterialFragment
import com.ebukom.arch.ui.classdetail.member.MemberFragment
import com.ebukom.arch.ui.classdetail.notification.NotificationActivity
import com.ebukom.arch.ui.classdetail.personal.PersonalFragment
import com.ebukom.arch.ui.classdetail.personal.personalnoteedit.PersonalNoteEditActivity
import com.ebukom.arch.ui.classdetail.personal.personalparent.PersonalParentFragment
import com.ebukom.arch.ui.classdetail.school.SchoolFragment
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit.SchoolAnnouncementEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoAdapter
import com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotoedit.SchoolPhotoEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleAdapter
import com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolscheduleedit.SchoolScheduleEditActivity
import com.ebukom.arch.ui.editprofile.EditProfileActivity
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main_class_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_header.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*

class MainClassDetailActivity : AppCompatActivity(), OnMoreCallback {
    companion object {
        var isAnnouncement = true
    }
    private val mAnnouncementList: ArrayList<ClassDetailAnnouncementDao> = DataDummy.announcementData
    private lateinit var mAnnouncementAdapter : SchoolAnnouncementAdapter
    private val mScheduleList: ArrayList<ClassDetailScheduleDao> = DataDummy.scheduleData
    private lateinit var mScheduleAdapter : SchoolScheduleAdapter
    private val mPhotoList: ArrayList<ClassDetailPhotoDao> = DataDummy.photoData
    lateinit var mPhotoAdapter : SchoolPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_class_detail)

        ivClassHeaderNotificationBell.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        ivClassHeaderProfilePicture.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        val schoolFragment = SchoolFragment()
        val personalFragment = if (sharePref.getInt("level", 0) == 1) {
            PersonalParentFragment()
        } else {
            PersonalFragment()
        }
        val materialFragment = MaterialFragment()
        val memberFragment = MemberFragment()

        makeCurrentFragment(schoolFragment)
        bnClassDetail.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.schoolInfo -> makeCurrentFragment(schoolFragment)
                R.id.personalInfo -> makeCurrentFragment(personalFragment)
                R.id.studyMaterial -> makeCurrentFragment(materialFragment)
                R.id.classMember -> makeCurrentFragment(memberFragment)
            }
            true
        }

        // Class Header Dropdown
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_class_detail_header, null)

        bottomSheetDialog.setContentView(view)

        tvClassHeaderClassName.setOnClickListener {
            bottomSheetDialog.show()
        }

        view.llBottomSheetClassDetailHeaderKelas1.setOnClickListener {
//            bottomSheetDialog.dismiss()
            Toast.makeText(this, "A", Toast.LENGTH_LONG).show()
        }

        view.llBottomSheetClassDetailHeaderKelas2.setOnClickListener {
//            bottomSheetDialog.dismiss()
            Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
        }
    }

    // Move fragment
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flClassDetail, fragment)
            commit()
        }

    fun popupMenuInfo(id: String, pos: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)
        bottomSheetDialog.setContentView(view)

        if (id == "1") { // School Schedule
            view.tvEditInfo.text = "Edit Jadwal"
            view.tvDeleteInfo.text = "Hapus Jadwal"
        } else if (id == "2") { // School Photo
            view.tvEditInfo.text = "Edit Informasi Foto"
            view.tvDeleteInfo.text = "Hapus Foto"
        } else if (id == "3") { // Personal Note
            view.tvEditInfo.text = "Edit Catatan"
            view.tvDeleteInfo.text = "Hapus Catatan"
        } else if (!isAnnouncement) { // Personal Note FRAGMENT
            view.tvEditInfo.text = "Edit Catatan"
            view.tvDeleteInfo.text = "Hapus Catatan"
        }

        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            when (id) {
                "0" -> { // School Announcement
                    intent = Intent(this, SchoolAnnouncementEditActivity::class.java)
                }
                "1" -> { // School Schedule
                    intent = Intent(this, SchoolScheduleEditActivity::class.java)
                }
                "2" -> { // School Photo
                    intent = Intent(this, SchoolPhotoEditActivity::class.java)
                }
                "3" -> { // Personal Note
                    intent = Intent(this, PersonalNoteEditActivity::class.java)
                }
            }

            intent.putExtra("pos", pos)
            startActivity(intent)
        }

        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainClassDetailActivity)
            var alert = "pengumuman"

            if (id == "1") {
                alert = "jadwal"
            } else if (id == "3") {
                alert = "catatan"
            } else if (!isAnnouncement) {
                alert = "catatan"
            }

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus $alert ini?")

            builder.setNegativeButton("BATALKAN") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { _, _ ->
                when (id) {
                    "0" -> { // School announcement
                        DataDummy.announcementData.removeAt(pos)

                        mAnnouncementAdapter = SchoolAnnouncementAdapter(mAnnouncementList, this)
                        mAnnouncementAdapter.notifyDataSetChanged()

                        loading_main.visibility = View.VISIBLE
                        Handler().postDelayed({
                            loading_main.visibility = View.GONE
                        }, 1000)
                    }
                    "1" -> {
                        DataDummy.scheduleData.removeAt(pos)

                        mScheduleAdapter = SchoolScheduleAdapter(mScheduleList, this)
                        mScheduleAdapter.notifyDataSetChanged()

                        loading_main.visibility = View.VISIBLE
                        Handler().postDelayed({
                            loading_main.visibility = View.GONE
                        }, 1000)
                    }
                    "2" -> {
                        DataDummy.photoData.removeAt(pos)

                        mPhotoAdapter = SchoolPhotoAdapter(mPhotoList, this)
                        mPhotoAdapter.notifyDataSetChanged()

                        loading_main.visibility = View.VISIBLE
                        Handler().postDelayed({
                            loading_main.visibility = View.GONE
                        }, 1000)
                    }
                }
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

        view.tvCancelInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    override fun onMoreClicked(id: String, pos: Int) {
        popupMenuInfo(id, pos)
    }
}
