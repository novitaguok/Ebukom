package com.ebukom.arch.ui.classdetail.personal.personalnotenew

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.personal.personalacceptednote.PersonalAcceptedNoteFragment
import com.ebukom.arch.ui.classdetail.personal.personalnotedetail.PersonalNoteDetailActivity
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.item_note.view.*

class PersonalNoteAdapter(
    var data: List<ClassDetailPersonalNoteDao>,
    var tabItem: Int,
    var callback: OnMoreCallback,
    var fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit  var id: String

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

    inner class ViewHolder(itemView: View, val callback: OnMoreCallback, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(note: ClassDetailPersonalNoteDao) {
            itemView.ivItemNoteProfilePicture.setImageResource(note.profilePicture)
            if (tabItem == 1) {
                itemView.tvItemNoteTitle.text = "Untuk: " + note?.noteTitle
            } else {
                itemView.tvItemNoteTitle.text = note?.noteTitle
            }
            itemView.tvItemNoteContent.text = note?.noteContent
            itemView.tvItemNoteComment.text = note?.comments.size.toString() + " KOMENTAR"
            itemView.tvItemNoteTime.text = note?.time

            if (fragment is PersonalAcceptedNoteFragment) {
                id = "5"
            } else {
                id = "3"
            }
            itemView.ivItemNoteMoreButton.setOnClickListener {
//                callback.onMoreClicked(id, adapterPosition)
            }

            itemView.clItemNote.setOnClickListener {
                val intent = Intent(context, PersonalNoteDetailActivity::class.java)
                intent.putExtra("pos", adapterPosition)
                intent.putExtra("data", note)
                (context as MainClassDetailActivity).startActivity(intent)
            }
        }
    }
}
