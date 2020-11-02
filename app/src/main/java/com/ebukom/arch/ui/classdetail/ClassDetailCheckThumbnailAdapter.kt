package com.ebukom.arch.ui.classdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailItemCheckThumbnailDao
import com.ebukom.arch.ui.classdetail.personal.personalnotenewnext.PersonalNoteNewNextActivity
import kotlinx.android.synthetic.main.item_check.view.*
import kotlinx.android.synthetic.main.item_check_thumbnail.view.*

class ClassDetailCheckThumbnailAdapter(
    public val list: ArrayList<ClassDetailItemCheckThumbnailDao>,
    val callback: PersonalNoteNewNextActivity
) :
    RecyclerView.Adapter<ClassDetailCheckThumbnailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_check_thumbnail, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list?.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.tvItemCheckThumbnailName.text = list?.get(position)?.name
        holder.view.tvItemCheckThumbnailDesc.text = list?.get(position)?.desc
        holder.view.ivItemCheckThumbnailProfilePicture.setImageResource(R.drawable.ic_notification)
        holder.view.cbItemCheckThumbnail.isChecked = list[position].isChecked

        if (position == list?.size - 1) {
            holder.view.vItemCheckThumbnail.visibility = View.INVISIBLE
        }
    }

    inner class ViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view) {
        init {
            view.cbItemCheckThumbnail.setOnCheckedChangeListener { _, isChecked ->
                list[adapterPosition].isChecked = isChecked
                callback.onCheckChange()
            }
        }
    }

    interface OnCheckListener {
        fun onCheckChange()
    }
}