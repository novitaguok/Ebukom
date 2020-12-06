package com.ebukom.arch.ui.classdetail.school.schoolphoto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPhotoDao
import kotlinx.android.synthetic.main.item_photo.view.*

class SchoolPhotoAdapter(
    var data: List<ClassDetailPhotoDao>,
    var context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SchoolPhotoViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(photo: ClassDetailPhotoDao) {
            itemView.tvItemPhoto.text = photo.photoTitle
            itemView.ibItemPhoto.setOnClickListener {
//                callback.onMoreClicked("2", photo.photoId)
                (context as SchoolPhotoActivity).popUpMenu(photo.photoId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return SchoolPhotoViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SchoolPhotoViewHolder).bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}