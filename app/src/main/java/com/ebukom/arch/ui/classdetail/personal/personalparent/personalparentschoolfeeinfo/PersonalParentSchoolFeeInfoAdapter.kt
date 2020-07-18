package com.ebukom.arch.ui.classdetail.personal.personalparent.personalparentschoolfeeinfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.*
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoAdapter
import com.ebukom.base.BaseAdapter
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.item_photo_blue.view.*
import kotlinx.android.synthetic.main.item_photo_red.view.*
import kotlinx.android.synthetic.main.item_schedule_purple.view.*
import kotlinx.android.synthetic.main.item_schedule_red.view.*
import kotlinx.android.synthetic.main.item_schedule_yellow.view.*
import kotlinx.android.synthetic.main.item_schoolfee_purple.view.*
import kotlinx.android.synthetic.main.item_schoolfee_red.view.*

private const val TYPE_RED = 0
private const val TYPE_PURPLE = 1

class PersonalParentSchoolFeeInfoAdapter(
    var data: List<ClassDetailPersonalParentSchoolFeeDao>,
    var callback: OnMoreCallback
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class PersonalParentSchoolFeeInfoAdapterViewHolderRed(itemView: View, val callback: OnMoreCallback) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: ClassDetailPersonalParentSchoolFeeDao) {
            itemView.tvItemSchoolfeeRedTitle.text = dataModel.title
            itemView.tvItemSchoolfeeRedDate.text = dataModel.date
        }
    }

    class PersonalParentSchoolFeeInfoAdapterViewHolderPurple(itemView: View, val callback: OnMoreCallback) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: ClassDetailPersonalParentSchoolFeeDao) {
            itemView.tvItemSchoolfeePurpleTitle.text = dataModel.title
            itemView.tvItemSchoolfeePurpleDate.text = dataModel.date
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_PURPLE) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_schoolfee_purple, parent, false)
            return PersonalParentSchoolFeeInfoAdapterViewHolderPurple(view, callback)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_schoolfee_red, parent, false)
            return PersonalParentSchoolFeeInfoAdapterViewHolderRed(view, callback)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_PURPLE) {
            (holder as PersonalParentSchoolFeeInfoAdapterViewHolderPurple).bind(data[position])
        } else {
            (holder as PersonalParentSchoolFeeInfoAdapterViewHolderRed).bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].viewType == 0) {
            TYPE_RED
        } else {
            TYPE_PURPLE
        }
    }
}