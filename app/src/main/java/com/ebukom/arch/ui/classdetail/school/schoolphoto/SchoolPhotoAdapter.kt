package com.ebukom.arch.ui.classdetail.school.schoolphoto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPhotoDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import kotlinx.android.synthetic.main.item_photo_blue.view.*
import kotlinx.android.synthetic.main.item_photo_red.view.*

private const val TYPE_RED = 0
private const val TYPE_BLUE = 1

class SchoolPhotoAdapter(
    var data: List<ClassDetailPhotoDao>,
    var callback: OnMoreCallback
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SchoolPhotoViewHolderRed(itemView: View, val callback: OnMoreCallback) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: ClassDetailPhotoDao) {
            itemView.tvItemPhotoRed.text = dataModel.photoTitle

            itemView.ibItemPhotoRed.setOnClickListener {
                callback.onMoreClicked("2")
            }
        }
    }

    class SchoolPhotoViewHolderBlue(itemView: View, val callback: OnMoreCallback) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: ClassDetailPhotoDao) {
            itemView.tvItemPhotoBlue.text = dataModel.photoTitle

            itemView.ibItemPhotoBlue.setOnClickListener {
                callback.onMoreClicked("2")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_RED) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_photo_red, parent, false)
            return SchoolPhotoViewHolderRed(view, callback)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_photo_blue, parent, false)
            return SchoolPhotoViewHolderBlue(view, callback)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_RED) {
            (holder as SchoolPhotoViewHolderRed).bind(data[position])
        } else {
            (holder as SchoolPhotoViewHolderBlue).bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
        return if (data[position].viewType == 0) {
            TYPE_RED
        } else
            TYPE_BLUE
    }
}