package com.ebukom.arch.ui.register.parent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ebukom.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_register_parent.*

class RegisterParentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_parent)

        initToolbar()

        // Pop Up
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_register_parent, null)
        bottomSheetDialog.setContentView(view)

        btnRegisterParentEskul.setOnClickListener {
            bottomSheetDialog.show()
        }

//        view.btnRegisterParentBottomSheetDone.setOnClickListener {
//
//            val pramuka = view.cbRegisterParentBottomSheetPramuka.isChecked
//            val futsal = view.cbRegisterParentBottomSheetFutsal.isChecked
//            val basket = view.cbRegisterParentBottomSheetBasket.isChecked
//            val pmr = view.cbRegisterParentBottomSheetPMR.isChecked
//
//            if(pramuka) {
//                Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show()
//            } else if(futsal) {
//                Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show()
//            } else if(basket) {
//                Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show()
//            }
//        }
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
