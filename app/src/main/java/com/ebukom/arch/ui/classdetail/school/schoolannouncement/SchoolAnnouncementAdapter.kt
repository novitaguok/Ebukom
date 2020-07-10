package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.chooseclass.ChooseClassViewHolderBlue
import com.ebukom.arch.ui.chooseclass.ChooseClassViewHolderGreen
import com.ebukom.base.BaseAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.fragment_school_announcement.view.*
import kotlinx.android.synthetic.main.item_announcement.view.*

class SchoolAnnouncementAdapter(var announcements: List<ClassDetailAnnouncementDao>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_announcement, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return announcements.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(announcements[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(announcement: ClassDetailAnnouncementDao) {
            itemView.tvAnnouncementTitle.text = announcement?.announcementTitle
            itemView.tvAnnouncementContent.text = announcement?.announcementContent
            itemView.tvAnnouncementComment.text = announcement?.comment
            itemView.tvAnnouncementTime.text = announcement?.time

            itemView.ivAnnouncementMoreButton.setOnClickListener {

            }


        }
    }
}
