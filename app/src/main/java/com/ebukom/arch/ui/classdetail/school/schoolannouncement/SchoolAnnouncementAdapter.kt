package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailActivity
import kotlinx.android.synthetic.main.item_announcement.view.*

class SchoolAnnouncementAdapter(
    var announcements: List<ClassDetailAnnouncementDao>,
    var callback: OnMoreCallback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_announcement, parent, false)
        return ViewHolder(view, callback, parent.context)
    }

    override fun getItemCount(): Int {
        return announcements.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(announcements[position])
    }

    class ViewHolder(
        itemView: View,
        val callback: OnMoreCallback,
        val context: Context
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(announcement: ClassDetailAnnouncementDao) {
            itemView.tvAnnouncementTitle.text = announcement?.announcementTitle
            itemView.tvAnnouncementContent.text = announcement?.announcementContent
            itemView.tvAnnouncementComment.text = announcement?.comment
            itemView.tvAnnouncementTime.text = announcement?.time

            itemView.ivAnnouncementMoreButton.setOnClickListener {
                callback.onMoreClicked("")
            }

            itemView.clItemAnnouncement.setOnClickListener {
                context.startActivity(Intent(context, SchoolAnnouncementDetailActivity::class.java))
            }
        }
    }
}
