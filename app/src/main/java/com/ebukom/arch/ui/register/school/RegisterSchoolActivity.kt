package com.ebukom.arch.ui.register.school

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ebukom.R
import com.ebukom.arch.ui.forgotpassword.verification.VerificationActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register_school.*
import kotlinx.android.synthetic.main.activity_register_school.toolbar
import kotlinx.android.synthetic.main.bottom_sheet_register_school.*
import kotlinx.android.synthetic.main.bottom_sheet_register_school.view.*

class RegisterSchoolActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    var role: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_school)

        initToolbar()

        /**
         * Google Authentication
         */
        auth = FirebaseAuth.getInstance() // Firebase authentication
        db = FirebaseFirestore.getInstance() // Firestore

        /**
         * Role dropdown
         */
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_register_school, null)
        bottomSheetDialog.setContentView(view)
        btnRegisterSchoolRole.setOnClickListener {
            bottomSheetDialog.show()
        }
        view.rbGroupRegisterSchool.setOnCheckedChangeListener { _, checkedId ->
            if (R.id.rbRegisterSchoolBottomSheetKrypton == checkedId)
                role = "Guru Kelas 1 Krypton"
            else if (R.id.rbRegisterSchoolBottomSheetXenon == checkedId)
                role = "Guru Kelas 1 Xenon"
            else if (R.id.rbRegisterSchoolBottomSheetArgon == checkedId)
                role = "Guru Kelas 1 Argon"
            else if (R.id.rbRegisterSchoolBottomSheetTitanium == checkedId)
                role = "Guru Kelas 2 Titanium"
            else if (R.id.rbRegisterSchoolBottomSheetNeon == checkedId)
                role = "Guru Kelas 2 Neon"
            else if (R.id.rbRegisterSchoolBottomSheetHelium == checkedId)
                role = "Guru Kelas 2 Helium"
            else if (R.id.rbRegisterSchoolBottomSheetArgentum == checkedId)
                role = "Guru Kelas 3 Argentum"
            else if (R.id.rbRegisterSchoolBottomSheetAurum == checkedId)
                role = "Guru Kelas 3 Aurum"
            else if (R.id.rbRegisterSchoolBottomSheetSelenium == checkedId)
                role = "Guru Kelas 3 Selenium"

            tvRegisterSchoolNameErrorMessage.visibility = View.GONE
            tvRegisterSchoolRoleErrorMessage.visibility = View.GONE
            tvRegisterSchoolPhoneErrorMessage.visibility = View.GONE
            tvRegisterSchoolPasswordErrorMessage.visibility = View.GONE
            tvRegisterSchoolConfirmPasswordErrorMessage.visibility = View.GONE

            btnRegisterSchoolRole.text = role
            btnRegisterSchoolRole.setTextColor(Color.parseColor("#000000"))
            btnRegisterSchoolRole.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
            btnRegisterSchoolRole.setTypeface(btnRegisterSchoolRole.typeface, Typeface.NORMAL)

            bottomSheetDialog.dismiss()
        }

        register()

//        btnRegisterSchoolRegister.setOnClickListener {
//            val intent = Intent(this, VerificationActivity::class.java)
//            intent.putExtra("Layout", 1)
//            startActivity(intent)
//        }
        btnRegisterSchoolLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        /**
         * Text watcher for error message to be invisibled
         */
        etRegisterSchoolName.addTextChangedListener(textWatcher)
        etRegisterSchoolPhone.addTextChangedListener(textWatcher)
        etRegisterSchoolPassword.addTextChangedListener(textWatcher)
        etRegisterSchoolConfirmPassword.addTextChangedListener(textWatcher)
    }

    /**
     * Action bar
     */
    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * Register as Teacher
     */
    private fun register() {
        btnRegisterSchoolRegister.setOnClickListener {
            if (isValid()) {
                // reformat phone to email like 08xx@phone.id
                var phoneMail = etRegisterSchoolPhone.text.toString() + "@phone.id"

                auth.createUserWithEmailAndPassword(
                    phoneMail,
                    etRegisterSchoolPassword.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // insert to database
                        task.result?.user?.let { it -> insertData(it, role) }
                    } else {
                        Log.e("Error", task.exception?.message)
//                        Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        Toast.makeText(
                            this@RegisterSchoolActivity,
                            "Registration failed, please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    /**
     * Insert data to Firestore
     */
    private fun insertData(user: FirebaseUser, role: String) {

        val userInfo: MutableMap<String, Any> = HashMap()

        userInfo["name"] = etRegisterSchoolName.text.toString()
        userInfo["phone"] = etRegisterSchoolPhone.text.toString()
        userInfo["role"] = role
        userInfo["level"] = 0 // 0 for teacher

        userInfo["childNames"] = ""
//        userInfo["eskul"] =

        db.collection("users").document(user.uid).set(userInfo).addOnSuccessListener {
            Toast.makeText(this@RegisterSchoolActivity, "Registration success", Toast.LENGTH_LONG)
                .show()

            val intent = Intent(this, VerificationActivity::class.java)
            intent.putExtra("role", 0)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            // TODO if Failure
        }
    }

    /**
     * Show error message if the form is invalid
     */
    private fun isValid(): Boolean {
        var isValid = true

        if (etRegisterSchoolName.text.toString().isEmpty()) {
            tvRegisterSchoolNameErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvRegisterSchoolNameErrorMessage.visibility = View.GONE
        if (role == "") {
            tvRegisterSchoolRoleErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvRegisterSchoolNameErrorMessage.visibility = View.GONE
        if (etRegisterSchoolPhone.text.toString().isEmpty()) {
            tvRegisterSchoolPhoneErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvRegisterSchoolNameErrorMessage.visibility = View.GONE
        if (etRegisterSchoolPassword.text.toString().isEmpty()) {
            tvRegisterSchoolPasswordErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvRegisterSchoolNameErrorMessage.visibility = View.GONE
        if (etRegisterSchoolConfirmPassword.text.toString()
                .isEmpty() || etRegisterSchoolConfirmPassword.text.toString() != etRegisterSchoolPassword.text.toString()
        ) {
            tvRegisterSchoolConfirmPasswordErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvRegisterSchoolNameErrorMessage.visibility = View.GONE

        return isValid
    }

    /**
     *
     */
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etRegisterSchoolName.text.toString()
                    .isNotEmpty() || rbGroupRegisterSchool != null || etRegisterSchoolPhone.text.toString()
                    .isNotEmpty() || etRegisterSchoolPassword.text.toString()
                    .isNotEmpty() || etRegisterSchoolConfirmPassword.text.toString().isNotEmpty()
            ) {
                tvRegisterSchoolNameErrorMessage.visibility = View.GONE
                tvRegisterSchoolRoleErrorMessage.visibility = View.GONE
                tvRegisterSchoolPhoneErrorMessage.visibility = View.GONE
                tvRegisterSchoolPasswordErrorMessage.visibility = View.GONE
                tvRegisterSchoolConfirmPasswordErrorMessage.visibility = View.GONE
            }
        }
    }
}
