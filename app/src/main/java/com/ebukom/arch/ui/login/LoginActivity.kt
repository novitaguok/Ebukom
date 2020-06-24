package com.ebukom.arch.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ebukom.R
import com.ebukom.RegisterActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.login_bottom_sheet.view.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val bottomSheetDialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.login_bottom_sheet, null)

        bottomSheetDialog.setContentView(view)

        btnLoginRegister.setOnClickListener {
            bottomSheetDialog.show()
        }

        view.regisSchool.setOnClickListener {
            Toast.makeText(this, "Register by School", Toast.LENGTH_LONG).show()
        }

        view.regisParent.setOnClickListener {
            Toast.makeText(this, "Register by Parents", Toast.LENGTH_LONG).show()
        }


    }
}
