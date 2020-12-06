package com.ebukom.arch.ui.classdetail.school.schoolschedule

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailScheduleDao
import com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulefile.SchoolScheduleFileActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_schedule.view.*

class SchoolScheduleAdapter(private val items: List<ClassDetailScheduleDao>, var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind (item: ClassDetailScheduleDao) {
            val db = FirebaseFirestore.getInstance()

//            itemView.tvItemScheduleSubject.text = item?.title
//            itemView.tvItemScheduleSubjectType.text = item?.type
//            itemView.tvItemScheduleSubjectOpen.text = "Lihat " + item?.type
//            itemView.tvItemScheduleSubjectType.setTextColor(item?.colorTheme!!)
//            itemView.tvItemScheduleSubjectOpen.setTextColor(item?.colorTheme!!)
//            itemView.ivItemScheduleSubject.setImageResource(item?.background)
            itemView.clItemSchedule.setOnClickListener {
                (context as SchoolScheduleActivity).startActivity(Intent((context as SchoolScheduleActivity), SchoolScheduleFileActivity::class.java))
            }
        }
    }
}