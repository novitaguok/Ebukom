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
import com.ebukom.arch.dao.ClassDetailSchoolInfoDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.material.materialeducation.MaterialEducationFragment
import com.ebukom.arch.ui.classdetail.material.materialeducation.materialeducationdetail.MaterialEducationDetailActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailActivity
import kotlinx.android.synthetic.main.activity_personal_note_new.*
import kotlinx.android.synthetic.main.fragment_school_announcement.*
import kotlinx.android.synthetic.main.item_announcement.view.*
import kotlinx.android.synthetic.main.item_school_info.view.*

class SchoolAnnouncementAdapter(
    var announcements: List<ClassDetailAnnouncementDao>,
    var callback: OnMoreCallback,
    var fragment: Fragment? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    lateinit var intent: Intent

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

    inner class ViewHolder(
        itemView: View,
        val callback: OnMoreCallback,
        val context: Context
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ClassDetailAnnouncementDao) {
            itemView.tvAnnouncementTitle.text = data?.announcementTitle
            itemView.tvAnnouncementContent.text = data?.announcementContent
            if (data.teacherName.isNullOrEmpty())
                itemView.tvAnnouncementComment.text =
                    data?.comments.size.toString() + " KOMENTAR"
            else
                itemView.tvAnnouncementComment.text = data.teacherName

            itemView.tvAnnouncementTime.text = data?.time

            val sharePref: SharedPreferences =
                context.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
            if (sharePref.getInt("level", 0) == 1) {
                itemView.ivAnnouncementMoreButton.visibility = View.GONE
            }

            itemView.ivAnnouncementMoreButton.setOnClickListener {
                if (fragment != null) {
                    if (fragment is SchoolAnnouncementFragment) {
                        callback.onMoreClicked("0", adapterPosition)
                    } else if (fragment is MaterialEducationFragment) {
                        callback.onMoreClicked("4", adapterPosition)
                    }
                }
            }

            itemView.clItemAnnouncement.setOnClickListener {
                if (fragment is MaterialEducationFragment) {
                    intent = Intent(context, MaterialEducationDetailActivity::class.java)
                } else {
                    intent = Intent(context, SchoolAnnouncementDetailActivity::class.java)
                }

                if (!(MainClassDetailActivity.isAnnouncement)) {
                    intent.putExtra("layout", "1")
                }

                intent.putExtra("pos", adapterPosition)
                intent.putExtra("data", data)
                context.startActivity(intent)
            }
        }
    }
}
