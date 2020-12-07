package com.ebukom.arch.ui.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ebukom.R
import com.ebukom.arch.ui.chooseclass.ChooseClassActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.ebukom.arch.ui.main.MainActivity
import com.ebukom.arch.ui.welcome.WelcomeActivity
import com.ebukom.data.DataDummy
import com.ebukom.data.Databases
//import com.ebukom.data.buildClassDummy
import com.ebukom.data.buildParentNameDummy
import com.ebukom.data.buildParentNoteDummy
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    lateinit var sharePref: SharedPreferences
    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

        DataDummy.parentNameData.buildParentNameDummy()
        DataDummy.noteAcceptedData.buildParentNoteDummy(this)

        val pos = Handler().postDelayed({
            if (sharePref.getBoolean("isLogin", false)) {
                if (sharePref.getInt("level", 0) == 2) {
//                    startActivity(Intent(this, MainAdminActivity::class.java))
                } else {
                    startActivity(Intent(this, ChooseClassActivity::class.java))
                }
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT)

        /**
         * Generating classes database (uncomment for the first time)
         */
//        Databases().initClassesData()
//        Databases().initSubjectMaterialsData()
    }

}
