package com.ebukom.arch.ui.classdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailSchoolAnnouncementMonthDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.personal.personalnotenew.PersonalNoteNewActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew.SchoolAnnouncementNewActivity
import kotlinx.android.synthetic.main.item_announcement_edit_template.view.*

class ClassDetailSchoolAnnouncementMonthAdapter(private val data: List<ClassDetailSchoolAnnouncementMonthDao>, var callback: OnItemClickedListener) :
    RecyclerView.Adapter<ClassDetailSchoolAnnouncementMonthAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_announcement_edit_template, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.month.text = data[position].month
        holder.itemView.setOnClickListener {
            callback.onItemClicked(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder
    internal constructor(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        val month: TextView = itemView.tvAnnouncementEditTemplate
        init {

        }
    }

    interface OnItemClickedListener{
        public fun onItemClicked(child : ClassDetailSchoolAnnouncementMonthDao)
    }
}