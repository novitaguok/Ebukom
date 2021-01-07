package com.ebukom.arch.ui.classdetail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.chooseclass.ChooseClassActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementmainpage.SchoolAnnouncementActivity
import com.ebukom.arch.ui.joinclass.JoinClassActivity
import kotlinx.android.synthetic.main.item_announcement_title.view.*
import kotlinx.android.synthetic.main.item_class_detail_header.view.*

class MainClassDetailAdapter(private val classes: List<ChooseClassDao>) :
    RecyclerView.Adapter<MainClassDetailAdapter.ViewHolder>() {

    private var mSelectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_class_detail_header, parent, false), parent.context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(classes[position], position, mSelectedItem)
    }

    override fun getItemCount() = classes.size

    interface onItemClickedListener {
        fun onItemClicked()
    }

    inner class ViewHolder(
        itemView: View,
        val context: Context
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ChooseClassDao, position: Int, selectedPosition: Int) {
            itemView.rbItemClassDetailHeader.text = data.classNumber + " " + data.className

            itemView.rbItemClassDetailHeader.isChecked = data.isChecked
            itemView.rbItemClassDetailHeader.setOnClickListener {
                mSelectedItem = adapterPosition
                notifyDataSetChanged()

                val intent = Intent(
                    context,
                    MainClassDetailActivity::class.java
                )
                intent.putExtra("classId", data.classId)
                intent.putExtra("className", data.className)
                intent.putExtra("classNumber", data.classNumber)
                context.startActivity(intent)
                (context as MainClassDetailActivity).finish()
            }
        }
    }
}