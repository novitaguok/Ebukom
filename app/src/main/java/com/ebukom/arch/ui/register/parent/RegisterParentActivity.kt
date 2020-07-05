package com.ebukom.arch.ui.register.parent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.ebukom.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register_parent.*
import kotlinx.android.synthetic.main.bottom_sheet_choose_class.view.*
import kotlinx.android.synthetic.main.bottom_sheet_login.view.*
import kotlinx.android.synthetic.main.bottom_sheet_register_parent.*
import kotlinx.android.synthetic.main.bottom_sheet_register_parent.view.*

class RegisterParentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_parent)

        // Pop Up
//        val bottomSheetDialog = BottomSheetDialog(this)
//        val view = layoutInflater.inflate(R.layout.bottom_sheet_register_parent, null)
//        bottomSheetDialog.setContentView(view)
//
//        btnRegisterParentEskul.setOnClickListener {
//            bottomSheetDialog.show()
//        }
//
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
}
