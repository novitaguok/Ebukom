package com.ebukom.arch.ui.classdetail.member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMemberContactDao
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import kotlinx.android.synthetic.main.item_member.view.*
import kotlinx.android.synthetic.main.item_note.view.*

class MemberContactAdapter(
    var data: List<ClassDetailMemberContactDao>,
    var callback: OnMoreCallback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_member, parent, false)
        return ViewHolder(
            view,
            callback
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    class ViewHolder(itemView: View, val callback : OnMoreCallback) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: ClassDetailMemberContactDao) {
            itemView.ivItemMember.setImageResource(contact?.profilePic)
            itemView.tvItemMemberTeacher.text = contact?.teacherName
            itemView.tvItemMemberChild.text = contact?.childName
        }
    }
}
