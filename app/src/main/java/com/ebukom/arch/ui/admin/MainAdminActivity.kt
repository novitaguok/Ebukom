package com.ebukom.arch.ui.admin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ebukom.R
import com.ebukom.arch.ui.admin.adminclassmember.AdminClassMemberFragment
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main_admin.*

//class MainAdminActivity : AppCompatActivity(), OnMoreCallback {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main_admin)
//
//        val schoolFeeInfoFragment = AdminSchoolFeeInfoFragment()
//        val classMemberFragment = AdminClassMemberFragment()
//
//        makeCurrentFragment(schoolFeeInfoFragment)
//        bnAdmin.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.adminSchoolFeeInfo -> makeCurrentFragment(schoolFeeInfoFragment)
//                R.id.admindClassMember -> makeCurrentFragment(classMemberFragment)
//            }
//            true
//        }
//
//        ivAdminHeaderProfilePicture.setOnClickListener {
//            var sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
//            sharePref.edit().remove("level").apply()
//            sharePref.edit().apply {
//                putBoolean("isLogin", false)
//            }.apply()
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }
//    }
//
//    private fun makeCurrentFragment(fragment: Fragment) =
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.flAdmin, fragment)
//            commit()
//        }
//
//    override fun onMoreClicked(id: String, pos: String) {
//
//    }
//
//
//    override fun onBackPressed() {
//
//    }
//}