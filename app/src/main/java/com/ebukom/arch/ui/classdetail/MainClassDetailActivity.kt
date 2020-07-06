package com.ebukom.arch.ui.classdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.material.MaterialFragment
import com.ebukom.arch.ui.classdetail.member.MemberFragment
import com.ebukom.arch.ui.classdetail.personal.PersonalFragment
import com.ebukom.arch.ui.classdetail.school.SchoolFragment
import kotlinx.android.synthetic.main.activity_main_class_detail.*

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
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flClassDetail, fragment)
            commit()
        }

}
