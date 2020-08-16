package com.ebukom.arch.ui.classdetail.school.schoolschedule

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailScheduleDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulefile.SchoolScheduleFileActivity
import kotlinx.android.synthetic.main.item_schedule_purple.view.*
import kotlinx.android.synthetic.main.item_schedule_purple.view.tvItemScheduleSubjectOpen
import kotlinx.android.synthetic.main.item_schedule_red.view.*
import kotlinx.android.synthetic.main.item_schedule_yellow.view.*

private const val TYPE_PURPLE = 0
private const val TYPE_RED = 1
private const val TYPE_YELLOW = 2

class SchoolScheduleAdapter(
    var data: List<ClassDetailScheduleDao>,
    var callback: OnMoreCallback
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SchoolScheduleViewHolderPurple(
        itemView: View,
        val callback: OnMoreCallback,
        val context: Context
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: ClassDetailScheduleDao) {
            itemView.tvItemScheduleSubjectType.text = dataModel.type
            itemView.tvItemScheduleSubject.text = dataModel.title
            itemView.tvItemScheduleSubjectOpen.text = dataModel.open

            itemView.ibItemScheduleSubject.setOnClickListener {
                callback.onMoreClicked("1", adapterPosition)
            }

            itemView.clItemSchedulePurple.setOnClickListener {
                (context as MainClassDetailActivity).startActivity(Intent(context, SchoolScheduleFileActivity::class.java))
            }
        }
    }

    class SchoolScheduleViewHolderRed(
        itemView: View,
        val callback: OnMoreCallback,
        val context: Context
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: ClassDetailScheduleDao) {
            itemView.tvItemScheduleEskulType.text = dataModel.type
            itemView.tvItemScheduleEskul.text = dataModel.title
            itemView.tvItemScheduleEskulOpen.text = dataModel.open

            itemView.ibItemScheduleEskul.setOnClickListener {
                callback.onMoreClicked("1", adapterPosition)
            }

            itemView.clItemScheduleRed.setOnClickListener {
                (context as MainClassDetailActivity).startActivity(Intent(context, SchoolScheduleFileActivity::class.java))
            }
        }
    }

    class SchoolScheduleViewHolderYellow(
        itemView: View,
        val callback: OnMoreCallback,
        val context: Context
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: ClassDetailScheduleDao) {
            itemView.tvItemScheduleAcademicType.text = dataModel.type
            itemView.tvItemScheduleAcademic.text = dataModel.title
            itemView.tvItemScheduleAcademicOpen.text = dataModel.open

            itemView.ibItemScheduleAcademic.setOnClickListener {
                callback.onMoreClicked("1", adapterPosition)
            }

            itemView.clItemScheduleYellow.setOnClickListener {
                (context as MainClassDetailActivity).startActivity(Intent(context, SchoolScheduleFileActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_PURPLE) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_schedule_purple, parent, false)
            return SchoolScheduleViewHolderPurple(view, callback, parent.context)
        } else if (viewType == TYPE_RED) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_schedule_red, parent, false)
            return SchoolScheduleViewHolderRed(view, callback, parent.context)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_schedule_yellow, parent, false)
            return SchoolScheduleViewHolderYellow(view, callback, parent.context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_PURPLE) {
            (holder as SchoolScheduleViewHolderPurple).bind(data[position])
        } else if (getItemViewType(position) == TYPE_RED) {
            (holder as SchoolScheduleViewHolderRed).bind(data[position])
        } else {
            (holder as SchoolScheduleViewHolderYellow).bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].viewType == 0) {
            TYPE_PURPLE
        } else if (data[position].viewType == 1) {
            TYPE_RED
        } else {
            TYPE_YELLOW
        }
    }
}