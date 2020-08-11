package com.ebukom.arch.ui.chooseclass

import android.content.Context
import android.content.Intent
import android.view.View
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.joinclass.JoinClassActivity
import com.ebukom.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_class_blue.view.*

class ChooseClassViewHolderBlue(var view: View, var context: Context) :
    BaseViewHolder<ChooseClassDao>(view) {
    override fun onBind(pos: Int, count: Int, item: ChooseClassDao) {

        view.tvItemClassKelas2.text = item.classNumber
        view.tvItemClassName2.text = item.className
        view.tvItemClassTeacher2.text = item.teacher


        if (context is ChooseClassActivity) {
            view.ibItemClassTwo.setOnClickListener {
                (context as ChooseClassActivity).popupMenu(item)
            }
            view.clItemClassBlue.setOnClickListener {
                (context as ChooseClassActivity).startActivity(Intent((context as ChooseClassActivity), MainClassDetailActivity::class.java))
            }

        } else {
            view.ibItemClassTwo.visibility = View.INVISIBLE

            view.clItemClassBlue.setOnClickListener{
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