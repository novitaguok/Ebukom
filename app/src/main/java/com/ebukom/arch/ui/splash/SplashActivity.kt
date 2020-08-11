package com.ebukom.arch.ui.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ebukom.R
import com.ebukom.arch.ui.admin.MainAdminActivity
import com.ebukom.arch.ui.chooseclass.ChooseClassActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.ebukom.arch.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    lateinit var sharePref: SharedPreferences
    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

        val pos = Handler().postDelayed({
            if(sharePref.getBoolean("isLogin", false)){
                if(sharePref.getInt("level", 0) == 2) {
                    startActivity(Intent(this, MainAdminActivity::class.java))
                } else {
                    startActivity(Intent(this, ChooseClassActivity::class.java))
                }
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT)
    }
}
