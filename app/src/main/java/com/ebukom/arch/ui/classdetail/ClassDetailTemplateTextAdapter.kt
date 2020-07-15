package com.ebukom.arch.ui.classdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import kotlinx.android.synthetic.main.item_announcement_edit_template.view.*

class ClassDetailTemplateTextAdapter(private val data: List<ClassDetailTemplateTextDao>) :
    RecyclerView.Adapter<ClassDetailTemplateTextAdapter.ViewHolder>() {

    private val items: MutableList<ClassDetailTemplateTextDao>

    init {
        this.items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_announcement_edit_template, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.templateText.text = data[position].title
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder
    internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val templateText: TextView = itemView.tvAnnouncementEditTemplate
    }
}