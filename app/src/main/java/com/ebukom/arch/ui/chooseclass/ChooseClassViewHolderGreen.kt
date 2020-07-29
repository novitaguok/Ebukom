package com.ebukom.arch.ui.chooseclass

import android.content.Context
import android.content.Intent
import android.view.View
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_class_green.view.*

class ChooseClassViewHolderGreen(var view: View, var context: Context) : BaseViewHolder<ChooseClassDao>(view) {
    override fun onBind(pos: Int, count: Int, item: ChooseClassDao) {

        view.tvItemClassKelas1.text = item.classNumber
        view.tvItemClassName1.text = item.className
        view.tvItemClassTeacher1.text = item.teacher

        if (context is ChooseClassActivity) {
            view.ibItemClassOne.setOnClickListener {
                (context as ChooseClassActivity).popupMenu()
            }
            view.clItemClassGreen.setOnClickListener {
                (context as ChooseClassActivity).startActivity(Intent((context as ChooseClassActivity), MainClassDetailActivity::class.java))
            }
        } else {
            view.ibItemClassOne.visibility = View.INVISIBLE
        }
    }
}