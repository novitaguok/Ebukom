package com.ebukom.arch.ui.register.parent

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.ebukom.R
import com.ebukom.arch.dao.firebase.RegisterParentRequestDao
import com.ebukom.arch.ui.forgotpassword.verification.VerificationActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register_parent.*
import kotlinx.android.synthetic.main.activity_register_parent.etRegisterParentName
import kotlinx.android.synthetic.main.activity_register_parent.etRegisterParentPhone
import kotlinx.android.synthetic.main.activity_register_parent.toolbar
import kotlinx.android.synthetic.main.bottom_sheet_register_parent.view.*
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.io.File

class RegisterParentActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private var imagePath: String? = null
    private var imageFile: File? = null
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

        tvRegisterParentAddPhoto.setOnClickListener {
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
            btnRegisterParentEskul.setTextColor(Color.parseColor("#808080"))

            bottomSheetDialog.dismiss()
        }

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
     * Register as parent
     */
    private fun register() {
        btnRegisterParentRegister.setOnClickListener {

//            Toast.makeText(applicationContext, "Test", Toast.LENGTH_SHORT).show()

            if (isValid()) {
                if (reformatPhoneNumber(etRegisterParentPhone.text.toString()) != null) {
                    if (imageFile != null) {
                        loading.visibility = View.VISIBLE
                        FirebaseStorage.getInstance()
                            .getReference("images/profile/")
                            .putFile(Uri.fromFile(imageFile))
                            .addOnSuccessListener {
                                val data = RegisterParentRequestDao(
                                    etRegisterParentName.text.toString(),
                                    etRegisterParentChild.text.toString(),
                                    reformatPhoneNumber(etRegisterParentPhone.text.toString())!!,
                                    eskul,
                                    etRegisterParentPassword.text.toString(),
                                    "",
                                    1
                                )

                                val intent = Intent(this, VerificationActivity::class.java)
                                intent.putExtra("layout", VerificationActivity.LAYOUT_REGISTER)
                                intent.putExtra("data", data)
                                startActivity(intent)
                            }
                            .addOnFailureListener {
                                Timber.e(it)
                            }
                    } else {
                        val data = RegisterParentRequestDao(
                            etRegisterParentName.text.toString(),
                            etRegisterParentChild.text.toString(),
                            reformatPhoneNumber(etRegisterParentPhone.text.toString())!!,
                            eskul,
                            etRegisterParentPassword.text.toString(),
                            "",
                            1
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = ImagePicker.getFile(data)
                imagePath = ImagePicker.getFilePath(data)

                ivRegisterParentProfilePicture.setImageURI(fileUri)
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
