package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementEditDao
import kotlinx.android.synthetic.main.item_announcement_edit_template.view.*

class SchoolAnnouncementEditAdapter(private val data: List<ClassDetailAnnouncementEditDao>) :
    RecyclerView.Adapter<SchoolAnnouncementEditAdapter.ViewHolder>() {

    private val items: MutableList<ClassDetailAnnouncementEditDao>

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