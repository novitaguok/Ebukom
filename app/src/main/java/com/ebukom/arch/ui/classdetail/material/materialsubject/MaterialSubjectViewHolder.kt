package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.joinclass.JoinClassActivity
import kotlinx.android.synthetic.main.item_class.view.*
import kotlinx.android.synthetic.main.item_subject.view.*

class MaterialSubjectViewHolder(var view: View, var context: Context) :
    RecyclerView.ViewHolder(view) {
    fun onBind(item: ChooseClassDao) {

        view.tvItemSubjectName.text = item?.className
        view.ivItemSubject.setImageResource(item?.background)

        view.clItemSubject.setOnClickListener {
            (context as MainClassDetailActivity).startActivity(Intent((context as MainClassDetailActivity), MaterialSubjectNewActivity::class.java))
        }
    }
}