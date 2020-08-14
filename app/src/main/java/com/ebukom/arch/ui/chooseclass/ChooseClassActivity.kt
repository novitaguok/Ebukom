package com.ebukom.arch.ui.chooseclass

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.admin.MainAdminActivity
import com.ebukom.arch.ui.joinclass.JoinClassActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_choose_class.*
import kotlinx.android.synthetic.main.bottom_sheet_choose_class.view.*


class ChooseClassActivity : AppCompatActivity() {

    private val mList: ArrayList<ChooseClassDao> = arrayListOf()
    private val mAdapter = ChooseClassAdapter()
    lateinit var sharePref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_class)

        rvChooseClassClasses.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@ChooseClassActivity)
        }

        // "Gabung Kelas" Button
        btnChooseClassJoin.setOnClickListener {
            startActivity(Intent(this, JoinClassActivity::class.java))
        }

        // Class list
        mList.addAll(DataDummy.chooseClassDataMain)
        mAdapter.addAll(mList)

        checkEmptyList()

        // Logout Button
        btnChooseClassLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this@ChooseClassActivity)

            sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

            builder.setMessage("Apakah Anda yakin ingin melakukan logout?")
            builder.setPositiveButton("LOGOUT") { dialog, which ->
                sharePref.edit().remove("level").apply()
                sharePref.edit().apply {
                    putBoolean("isLogin", false)
                }.apply()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext,"Next?", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorRed
                )
            )
        }

    }

    private fun checkEmptyList() {
        if (mList.isEmpty()) {
            tvChooseClassMainEmpty.visibility = View.VISIBLE
            ivChooseClassMainEmpty.visibility = View.VISIBLE
            rvChooseClassClasses.visibility = View.INVISIBLE
        } else {
            tvChooseClassMainEmpty.visibility = View.INVISIBLE
            ivChooseClassMainEmpty.visibility = View.INVISIBLE
            rvChooseClassClasses.visibility = View.VISIBLE
        }
    }

    fun popupMenu(item: ChooseClassDao) {
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
                DataDummy.chooseClassDataMain.remove(item)
                mList.remove(item)
                mAdapter.addAll(mList)
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorRed
                )
            )
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )
        }
        view.tvCancelClass.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()

        mList.clear()
        mList.addAll(DataDummy.chooseClassDataMain)
        mAdapter.addAll(mList)
        checkEmptyList()
    }
}
