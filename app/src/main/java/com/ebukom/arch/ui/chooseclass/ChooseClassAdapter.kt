package com.ebukom.arch.ui.chooseclass

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.joinclass.JoinClassActivity

class ChooseClassAdapter(private val classes: List<ChooseClassDao>) : RecyclerView.Adapter<ChooseClassViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseClassViewHolder {
        return ChooseClassViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false), parent.context)
    }

    override fun onBindViewHolder(holder: ChooseClassViewHolder, position: Int) {
        holder.onBind(classes[position])
    }

    override fun getItemCount() = classes.size

    interface onItemClickedListener {
        fun onItemClicked()
    }

}