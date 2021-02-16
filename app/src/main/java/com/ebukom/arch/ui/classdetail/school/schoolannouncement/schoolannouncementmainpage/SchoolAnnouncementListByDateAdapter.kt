package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementmainpage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailActivity
import kotlinx.android.synthetic.main.item_announcement_title.view.*

class SchoolAnnouncementListByDateAdapter(var announcements: List<ClassDetailAnnouncementDao>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var intent: Intent

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_announcement_title, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return announcements.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(announcements[position])
    }

    inner class ViewHolder(
        itemView: View,
        val context: Context
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ClassDetailAnnouncementDao) {
            itemView.tvAnnouncementDetail.text = data.announcementTitle
            itemView.rlItemAnnouncementTitle.setOnClickListener {
                val intent = Intent(context, SchoolAnnouncementDetailActivity::class.java)
                data.isRead = true
                if (data.isRead) itemView.ivAnnouncementDetailBadge.visibility = View.GONE
                intent.putExtra("classId", data.classId)
                intent.putExtra("announcementId", data.announcementId)
                (context as SchoolAnnouncementActivity).startActivity(intent)
            }
        }
    }
}
