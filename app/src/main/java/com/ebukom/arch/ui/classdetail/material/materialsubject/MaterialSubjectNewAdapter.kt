package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.chooseclass.ChooseClassActivity
import kotlinx.android.synthetic.main.item_subject.view.*

class MaterialSubjectNewAdapter(val list: ArrayList<ClassDetailTemplateTextDao>, var context: Context) :
    RecyclerView.Adapter<MaterialSubjectNewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_subject, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list?.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.tvItemSubject.text = list?.get(position)?.title

        holder.view.ibItemSubject.setOnClickListener {
            (context as MaterialSubjectNewActivity).popUpMenu()
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}