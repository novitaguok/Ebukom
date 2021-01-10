package com.ebukom.arch.ui.register.school

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ebukom.R
import com.ebukom.arch.dao.firebase.RegisterRolesDao
import com.ebukom.arch.dao.firebase.RegisterSchoolRequestDao
import com.ebukom.arch.ui.forgotpassword.verification.VerificationActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register_school.*
import kotlinx.android.synthetic.main.bottom_sheet_register_school.*
import kotlinx.android.synthetic.main.bottom_sheet_register_school.view.*
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.io.File


class RegisterSchoolActivity : AppCompatActivity() {

    private var imagePath: String? = null
    private var imageFile: File? = null
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
        tvRegisterSchoolAddPhoto.setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
            val perms = arrayOf(
                Manifest.permission.CAMERA
            )
            if (EasyPermissions.hasPermissions(this, *perms)) {
                ImagePicker.with(this)
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start()
            } else {
                EasyPermissions.requestPermissions(
                    this, "camera",
                    611, *perms)
            }
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

        btnRegisterSchoolLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        /**
         * Text watcher for error message to be invisible
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
     * Register as teacher
     */
    private fun register() {
        btnRegisterSchoolRegister.setOnClickListener {
            if (isValid()) {
                if (reformatPhoneNumber(etRegisterSchoolPhone.text.toString()) != null) {
                    if (imageFile != null) {
                        val data = RegisterSchoolRequestDao(
                            etRegisterSchoolName.text.toString(),
                            RegisterRolesDao(
                                "",
                                role
                            ),
                            reformatPhoneNumber(etRegisterSchoolPhone.text.toString())!!,
                            etRegisterSchoolPassword.text.toString(),
                            imagePath.toString(),
                            0
                        )

                        val intent = Intent(this, VerificationActivity::class.java)
                        intent.putExtra("layout", VerificationActivity.LAYOUT_REGISTER)
                        intent.putExtra("data", data)
                        startActivity(intent)
                    } else {
                        val data = RegisterSchoolRequestDao(
                            etRegisterSchoolName.text.toString(),
                            RegisterRolesDao(
                                "",
                                role
                            ),
                            reformatPhoneNumber(etRegisterSchoolPhone.text.toString())!!,
                            etRegisterSchoolPassword.text.toString(),
                            "",
                            0
                        )

                        val intent = Intent(this, VerificationActivity::class.java)
                        intent.putExtra("layout", VerificationActivity.LAYOUT_REGISTER)
                        intent.putExtra("data", data)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    fun reformatPhoneNumber(phone: String): String? {
        if (phone[0] == '0' && phone[1] == '8') {
            return phone.replaceRange(0, 1, "+62")
        } else if (phone[0] == '6' && phone[1] == '2') {
            return phone.replaceRange(0, 1, "+6")
        } else if (phone[0] == '+' && phone[1] == '6') {
            return phone
        } else {
            return null
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
     * Error message be invisible
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = ImagePicker.getFile(data)
                imagePath = ImagePicker.getFilePath(data)

                ivRegisterSchoolProfilePicture.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Timber.e(ImagePicker.getError(data))
            } else {
                Timber.e("Failed get Image")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
