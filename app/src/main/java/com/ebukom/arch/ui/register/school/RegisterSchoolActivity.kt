package com.ebukom.arch.ui.register.school

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.ebukom.R

class RegisterSchoolActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_school)

        val spinner = findViewById<Spinner>(R.id.spRegisterSchoolJabatan) as Spinner

        val jabatan = arrayOf("Jabatan", "Jabatan 1", "Jabatan 2")

        val adapter = ArrayAdapter(
            this@RegisterSchoolActivity,
            android.R.layout.simple_spinner_dropdown_item,
            jabatan
        )
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    this@RegisterSchoolActivity,
                    jabatan[position] + " selected",
                    Toast.LENGTH_LONG
                )
            }
        }
    }
}
