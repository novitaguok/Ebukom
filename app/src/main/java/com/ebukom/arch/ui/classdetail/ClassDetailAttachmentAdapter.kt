package com.ebukom.arch.ui.classdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd.MaterialSubjectAddActivity
import com.ebukom.arch.ui.classdetail.personal.personalnotedetail.PersonalNoteDetailActivity
import com.ebukom.arch.ui.classdetail.personal.personalnoteedit.PersonalNoteEditActivity
import com.ebukom.arch.ui.classdetail.personal.personalnotenew.PersonalNoteNewActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit.SchoolAnnouncementEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew.SchoolAnnouncementNewActivity
import com.ebukom.utils.load
import kotlinx.android.synthetic.main.item_announcement_attachment.view.*

class ClassDetailAttachmentAdapter(private val data: List<ClassDetailAttachmentDao>) :
    RecyclerView.Adapter<ClassDetailAttachmentAdapter.ViewHolder>() {
    private val items: MutableList<ClassDetailAttachmentDao>
    lateinit var context: Context

    init {
        this.items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_announcement_attachment, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        itemView: View,
        var context: Context
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(item: ClassDetailAttachmentDao) {
            itemView.tvItemAnnouncementAttachment.text = item.path

            when (item.category) {
                0 -> {
                    itemView.ivItemAnnouncementAttachment.setImageResource(R.drawable.ic_link_red)
                }
                1 -> {
                    itemView.ivItemAnnouncementAttachment.setImageResource(R.drawable.ic_photo_red)
                }
                2 -> {
                    itemView.ivItemAnnouncementAttachment.setImageResource(R.drawable.ic_file_red)
                }
                3 -> {
                    itemView.ivItemAnnouncementAttachment.setImageResource(R.drawable.ic_video_red)
                }
            }

            if (context is SchoolAnnouncementDetailActivity || context is PersonalNoteDetailActivity) itemView.ivItemAnnouncementAttachmentDelete.visibility =
                View.GONE

            itemView.ivItemAnnouncementAttachmentDelete.setOnClickListener {
                if (context is SchoolAnnouncementNewActivity) {
                    (context as SchoolAnnouncementNewActivity).deleteAttachment(item)
                } else if (context is SchoolAnnouncementEditActivity) {
                    (context as SchoolAnnouncementEditActivity).deleteAttachment(item)
                } else if (context is PersonalNoteNewActivity) {
                    (context as PersonalNoteNewActivity).deleteAttachment(item)
                } else if (context is PersonalNoteEditActivity) {
                    (context as PersonalNoteEditActivity).deleteAttachment(item)
                } else {
                    (context as MaterialSubjectAddActivity).deleteAttachment(item)
                }
            }
        }
    }
}