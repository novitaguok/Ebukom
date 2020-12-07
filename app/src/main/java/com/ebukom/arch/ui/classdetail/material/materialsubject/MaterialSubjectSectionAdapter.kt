package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMaterialSubjectSectionDao
import kotlinx.android.synthetic.main.item_subject.view.*
import kotlinx.android.synthetic.main.item_subject_material.view.*

class MaterialSubjectSectionAdapter(
    val sectionList: ArrayList<ClassDetailMaterialSubjectSectionDao>,
    var context: Context
) :
    RecyclerView.Adapter<MaterialSubjectSectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subject_material, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.tvItemSubjectMaterial.text = sectionList.get(position).sectionName
//        holder.view.ibItemSubjectMaterial.setOnClickListener {
//            (context as MaterialSubjectNewActivity).popUpMenu(list?.get(position), position)
//        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}