package com.ebukom.arch.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.ebukom.R
import com.ebukom.arch.ui.chooseclass.ChooseClassActivity
import com.ebukom.arch.ui.forgotpassword.sendcode.SendCodeActivity
import com.ebukom.arch.ui.register.parent.RegisterParentActivity
import com.ebukom.arch.ui.register.school.RegisterSchoolActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_login.*
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

        /**
         * Intent to Forget Password Send Code page
         */
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
            val usersRef: CollectionReference = FirebaseFirestore.getInstance().collection("users")
            val query: Query = usersRef.whereEqualTo("phone", phoneMail)

            if (isValid()) {
                auth.signInWithEmailAndPassword(
                    phoneMail,
                    etLoginPassword.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        checkUserAccessLevel(task.result?.user!!.uid)
                    } else {
                        Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed, please try again",
                            Toast.LENGTH_LONG
                        ).show()

                        query.get().addOnCompleteListener {
                            if (it.isSuccessful) {
                                for (documentSnapshot in it.result!!) {
                                    val phone = documentSnapshot.getString("phone")
                                    if (phone == phoneMail) {
                                        Log.d("LoginActivity", "User Exists")
                                        Toast.makeText(this, "Username exists", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                            if (it.result!!.size() == 0) {
                                Log.d("LoginActivity", "User not Exists")
                                tvLoginPhoneErrorMessage.text =
                                    "Nomor telepon yang Anda masukkan salah"
                                tvLoginPhoneErrorMessage.visibility = View.VISIBLE
                                tvLoginPasswordErrorMessage.text =
                                    "Kata sandi yang Anda masukkan salah"
                                tvLoginPasswordErrorMessage.visibility = View.VISIBLE
                            } else {
                                tvLoginPasswordErrorMessage.visibility = View.GONE
                                tvLoginPasswordErrorMessage.visibility = View.GONE
                            }
                        }
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

            sharePref.edit().apply {
                putBoolean("isLogin", true)
                putString("uid", uid)
                putString("name", it["name"] as String)
                putLong("level",it["level"] as Long)
            }.apply()

            startActivity(Intent(this, ChooseClassActivity::class.java))
            finish()

        }.addOnFailureListener { }
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
