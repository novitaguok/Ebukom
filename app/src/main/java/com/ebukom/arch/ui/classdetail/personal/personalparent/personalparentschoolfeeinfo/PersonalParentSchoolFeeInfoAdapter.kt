package com.ebukom.arch.ui.classdetail.personal.personalparent.personalparentschoolfeeinfo

import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.*
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import kotlinx.android.synthetic.main.item_schoolfee_purple.view.*
import kotlinx.android.synthetic.main.item_schoolfee_red.view.*

private const val TYPE_RED = 0
private const val TYPE_PURPLE = 1

class PersonalParentSchoolFeeInfoAdapter(
    var data: List<ClassDetailPersonalParentSchoolFeeDao>,
    var callback: OnMoreCallback,
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class PersonalParentSchoolFeeInfoAdapterViewHolderRed(
        itemView: View,
        val callback: OnMoreCallback
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(item: ClassDetailPersonalParentSchoolFeeDao) {
            itemView.tvItemSchoolfeeRedTitle.text = item.title
            itemView.tvItemSchoolfeeRedDate.text = item.date
            itemView.clItemSchoolfeeRed.setOnClickListener {
                (context as MainClassDetailActivity).startActivity(Intent(context, PersonalParentSchoolFeeInfoActivity::class.java))
            }
        }
    }

    inner class PersonalParentSchoolFeeInfoAdapterViewHolderPurple(
        itemView: View,
        val callback: OnMoreCallback
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: ClassDetailPersonalParentSchoolFeeDao) {
            itemView.tvItemSchoolfeePurpleTitle.text = dataModel.title
            itemView.tvItemSchoolfeePurpleDate.text = dataModel.date
            itemView.clItemSchoolfeePurple.setOnClickListener {
                (context as MainClassDetailActivity).startActivity(Intent(context, PersonalParentSchoolFeeInfoActivity::class.java))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PURPLE) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_schoolfee_purple, parent, false)
            PersonalParentSchoolFeeInfoAdapterViewHolderPurple(view, callback)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_schoolfee_red, parent, false)
            PersonalParentSchoolFeeInfoAdapterViewHolderRed(view, callback)
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
        return if (position % 2 == 1) {
            TYPE_RED
        } else {
            TYPE_PURPLE
        }
    }
}