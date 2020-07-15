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
import com.ebukom.utils.load
import kotlinx.android.synthetic.main.item_announcement_attachment.view.*

class ClassDetailAttachmentAdapter(private val data: List<ClassDetailAttachmentDao>) :
    RecyclerView.Adapter<ClassDetailAttachmentAdapter.ViewHolder>() {
    private val items: MutableList<ClassDetailAttachmentDao>
    lateinit var context : Context

    init {
        this.items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_announcement_attachment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].name

        when(data[position].category){
            0 -> {
//                TODO : If category Link
                holder.image.load(context, R.drawable.ic_link_red)
            }
            1 -> {
//                TODO : If category Image
                holder.image.load(context, R.drawable.ic_photo_red)
            }
            2 -> {
//                TODO : If category Document
                holder.image.load(context, R.drawable.ic_file_red)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder
    internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.ivItemAnnouncementAttachment
        val name: TextView = itemView.tvItemAnnouncementAttachment
    }
}