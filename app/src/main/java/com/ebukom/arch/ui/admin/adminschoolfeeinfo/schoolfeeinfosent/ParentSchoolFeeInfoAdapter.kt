package com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.ui.admin.MainAdminActivity
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfonext.AdminShareSchoolFeeInfoNextActivity
import com.ebukom.arch.ui.classdetail.personal.personalparent.personalparentschoolfeeinfo.PersonalParentSchoolFeeInfoActivity
import kotlinx.android.synthetic.main.item_admin_info_sent.view.*

class ParentSchoolFeeInfoAdapter(
    var data: List<AdminSchoolFeeInfoSentDao>,
    val callback: onCheckListener?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_admin_info_sent, parent, false)
        return ViewHolder(
            view, parent.context
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    inner class ViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(info: AdminSchoolFeeInfoSentDao) {
            itemView.tvItemAdminInfoSentTitle.text = info?.title
            itemView.tvItemAdminInfoSentDetail.text = info?.detail
            itemView.tvItemAdminInfoSentDate.text = info?.date
            itemView.cbItemAdminInfoSent.isChecked = info?.isChecked

            if (context is AdminShareSchoolFeeInfoNextActivity) {
                itemView.cbItemAdminInfoSent.visibility = View.VISIBLE
            }

            itemView.cbItemAdminInfoSent.setOnCheckedChangeListener { _, isChecked ->
                data[adapterPosition].isChecked = isChecked
                callback?.onCheckChange()
            }

            if (context is MainAdminActivity) {
                itemView.clItemAdminInfoSent.setOnClickListener {
                    val intent = Intent(context, PersonalParentSchoolFeeInfoActivity::class.java)
                    intent.putExtra("role", "admin")
                    intent.putExtra("pos", adapterPosition)
                    context.startActivity(intent)
                }
            }
        }
    }

    interface onCheckListener {
        fun onCheckChange()
    }
}
