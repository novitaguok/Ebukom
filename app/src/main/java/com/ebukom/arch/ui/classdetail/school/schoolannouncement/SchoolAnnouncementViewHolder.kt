package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_announcement.view.*

class SchoolAnnouncementViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_announcement, parent, false)) {

    private var mAnnouncementTitle: TextView? = null
    private var mAnnouncementContent: TextView? = null
    private var mAnnouncementComment: TextView? = null
    private var mAnnouncementTime: TextView? = null

    init {
        mAnnouncementTitle = itemView.findViewById(R.id.tvAnnouncementTitle)
        mAnnouncementContent = itemView.findViewById(R.id.tvAnnouncementContent)
        mAnnouncementComment = itemView.findViewById(R.id.tvAnnouncementComment)
        mAnnouncementTime = itemView.findViewById(R.id.tvAnnouncementTime)
    }

    fun bind(announcement: ClassDetailAnnouncementDao) {
        mAnnouncementTitle?.text = announcement.announcementTitle
        mAnnouncementContent?.text = announcement.announcementContent
        mAnnouncementComment?.text = announcement.comment
        mAnnouncementTime?.text = announcement.time
    }
}
