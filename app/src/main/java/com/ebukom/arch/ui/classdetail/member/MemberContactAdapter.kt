package com.ebukom.arch.ui.classdetail.member

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMemberContactDao
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import kotlinx.android.synthetic.main.item_member.view.*
import kotlinx.android.synthetic.main.item_note.view.*

class MemberContactAdapter(
    var data: List<ClassDetailMemberContactDao>,
    var callback: OnMoreCallback,
    var context: Context
) :
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

    inner class ViewHolder(itemView: View, val callback : OnMoreCallback) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: ClassDetailMemberContactDao) {
            itemView.ivItemMember.setImageResource(contact?.profilePic)
            itemView.tvItemMemberTeacher.text = contact?.teacherName
            itemView.tvItemMemberChild.text = contact?.childName

            // Intent to WhatsApp
            var url: String = "https://api.whatsapp.com/"
            var intentWa = Intent(Intent.ACTION_VIEW)
            intentWa.data = Uri.parse(url)
            itemView.ivItemMemberWhatsApp.setOnClickListener {
                context.startActivity(intentWa)
            }

            // Intent to Call
            var intentPhone = Intent(Intent.ACTION_DIAL)
            intentPhone.data = Uri.parse("tel:08123456789")
            itemView.ivItemMemberPhone.setOnClickListener {
                context.startActivity(intentPhone)
            }
        }
    }
}
