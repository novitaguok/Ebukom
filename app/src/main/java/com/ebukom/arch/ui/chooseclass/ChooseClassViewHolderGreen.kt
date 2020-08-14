package com.ebukom.arch.ui.chooseclass

import android.content.Context
import android.content.Intent
import android.view.View
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.joinclass.JoinClassActivity
import com.ebukom.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_class_green.view.*

class ChooseClassViewHolderGreen(var view: View, var context: Context) : BaseViewHolder<ChooseClassDao>(view) {
    override fun onBind(pos: Int, count: Int, item: ChooseClassDao) {

        view.tvItemClassKelas1.text = item.classNumber
        view.tvItemClassName1.text = item.className
        view.tvItemClassTeacher1.text = item.teacher

        val intent = Intent((context as ChooseClassActivity), MainClassDetailActivity::class.java)
        intent.putExtra("teacher", item.teacher)

        if (context is ChooseClassActivity) {
            view.ibItemClassOne.setOnClickListener {
                (context as ChooseClassActivity).popupMenu(item)
            }
            view.clItemClassGreen.setOnClickListener {
                (context as ChooseClassActivity).startActivity(intent)
            }
        } else {
            view.ibItemClassOne.visibility = View.INVISIBLE

            view.clItemClassGreen.setOnClickListener{
//                view.loading.visibility = View.VISIBLE
//                Handler().postDelayed({
//                    view.loading.visibility = View.GONE
//                    (context as JoinClassActivity).addClass(item)
//                }, 1000)
                (context as JoinClassActivity).addClass(item)
            }
        }
    }
}