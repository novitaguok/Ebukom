package com.ebukom.arch.ui.chooseclass

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_choose_class.*
import kotlinx.android.synthetic.main.bottom_sheet_choose_class.view.*


class ChooseClassActivity : AppCompatActivity() {

    private val mList: ArrayList<ChooseClassDao> = arrayListOf()
    private val mAdapter = ChooseClassAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_class)

        rvChooseClassClasses.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@ChooseClassActivity)
        }

        mList.add(ChooseClassDao("", "", "", 0))
        mList.add(ChooseClassDao("", "", "", 1))
        mList.add(ChooseClassDao("", "", "", 0))
        mList.add(ChooseClassDao("", "", "", 1))

        mAdapter.addAll(mList)

        // Class list
        if (mList.isEmpty()) {
            tvChooseClassMainEmpty.visibility = View.VISIBLE
            ivChooseClassMainEmpty.visibility = View.VISIBLE
            rvChooseClassClasses.visibility = View.INVISIBLE
        } else {
            tvChooseClassMainEmpty.visibility = View.INVISIBLE
            ivChooseClassMainEmpty.visibility = View.INVISIBLE
            rvChooseClassClasses.visibility = View.VISIBLE
        }

        // Logout Button
        btnChooseClassLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this@ChooseClassActivity)

            builder.setMessage("Apakah Anda yakin ingin melakukan logout?")
            builder.setPositiveButton("LOGOUT") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext,"Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    fun popupMenu() {
        // Card menu
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_choose_class, null)
        bottomSheetDialog.setContentView(view)


        view.tvDeleteClass.setOnClickListener {
            val builder = AlertDialog.Builder(this@ChooseClassActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus kelas ini?")

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext,"Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        view.tvCancelClass.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Class cancelled", Toast.LENGTH_LONG).show()
        }

        bottomSheetDialog.show()
    }
}
