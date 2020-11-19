package com.ebukom.arch.ui.register.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ebukom.R
import com.ebukom.arch.ui.forgotpassword.verification.VerificationActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register_parent.*
import kotlinx.android.synthetic.main.activity_register_parent.etRegisterParentName
import kotlinx.android.synthetic.main.activity_register_parent.etRegisterParentPhone
import kotlinx.android.synthetic.main.activity_register_parent.toolbar
import kotlinx.android.synthetic.main.bottom_sheet_register_parent.view.*

class RegisterParentActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    var eskul: ArrayList<String> = arrayListOf()
    private var allEskul: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_parent)

        initToolbar()

        /**
         * Google Authentication
         */
        auth = FirebaseAuth.getInstance() // Firebase authentication
        db = FirebaseFirestore.getInstance() // Firestore

        /**
         * Eskul form field
         */
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_register_parent, null)
        bottomSheetDialog.setContentView(view)

        btnRegisterParentEskul.setOnClickListener {
            bottomSheetDialog.show()
        }

        view.btnRegisterParentBottomSheetDone.setOnClickListener {

            if (view.cbRegisterParentBottomSheetRobotik.isChecked) eskul.add("Robotik")
            if (view.cbRegisterParentBottomSheetDuniaKomik.isChecked) eskul.add("Dunia Komik")
            if (view.cbRegisterParentBottomSheetLittleDesigner.isChecked) eskul.add("Little Designer")
            if (view.cbRegisterParentBottomSheetNimsGitar.isChecked) eskul.add("Nims - Gitar")
            if (view.cbRegisterParentBottomSheetTahfizh.isChecked) eskul.add("Tahfizh")
            if (view.cbRegisterParentBottomSheetMemanah.isChecked) eskul.add("Memanah")
            if (view.cbRegisterParentBottomSheetRenang.isChecked) eskul.add("Renang")
            if (view.cbRegisterParentBottomSheetTaekwondo.isChecked) eskul.add("Taekwondo")
            if (view.cbRegisterParentBottomSheetFutsal.isChecked) eskul.add("Futsal")
            if (view.cbRegisterParentBottomSheetBuluTangkis.isChecked) eskul.add("Bulu Tangkis")
            if (view.cbRegisterParentBottomSheetCookieCookiePastry.isChecked) eskul.add("Cookie-Cookie Pastry")

            allEskul = eskul.distinct().toString()
            allEskul = allEskul?.substring(1, allEskul!!.length - 1)
            btnRegisterParentEskul.text = allEskul

            bottomSheetDialog.dismiss()
        }

//        if (eskul.isNotEmpty()) {
//            isChosen = true
//            mParentList.clear()
//            addData(allEskul!!, mClass)
//            mParentAdapter.notifyDataSetChanged()
//        }

        register()

        /**
         * Tap on LOGIN button
         */
        btnRegisterParentLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        /**
         * Text watcher for error message to be invisible
         */
        etRegisterParentName.addTextChangedListener(textWatcher)
        etRegisterParentChild.addTextChangedListener(textWatcher)
        etRegisterParentPhone.addTextChangedListener(textWatcher)
        etRegisterParentPassword.addTextChangedListener(textWatcher)
        etRegisterParentConfirmPassword.addTextChangedListener(textWatcher)
    }

    /**
     * Customized action bar
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
        btnRegisterParentRegister.setOnClickListener {
            if (isValid()) {
                // reformat phone to email like 08xx@phone.id
                var phoneMail = etRegisterParentPhone.text.toString() + "@phone.id"

                auth.createUserWithEmailAndPassword(
                    phoneMail,
                    etRegisterParentPassword.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // insert to database
                        task.result?.user?.let { it -> insertData(it, eskul) }
                    } else {
//                        Toast.makeText(
//                            this@RegisterParentActivity,
//                            "Registration failed, please try again",
//                            Toast.LENGTH_LONG
//                        ).show()
                        Toast.makeText(
                            this@RegisterParentActivity,
                            task.exception?.localizedMessage,
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
    private fun insertData(user: FirebaseUser, eskul: ArrayList<String>) {

        val userInfo: MutableMap<String, Any> = HashMap()

        userInfo["name"] = etRegisterParentName.text.toString()
        userInfo["childNames"] = etRegisterParentChild.text.toString()
        userInfo["phone"] = etRegisterParentPhone.text.toString()
        userInfo["eskul"] = eskul
        userInfo["level"] = 1 // 0 for parent

//        userInfo["role"] = ""

        db.collection("users").document(user.uid).set(userInfo).addOnSuccessListener {
            Toast.makeText(this@RegisterParentActivity, "Registration success", Toast.LENGTH_LONG)
                .show()

            val intent = Intent(this, VerificationActivity::class.java)
            intent.putExtra("role", 1)
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

        if (etRegisterParentName.text.toString().isEmpty()) {
            tvRegisterParentNameErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvRegisterParentChildErrorMessage.visibility = View.GONE
        if (etRegisterParentChild.text.toString().isEmpty()) {
            tvRegisterParentChildErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvRegisterParentChildErrorMessage.visibility = View.GONE
        if (etRegisterParentPhone.text.toString().isEmpty()) {
            tvRegisterParentPhoneErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvRegisterParentPhoneErrorMessage.visibility = View.GONE
        if (etRegisterParentPassword.text.toString().isEmpty()) {
            tvRegisterParentPasswordErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvRegisterParentNameErrorMessage.visibility = View.GONE
        if (etRegisterParentConfirmPassword.text.toString()
                .isEmpty() || etRegisterParentConfirmPassword.text.toString() != etRegisterParentPassword.text.toString()
        ) {
            tvRegisterParentConfirmPasswordErrorMessage.visibility = View.VISIBLE
            isValid = false
        } else tvRegisterParentConfirmPasswordErrorMessage.visibility = View.GONE

        return isValid
    }

    /**
     * Error message will be invisible when a text is typed
     */
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etRegisterParentName.text.toString()
                    .isNotEmpty() || etRegisterParentChild.text.toString()
                    .isNotEmpty() || etRegisterParentPhone.text.toString()
                    .isNotEmpty() || etRegisterParentPassword.text.toString()
                    .isNotEmpty() || etRegisterParentConfirmPassword.text.toString().isNotEmpty()
            ) {
                tvRegisterParentNameErrorMessage.visibility = View.GONE
                tvRegisterParentChildErrorMessage.visibility = View.GONE
                tvRegisterParentPhoneErrorMessage.visibility = View.GONE
                tvRegisterParentPasswordErrorMessage.visibility = View.GONE
                tvRegisterParentConfirmPasswordErrorMessage.visibility = View.GONE
            }
        }
    }
}
