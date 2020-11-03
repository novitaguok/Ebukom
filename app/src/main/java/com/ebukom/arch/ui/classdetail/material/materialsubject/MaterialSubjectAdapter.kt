package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao

class MaterialSubjectAdapter(private val classes: List<ChooseClassDao>) : RecyclerView.Adapter<MaterialSubjectViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialSubjectViewHolder {
        return MaterialSubjectViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_subject, parent, false), parent.context)
    }

    override fun onBindViewHolder(holder: MaterialSubjectViewHolder, position: Int) {
        holder.onBind(classes[position])
    }

    override fun getItemCount() = classes.size
}