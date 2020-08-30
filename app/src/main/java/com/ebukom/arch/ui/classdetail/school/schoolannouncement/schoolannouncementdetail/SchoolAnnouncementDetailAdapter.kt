package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.ebukom.arch.ui.classdetail.personal.personalnotedetail.PersonalNoteDetailActivity
import kotlinx.android.synthetic.main.item_announcement_detail_comment.view.*

class SchoolAnnouncementDetailAdapter(
    val comments: ArrayList<ClassDetailAnnouncementCommentDao>,
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_announcement_detail_comment, parent, false)
        return ViewHolder(view, context)

    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(comments[position])
    }

    class ViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bind(comment: ClassDetailAnnouncementCommentDao) {
            itemView.tvAnnouncementDetailCommentName.text = comment?.name
            itemView.tvAnnouncementDetailCommentContent.text = comment?.comment
            itemView.ivAnnouncementDetailCommentProfilePicture.setImageResource(comment.profilePic)

            itemView.ivAnnouncementDetailCommentMoreButton.setOnClickListener {
                if (context is SchoolAnnouncementDetailActivity) context.popupMenuComment(adapterPosition)
                else if (context is PersonalNoteDetailActivity) context.popupMenuComment(adapterPosition)
            }
        }
    }
}