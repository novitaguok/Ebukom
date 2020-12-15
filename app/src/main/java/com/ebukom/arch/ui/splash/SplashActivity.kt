package com.ebukom.arch.ui.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ebukom.R
import com.ebukom.arch.ui.chooseclass.ChooseClassActivity
import com.ebukom.arch.ui.welcome.WelcomeActivity
import com.ebukom.data.DataDummy
import com.ebukom.data.buildParentNameDummy
import com.ebukom.data.buildParentNoteDummy
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging


class SplashActivity : AppCompatActivity() {

    lateinit var sharePref: SharedPreferences
    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

        DataDummy.parentNameData.buildParentNameDummy()
        DataDummy.noteAcceptedData.buildParentNoteDummy(this)

        if (sharePref.getString("token", null).isNullOrEmpty())
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Notification", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                sharePref.edit().putString("token", token).apply()
            })

        val pos = Handler().postDelayed({
            if (sharePref.getBoolean("isLogin", false)) {
                if (sharePref.getLong("level", 0) == 1L) {
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
