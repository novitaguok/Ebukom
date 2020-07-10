package com.ebukom.arch.ui.classdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.material.MaterialFragment
import com.ebukom.arch.ui.classdetail.member.MemberFragment
import com.ebukom.arch.ui.classdetail.personal.PersonalFragment
import com.ebukom.arch.ui.classdetail.school.SchoolFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main_class_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_header.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_header.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.item_announcement.*
import kotlinx.android.synthetic.main.item_class_green.view.*

class MainClassDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_class_detail)

        val schoolFragment = SchoolFragment()
        val personalFragment = PersonalFragment()
        val materialFragment = MaterialFragment()
        val memberFragment = MemberFragment()

        makeCurrentFragment(schoolFragment)
        bnClassDetail.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.schoolInfo -> makeCurrentFragment(schoolFragment)
                R.id.personalInfo -> makeCurrentFragment(personalFragment)
                R.id.studyMaterial -> makeCurrentFragment(materialFragment)
                R.id.classMember -> makeCurrentFragment(memberFragment)
            }
            true
        }

        // Header
        clClassHeaderClassName.setOnClickListener {
            popupMenu()
        }

        // Class Header Dropdown
        val bottomSheetDialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.bottom_sheet_class_detail_header, null)

        bottomSheetDialog.setContentView(view)

        ivClassHeaderDropdown.setOnClickListener {
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

        val view = layoutInflater.inflate(R.layout.bottom_sheet_class_detail_header, null)

        bottomSheetDialog.setContentView(view)
//
//        ivAnnouncementMoreButton.setOnClickListener {
            bottomSheetDialog.show()
//        }
//
//        view.editInfo.setOnClickListener {
//            bottomSheetDialog.dismiss()
//            Toast.makeText(this, "Edit Info", Toast.LENGTH_LONG).show()
//        }
    }
}
