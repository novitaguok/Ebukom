package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementmainpage


import android.content.Context
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
import java.util.*

class SchoolAnnouncementPageAdapter(var announcements: List<ClassDetailSchoolAnnouncementMonthDao>) :
    RecyclerView.Adapter<SchoolAnnouncementPageAdapter.ViewHolder>() {

    lateinit var context: Context
    lateinit var intent: Intent
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
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
        if (holder is ViewHolderAnnounce) {
            val announcementPosition = announcements[position]
            holder.itemView.tvItemAnnouncementByDateDay.text = announcementPosition.dayName
            holder.itemView.tvItemAnnouncementByDateDate.text = announcementPosition.date.toString()

            val currentDate = Date().date
            val currentDay = Date().day
            val currentMonth = Date().month

            if (announcementPosition.day == currentDay && announcementPosition.date == currentDate && announcementPosition.monthId == currentMonth)
                holder.itemView.llItemAnnouncementByDateTime.setBackgroundResource(R.drawable.bg_square_red_4dp)
            else
                holder.itemView.llItemAnnouncementByDateTime.setBackgroundResource(R.drawable.bg_square_blue_4dp)


            val childLayoutManager =
                LinearLayoutManager(
                    holder.recyclerView.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )

            childLayoutManager.initialPrefetchItemCount = 4

            holder.recyclerView.apply {
                layoutManager = childLayoutManager
                adapter = SchoolAnnouncementListByDateAdapter(announcementPosition.announcement)
                setRecycledViewPool(viewPool)
            }
        } else if (holder is ViewHolderSeparator) {
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
