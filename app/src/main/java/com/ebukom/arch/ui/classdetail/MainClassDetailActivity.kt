package com.ebukom.arch.ui.classdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.material.MaterialFragment
import com.ebukom.arch.ui.classdetail.member.MemberFragment
import com.ebukom.arch.ui.classdetail.personal.PersonalFragment
import com.ebukom.arch.ui.classdetail.school.SchoolFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main_class_detail.*
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

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flClassDetail, fragment)
            commit()
        }

    fun popupMenu() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
        }
        view.tvDeleteInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            val builder = AlertDialog.Builder(this@MainClassDetailActivity)

            builder.setMessage("Apakah Anda yakin ingin menghapus?")
            builder.setPositiveButton("HAPUS") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext,"Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        view.tvCancelInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMoreClicked(id: String) {
        popupMenu()
    }
}
