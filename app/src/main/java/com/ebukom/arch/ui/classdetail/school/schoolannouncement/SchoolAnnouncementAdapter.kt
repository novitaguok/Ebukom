package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailActivity
import kotlinx.android.synthetic.main.fragment_school_announcement.*
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
            if(announcement.teacherName.isNullOrEmpty())
                itemView.tvAnnouncementComment.text = announcement?.comments.size.toString() + " KOMENTAR"
            else
                itemView.tvAnnouncementComment.text = announcement.teacherName

            itemView.tvAnnouncementTime.text = announcement?.time

            val sharePref: SharedPreferences = context.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
            if(sharePref.getInt("level", 0) == 1){
                itemView.ivAnnouncementMoreButton.visibility = View.GONE
            }

            itemView.ivAnnouncementMoreButton.setOnClickListener {
                callback.onMoreClicked("0", adapterPosition)
            }

            itemView.clItemAnnouncement.setOnClickListener {
                val intent = Intent(context, SchoolAnnouncementDetailActivity::class.java)
                if (!(MainClassDetailActivity.isAnnouncement)) {
                    intent.putExtra("layout", "1")
                }
                intent.putExtra("pos", adapterPosition)
                intent.putExtra("data", announcement)
                context.startActivity(intent)
            }
        }
    }
}
