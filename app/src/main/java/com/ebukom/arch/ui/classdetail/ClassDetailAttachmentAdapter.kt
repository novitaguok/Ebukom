package com.ebukom.arch.ui.classdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd.MaterialSubjectAddActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfilepreview.MaterialPreviewActivity
import com.ebukom.arch.ui.classdetail.personal.personalnotedetail.PersonalNoteDetailActivity
import com.ebukom.arch.ui.classdetail.personal.personalnoteedit.PersonalNoteEditActivity
import com.ebukom.arch.ui.classdetail.personal.personalnotenew.PersonalNoteNewActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit.SchoolAnnouncementEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew.SchoolAnnouncementNewActivity
import kotlinx.android.synthetic.main.item_attachment.view.*
import java.io.File

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
            .inflate(R.layout.item_attachment, parent, false)
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
            itemView.tvItemAttachment.text = item.fileName
            itemView.clItemAttachment.setOnClickListener {
                if (context is SchoolAnnouncementDetailActivity) {
                    if (item.category == 0) {
                        var url = item.path
                        if (!url!!.startsWith("http://")) {
                            url = "http://" + url
                        }
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        context.startActivity(intent)
                    } else if (item.category == 1) {
                        val intent = Intent(
                            context as SchoolAnnouncementDetailActivity,
                            MaterialPreviewActivity::class.java
                        )
                        intent.putExtra("filePath", item.path)
                        intent.putExtra("fileName", item.fileName)
                        intent.putExtra("activity", "announcement")
                        intent.putExtra("category", item.category)
                        context.startActivity(intent)
                    } else {
                        val webpage = Uri.parse(item.path)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        context.startActivity(intent)
                    }
                } else {
                    if (item.category == 0) {
                        var url = item.path
                        if (!url!!.startsWith("http://")) {
                            url = "http://" + url
                        }
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        context.startActivity(intent)
                    } else if (item.category == 1) {
                        val intent = Intent(
                            context as MaterialSubjectAddActivity,
                            MaterialPreviewActivity::class.java
                        )
                        intent.putExtra("filePath", item.path)
                        intent.putExtra("fileName", item.fileName)
                        intent.putExtra("category", item.category)
                        context.startActivity(intent)
                    } else {
                        val webpage = Uri.parse(item.path)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        context.startActivity(intent)
                    }
                }
            }

            when (item.category) {
                0 -> {
                    itemView.ivItemAttachment.setImageResource(R.drawable.ic_link_red)
                }
                1 -> {
                    itemView.ivItemAttachment.setImageResource(R.drawable.ic_photo_red)
                }
                2 -> {
                    itemView.ivItemAttachment.setImageResource(R.drawable.ic_file_red)
                }
                3 -> {
                    itemView.ivItemAttachment.setImageResource(R.drawable.ic_video_red)
                }
                else -> {
                    itemView.ivItemAttachment.setImageResource(R.drawable.ic_photo_red)
                }
            }

            if (context is SchoolAnnouncementDetailActivity || context is PersonalNoteDetailActivity) itemView.ivItemAttachmentDelete.visibility =
                View.GONE

            itemView.ivItemAttachmentDelete.setOnClickListener {
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