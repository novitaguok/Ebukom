package com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.dao.ClassDetailMemberContactDao
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import kotlinx.android.synthetic.main.item_admin_info_sent.view.*
import kotlinx.android.synthetic.main.item_member.view.*
import kotlinx.android.synthetic.main.item_note.view.*

class AdminSchoolFeeInfoSentAdapter(
    var data: List<AdminSchoolFeeInfoSentDao>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_admin_info_sent, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(info: AdminSchoolFeeInfoSentDao) {
            itemView.tvItemAdminInfoSentTitle.text = info?.title
            itemView.tvItemAdminInfoSentDetail.text = info?.date
            itemView.tvItemAdminInfoSentDate.text = info?.date
        }
    }
}
