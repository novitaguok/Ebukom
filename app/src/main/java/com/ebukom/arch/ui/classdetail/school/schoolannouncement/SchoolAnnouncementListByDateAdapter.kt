package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.material.materialeducation.MaterialEducationFragment
import com.ebukom.arch.ui.classdetail.material.materialeducation.materialeducationdetail.MaterialEducationDetailActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailActivity
import kotlinx.android.synthetic.main.activity_personal_note_new.*
import kotlinx.android.synthetic.main.fragment_school_announcement.*
import kotlinx.android.synthetic.main.item_announcement.view.*
import kotlinx.android.synthetic.main.item_announcement.view.tvAnnouncementTitle
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
                (context as SchoolAnnouncementActivity).startActivity(Intent(context, SchoolAnnouncementDetailActivity::class.java))
            }
//            itemView.tvAnnouncementContent.text = data.announcementContent
//            if (data.teacherName.isNullOrEmpty())
//                itemView.tvAnnouncementComment.text =
//                    data?.comments.size.toString() + " KOMENTAR"
//            else
//                itemView.tvAnnouncementComment.text = data.teacherName
//
//            itemView.tvAnnouncementTime.text = data?.time
        }
    }
}
