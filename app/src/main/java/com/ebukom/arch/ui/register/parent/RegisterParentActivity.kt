package com.ebukom.arch.ui.register.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ebukom.R
import com.ebukom.arch.ui.forgotpassword.verification.VerificationActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register_parent.*
import kotlinx.android.synthetic.main.activity_register_parent.etRegisterParentName
import kotlinx.android.synthetic.main.activity_register_parent.etRegisterParentPhone
import kotlinx.android.synthetic.main.activity_register_parent.toolbar
import kotlinx.android.synthetic.main.activity_register_school.*
import kotlinx.android.synthetic.main.bottom_sheet_register_parent.view.*
import java.lang.ref.PhantomReference

class RegisterParentActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_parent)

        initToolbar()

        // Auth
        auth = Firebase.auth

        // Pop Up
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_register_parent, null)
        bottomSheetDialog.setContentView(view)

        btnRegisterParentEskul.setOnClickListener {
            bottomSheetDialog.show()
        }
        view.btnRegisterParentBottomSheetDone.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        btnRegisterParentRegister.setOnClickListener {
            val intent = Intent(this, VerificationActivity::class.java)
            intent.putExtra("Layout", 1)
            startActivity(intent)
        }
        btnRegisterParentLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    /**
     * Authentication with Firebase
     */
    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
//        updateUI(currentUser)
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun createAccount() {
//        var valid = true

        var name = etRegisterParentName.toString()
        var childName = etRegisterParentChild.toString()
        var phone = etRegisterParentPhone.toString()
        var password = etRegisterParentPassword.toString()
        var confirmPassword = etRegisterParentConfirmPassword.toString()

        if (name.isEmpty()) {
//            tvRegisterParentNameErrorMessage.visibility = View.VISIBLE
            tilRegisterParentName.error = "Nama lengkap tidak boleh kosong"
        }
        if (childName.isEmpty()) {
            tvRegisterParentChildErrorMessage.visibility = View.VISIBLE
        }
        if (phone.isEmpty()) {
            tvRegisterParentPhoneErrorMessage.visibility = View.VISIBLE
        }
        if (password.isEmpty()) {
            tvRegisterParentPasswordErrorMessage.visibility = View.VISIBLE
        }
        if (confirmPassword.isEmpty() || confirmPassword != password) {
            tvRegisterParentConfirmPasswordErrorMessage.visibility = View.VISIBLE
        }

//        return valid

        auth.createUserWithEmailAndPassword(phone, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser
//                updateUI(user)
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                updateUI(null)
            }
        }
    }

    private fun getUserProfile() {
        val user = Firebase.auth.currentUser
        user?.let {
            val name = user.displayName
            val email = user.email
            val phone = user.phoneNumber
            val photoUrl = user.photoUrl

            val uid = user.uid
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_MULTI_FACTOR = 9005
    }
}
