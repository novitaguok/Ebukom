package com.ebukom.arch.ui.classdetail.school.schoolannouncement


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailSchoolAnnouncementMonthDao
import kotlinx.android.synthetic.main.item_announcement_by_date.view.*
import kotlinx.android.synthetic.main.item_announcement_time_header.view.*

class SchoolAnnouncementListAdapter(var announcements: List<ClassDetailSchoolAnnouncementMonthDao>) :
    RecyclerView.Adapter<SchoolAnnouncementListAdapter.ViewHolder>() {


    lateinit var intent: Intent
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> {
                ViewHolderAnnounce(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_announcement_by_date, parent, false)
                )
            }
            else -> {
                ViewHolderSeparator(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_announcement_time_header, parent, false)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return announcements.size
    }

    override fun getItemViewType(position: Int): Int {
        return announcements[position].viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        (holder as ViewHolder).bind(announcements[position])
        if(holder is ViewHolderAnnounce){
            val announcementPosition = announcements[position]
            val childLayoutManager =
                LinearLayoutManager(holder.recyclerView.context, LinearLayoutManager.VERTICAL, false)

            childLayoutManager.initialPrefetchItemCount = 4

            holder.recyclerView.apply {
                layoutManager = childLayoutManager
                adapter = SchoolAnnouncementListByDateAdapter(announcementPosition.announcement)
                setRecycledViewPool(viewPool)
            }
        }else if(holder is ViewHolderSeparator){
            holder.title.text = announcements[position].month
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    inner class ViewHolderAnnounce(itemView: View) : ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.rvItemAnnouncementByDate
    }

    inner class ViewHolderSeparator(itemView: View) : ViewHolder(itemView) {
        val title = itemView.tvAnnouncementDetailTimeHeader
    }
}
