package com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.dao.ClassDetailItemCheckDao
import kotlinx.android.synthetic.main.item_admin_info_sent.view.*
import kotlinx.android.synthetic.main.item_check.view.*

class AdminSchoolFeeInfoSentAdapter(val list: ArrayList<AdminSchoolFeeInfoSentDao>) :
    RecyclerView.Adapter<AdminSchoolFeeInfoSentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_admin_info_sent, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list?.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.tvItemAdminInfoSentTitle.text = list?.get(position)?.title
        holder.view.tvItemAdminInfoSentDetail.text = list?.get(position)?.detail
        holder.view.tvItemAdminInfoSentDate.text = list?.get(position)?.date
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}