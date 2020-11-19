package com.ebukom.arch.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ebukom.R
import com.ebukom.arch.ui.chooseclass.ChooseClassActivity
import com.ebukom.arch.ui.forgotpassword.sendcode.SendCodeActivity
import com.ebukom.arch.ui.joinclass.JoinClassActivity
import com.ebukom.arch.ui.register.parent.RegisterParentActivity
import com.ebukom.arch.ui.register.school.RegisterSchoolActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.toolbar
import kotlinx.android.synthetic.main.activity_register_school.*
import kotlinx.android.synthetic.main.bottom_sheet_login.view.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    //    var databaseReference: DatabaseReference? = null
    lateinit var sharePref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initToolbar()

        /**
         * Authentication
         */
        auth = FirebaseAuth.getInstance() // Firebase authentication
        db = FirebaseFirestore.getInstance() // Firestore

        login()

        /**
         * Tap on REGISTER button
         */
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_login, null)

        bottomSheetDialog.setContentView(view)
        btnLoginRegister.setOnClickListener {
            bottomSheetDialog.show()
        }
        view.tvRegisSchool.setOnClickListener {
            bottomSheetDialog.dismiss()
            startActivity(Intent(this, RegisterSchoolActivity::class.java))
        }
        view.tvRegisParent.setOnClickListener {
            bottomSheetDialog.dismiss()
            startActivity(Intent(this, RegisterParentActivity::class.java))
        }

        sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        // Intent to Choose Class
//        btnLoginLogin.setOnClickListener {
//
//            val phone = etLoginPhone.text.toString()
//
//            loading.visibility = View.VISIBLE
//            if (etLoginPhone.text.toString() == "000") {
//                if (etLoginPassword.text.toString() == "000") {
//                    // Teacher
//                    sharePref.edit().apply {
//                        putBoolean("isLogin", true)
//                        putString("phone", phone)
//                        putInt("level", 0)
//                    }.apply()
//
//                    Handler().postDelayed({
//                        loading.visibility = View.GONE
//                        startActivity(Intent(this, ChooseClassActivity::class.java))
//                        finish()
//                    }, 1000)
//
//                } else {
//                    if (etLoginPassword.toString().isEmpty()) tvLoginPasswordErrorMessage.text =
//                        "Kata sandi tidak boleh kosong"
//                    tvLoginPhoneErrorMessage.visibility = View.GONE
//                    tvLoginPasswordErrorMessage.visibility = View.VISIBLE
//                }
//            } else if (etLoginPhone.text.toString() == "123") {
//                // Parent
//                if (etLoginPassword.text.toString() == "123") {
//                    sharePref.edit().apply {
//                        putBoolean("isLogin", true)
//                        putString("phone", phone)
//                        putInt("level", 1)
//                    }.apply()
//
//                    Handler().postDelayed({
//                        loading.visibility = View.GONE
//                        startActivity(Intent(this, ChooseClassActivity::class.java))
//                        finish()
//                    }, 1000)
//                } else {
//                    if (etLoginPassword.toString().isEmpty()) tvLoginPasswordErrorMessage.text =
//                        "Kata sandi tidak boleh kosong"
//                        tvLoginPhoneErrorMessage.visibility = View.GONE
//                        tvLoginPasswordErrorMessage.visibility = View.VISIBLE
//                }
//            } else if (etLoginPhone.text.toString() == "456") {
//                // Admin
//                if (etLoginPassword.text.toString() == "456") {
//                    sharePref.edit().apply {
//                        putBoolean("isLogin", true)
//                        putString("phone", phone)
//                        putInt("level", 2)
//                    }.apply()
//
//                    Handler().postDelayed({
//                        loading.visibility = View.GONE
//                        startActivity(Intent(this, MainAdminActivity::class.java))
//                        finish()
//                    }, 1000)
//                } else {
//                    if (etLoginPassword.toString().isEmpty()) tvLoginPasswordErrorMessage.text =
//                        "Kata sandi tidak boleh kosong"
//                        tvLoginPhoneErrorMessage.visibility = View.GONE
//                        tvLoginPasswordErrorMessage.visibility = View.VISIBLE
//                }
//            } else {
//                Handler().postDelayed({
//                    loading.visibility = View.GONE
//                    if (etLoginPhone.toString().isEmpty() && etLoginPassword.toString().isEmpty()) {
//                        tvLoginPhoneErrorMessage.text = "Nomor telepon tidak boleh kosong"
//                        tvLoginPasswordErrorMessage.text = "Kata sandi tidak boleh kosong"
//                    }
//                    tvLoginPhoneErrorMessage.visibility = View.VISIBLE
//                    tvLoginPasswordErrorMessage.visibility = View.VISIBLE
//                }, 1000)
//            }
//        }

        // Intent to Forget Password Send Code page
        tvLoginForgetPassword.setOnClickListener {
            val intent = Intent(this, SendCodeActivity::class.java)
            startActivity(intent)
        }

        /**
         * Text watcher
         */
        etLoginPhone.addTextChangedListener(textWatcher)
        etLoginPassword.addTextChangedListener(textWatcher)
    }

    /**
     * Custom action bar
     */
    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, ChooseClassActivity::class.java))
        }
    }

    /**
     * Login
     */
    private fun login() {
        btnLoginLogin.setOnClickListener {

            val phoneMail = etLoginPhone.text.toString() + "@phone.id"

            if (isValid()) {
                auth.signInWithEmailAndPassword(
                    phoneMail,
                    etLoginPassword.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                        startActivity(Intent(this, ChooseClassActivity::class.java))
                        checkUserAccessLevel(task.result?.user!!.uid)
                    } else {
                        Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed, please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    /**
     * Check if login form is empty
     */
    private fun isValid(): Boolean {
        var isValid = true

        if (etLoginPhone.text.toString().isEmpty()) {
            tvLoginPhoneErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvLoginPhoneErrorMessage.visibility = View.GONE
        if (etLoginPassword.text.toString().isEmpty()) {
            tvLoginPasswordErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvLoginPasswordErrorMessage.visibility = View.GONE

        return isValid
    }

    /**
     * Check user access level
     */
    fun checkUserAccessLevel(uid: String) {
        var df: DocumentReference = db.collection("users").document(uid)
        df.get().addOnSuccessListener {
            Log.d("TAG", "onSuccess: " + it.data)

            if (it["level"] == 0) {
                startActivity(Intent(this, ChooseClassActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, ChooseClassActivity::class.java))
                finish()
            }
        }.addOnFailureListener {  }
    }

    /**
     * Error message be invisibled
     */
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etLoginPhone.text.toString().isNotEmpty() || etLoginPassword.text.toString()
                    .isNotEmpty()
            ) {
                tvLoginPhoneErrorMessage.visibility = View.GONE
                tvLoginPasswordErrorMessage.visibility = View.GONE
            }
        }
    }
}
