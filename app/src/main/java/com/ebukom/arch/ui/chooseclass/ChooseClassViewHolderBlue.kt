package com.ebukom.arch.ui.chooseclass

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.widget.ImageButton
import android.widget.Toast
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.base.BaseViewHolder
import com.ebukom.utils.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.bottom_sheet_choose_class.view.*
import kotlinx.android.synthetic.main.item_class_blue.*
import kotlinx.android.synthetic.main.item_class_blue.view.*
import kotlinx.android.synthetic.main.item_class_green.*

class ChooseClassViewHolderBlue(var view: View, var context: Context) :
    BaseViewHolder<ChooseClassDao>(view) {
    override fun onBind(pos: Int, count: Int, item: ChooseClassDao) {

        view.tvItemClassKelas2.text = item.classNumber
        view.tvItemClassName2.text = item.className
        view.tvItemClassTeacher2.text = item.teacher


        if (context is ChooseClassActivity) {
            view.ibItemClassTwo.setOnClickListener {
                (context as ChooseClassActivity).popupMenu()
            }
        } else {
            view.ibItemClassTwo.visibility = View.INVISIBLE
        }
    }
}