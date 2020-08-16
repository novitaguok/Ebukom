package com.ebukom.arch.ui.classdetail.personal.personalnotenew

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.personal.personalnotedetail.PersonalNoteDetailActivity
import kotlinx.android.synthetic.main.item_note.view.*

class PersonalNoteAdapter(
    var data: List<ClassDetailPersonalNoteDao>,
    var callback: OnMoreCallback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_note, parent, false)
        return ViewHolder(
            view,
            callback, parent.context
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    class ViewHolder(itemView: View, val callback : OnMoreCallback, val context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: ClassDetailPersonalNoteDao) {
            itemView.ivItemNoteProfilePicture.setImageResource(note.profilePicture)
            itemView.tvItemNoteTitle.text = note?.noteTitle
            itemView.tvItemNoteContent.text = note?.noteContent
            itemView.tvItemNoteComment.text = note?.comment
            itemView.tvItemNoteTime.text = note?.time

            itemView.ivItemNoteMoreButton.setOnClickListener {
                callback.onMoreClicked("3", adapterPosition)
            }

            itemView.clItemNote.setOnClickListener {
                (context as MainClassDetailActivity).startActivity(Intent(context, PersonalNoteDetailActivity::class.java))
            }
        }
    }
}
