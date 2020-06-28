package com.ebukom.arch.ui.chooseclass

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.R.layout.activity_join_class
import com.ebukom.arch.dao.ChooseClassDao
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_choose_class.*
import kotlinx.android.synthetic.main.choose_class_bottom_sheet.view.*
import kotlinx.android.synthetic.main.item_class_blue.*


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
            clChooseClass.visibility = View.VISIBLE
            rvChooseClassClasses.visibility = View.INVISIBLE
        } else {
            clChooseClass.visibility = View.INVISIBLE
            rvChooseClassClasses.visibility = View.VISIBLE
        }

    }

    fun popupMenu() {
        // Card menu
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.choose_class_bottom_sheet, null)
        bottomSheetDialog.setContentView(view)


        view.deleteClass.setOnClickListener {
            Toast.makeText(this, "Class deleted", Toast.LENGTH_LONG).show()
        }
        view.cancelClass.setOnClickListener {
            Toast.makeText(this, "Class cancelled", Toast.LENGTH_LONG).show()
        }

        bottomSheetDialog.show()
    }
}
