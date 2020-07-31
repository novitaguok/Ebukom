package com.ebukom.arch.ui.classdetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.material.MaterialFragment
import com.ebukom.arch.ui.classdetail.member.MemberFragment
import com.ebukom.arch.ui.classdetail.personal.PersonalFragment
import com.ebukom.arch.ui.classdetail.school.SchoolFragment
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit.SchoolAnnouncementEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoFragment
import com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotoedit.SchoolPhotoEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotonew.SchoolPhotoNewActivity
import com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolscheduleedit.SchoolScheduleEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulenew.SchoolScheduleNewActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main_class_detail.*
import kotlinx.android.synthetic.main.activity_school_schedule_new.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_header.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*

class MainClassDetailActivity : AppCompatActivity(), OnMoreCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_class_detail)

        val schoolFragment = SchoolFragment()
        val personalFragment = PersonalFragment()
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

    fun popupMenuInfo(id: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)
        bottomSheetDialog.setContentView(view)

        if (id == "1") {
            view.tvEditInfo.text = "Edit Jadwal"
            view.tvDeleteInfo.text = "Hapus Jadwal"
        } else if (id == "2") {
            view.tvEditInfo.text = "Edit Informasi Foto"
            view.tvDeleteInfo.text = "Hapus Foto"
        }

        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            if (id == "0") { // Pengumuman
                intent = Intent(this, SchoolAnnouncementEditActivity::class.java)
            } else if (id == "1") { // Jadwal
                intent = Intent(this, SchoolScheduleEditActivity::class.java)
            } else if (id == "2") { // Foto
                intent = Intent(this, SchoolPhotoEditActivity::class.java)
            }

            startActivity(intent)
        }

        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainClassDetailActivity)
            var alert = "pengumuman"

            if (id == "1") {
                alert = "jadwal"
            }

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus $alert ini?")

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show()
        }

        bottomSheetDialog.show()
    }

    override fun onMoreClicked(id: String) {
        popupMenuInfo(id)
    }
}
