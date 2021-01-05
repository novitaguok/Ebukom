package com.ebukom.arch.ui.classdetail.school.schoolphoto

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Browser
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPhotoDao
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
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
                (context as SchoolPhotoActivity).popUpMenu(photo.photoId)
            }
            itemView.ivItemPhoto.setOnClickListener {
                val db = FirebaseFirestore.getInstance()
                var url = ""

                db.collection("classes").document(photo.classId).collection("photos")
                    .document(photo.photoId).get().addOnSuccessListener {
                        url = it["link"] as String
                        if (!url.startsWith("http://")) {
                            url = "http://" + url
                        }

                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        (context as SchoolPhotoActivity).startActivity(intent)
                    }
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