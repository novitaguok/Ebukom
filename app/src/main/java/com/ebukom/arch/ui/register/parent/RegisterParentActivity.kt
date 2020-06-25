package com.ebukom.arch.ui.register.parent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.ebukom.R

class RegisterParentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_parent)

        val spinner = findViewById<Spinner>(R.id.spRegisterParentEskul) as Spinner

        val eskul = arrayOf("Eskul 1", "Eskul 2", "Eskul 3")

        val adapter = ArrayAdapter(
            this@RegisterParentActivity,
            android.R.layout.simple_spinner_dropdown_item,
            eskul
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
                    this@RegisterParentActivity,
                    eskul[position] + " selected",
                    Toast.LENGTH_LONG
                )
            }
        }
    }
}
