package com.ebukom.arch.ui.chooseclass

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
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
import kotlinx.android.synthetic.main.item_class.*
import kotlinx.android.synthetic.main.item_class.view.*


class ChooseClassActivity : AppCompatActivity() {

    private val mList: ArrayList<ChooseClassDao> = arrayListOf()
    private val mAdapter = ChooseClassAdapter(mList)
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
//        mList.addAll(DataDummy.chooseClassDataMain)
//        mAdapter.addAll(mList)
        mList.add(ChooseClassDao("Kelas 1", "Krypton", "Ratna Hendrawati", R.drawable.bg_class_krypton, Color.parseColor("#005C39")))
        mList.add(ChooseClassDao("Kelas 1", "Xenon", "Eni Trikuswanti", R.drawable.bg_class_xenon, Color.parseColor("#004A61")))
        mList.add(ChooseClassDao("Kelas 1", "Argon", "Ratna Hendrawati", R.drawable.bg_class_argon, Color.parseColor("#693535")))
        mList.add(ChooseClassDao("Kelas 2", "Titanium", "Yulianti Puspita", R.drawable.bg_class_titanium, Color.parseColor("#229AE6")))
        mList.add(ChooseClassDao("Kelas 2", "Neon", "Putri Eka", R.drawable.bg_class_neon, Color.parseColor("#FFADAD")))
        mList.add(ChooseClassDao("Kelas 2", "Helium", "Ahmad Juliansyah", R.drawable.bg_class_helium, Color.parseColor("#645470")))
        mList.add(ChooseClassDao("Kelas 2", "Helium", "Ahmad Juliansyah", R.drawable.bg_class_helium, Color.parseColor("#645470")))
        mList.add(ChooseClassDao("Kelas 3", "Argentum", "Gigi Rahma", R.drawable.bg_class_argentum, Color.parseColor("#313D2E")))
        mList.add(ChooseClassDao("Kelas 3", "Aurum", "Julia Isma", R.drawable.bg_class_aurum, Color.parseColor("#828127")))
        mList.add(ChooseClassDao("Kelas 3", "Selenium", "Dewi Putri", R.drawable.bg_class_selenium, Color.parseColor("#4F483B")))

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
                dialog.dismiss()
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

            builder.setNegativeButton("BATALKAN") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { _, _ ->
                DataDummy.chooseClassDataMain.remove(item)
                mList.remove(item)
//                mAdapter.addAll(mList)
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

//    override fun onResume() {
//        super.onResume()
//
//        mList.clear()
//        mList.addAll(DataDummy.chooseClassDataMain)
////        mAdapter.addAll(mList)
//        checkEmptyList()
//    }
}
