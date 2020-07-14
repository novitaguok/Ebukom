package com.ebukom.arch.ui.classdetail.school.schoolschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailScheduleDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import kotlinx.android.synthetic.main.item_schedule.view.*

class SchoolScheduleAdapter(
    var items: List<ClassDetailScheduleDao>,
    var callback: OnMoreCallback
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_schedule, parent, false)
        return ViewHolder(view, callback)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    class ViewHolder(itemView: View, val callback: OnMoreCallback) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(item: ClassDetailScheduleDao) {
            itemView.tvScheduleTitle.text = item?.scheduleTitle
            itemView.tvSchedulePath.text = item?.path

//            itemView.ivAnnouncementMoreButton.setOnClickListener {
//                callback.onMoreClicked("")
//            }
        }
    }
}