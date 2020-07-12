package com.ebukom.arch.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ebukom.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.bottom_sheet_login.view.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val bottomSheetDialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.bottom_sheet_login, null)

        bottomSheetDialog.setContentView(view)

        btnLoginRegister.setOnClickListener {
            bottomSheetDialog.show()
        }

        view.tvRegisSchool.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Register by School", Toast.LENGTH_LONG).show()
        }

        view.tvRegisParent.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Register by Parents", Toast.LENGTH_LONG).show()
        }
    }
}
