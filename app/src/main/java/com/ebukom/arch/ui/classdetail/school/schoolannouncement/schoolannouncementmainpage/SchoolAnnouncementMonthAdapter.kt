package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementmainpage

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailSchoolAnnouncementMonthDao
import kotlinx.android.synthetic.main.item_announcement_month.view.*

class SchoolAnnouncementMonthAdapter(private val data: List<ClassDetailSchoolAnnouncementMonthDao>, var callback: OnItemClickedListener) :
    RecyclerView.Adapter<SchoolAnnouncementMonthAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_announcement_month, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.month.text = data[position].month
        holder.itemView.setOnClickListener {
            callback.onItemClicked(data[position])
        }

        if (data[position].isSelected) {
            holder.itemView.tvAnnouncementMonth.setTextColor(Color.parseColor("#FFFFFF"))
            holder.itemView.setBackgroundResource(R.drawable.btn_blue_month)
        } else {
            holder.itemView.tvAnnouncementMonth.setTextColor(Color.parseColor("#808080"))
            holder.itemView.setBackgroundResource(R.drawable.btn_gray_month)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder
    internal constructor(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        val month: TextView = itemView.tvAnnouncementMonth
        init {

        }
    }

    interface OnItemClickedListener{
        public fun onItemClicked(child : ClassDetailSchoolAnnouncementMonthDao)
    }
}