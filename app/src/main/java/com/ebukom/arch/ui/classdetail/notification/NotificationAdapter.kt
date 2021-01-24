package com.ebukom.arch.ui.classdetail.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailCommentDao
import com.ebukom.arch.dao.ClassDetailNotificationDao
import com.ebukom.arch.ui.classdetail.personal.personalnotedetail.PersonalNoteDetailActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_material_preview.*
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter(
    var data: List<ClassDetailNotificationDao>,
    var context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(notification: ClassDetailNotificationDao) {
            Glide.with(context)
                .load(notification.picture)
                .centerCrop()
                .into(itemView.ivNotification)
            itemView.tvNotificationTitle.text = notification.title
            itemView.tvNotificationContext.text = notification.content
            itemView.clItemNotification.setOnClickListener {
                if (notification.type == 0) {
                    val intent = Intent(context, SchoolAnnouncementDetailActivity::class.java)
                    intent.putExtra("announcementId", notification.contentId)
                    intent.putExtra("classId", notification.classId)
                    intent.putExtra("notificationId", notification.notificationId)
                    context.startActivity(intent)
                } else {
                    val intent = Intent(context, PersonalNoteDetailActivity::class.java)
                    intent.putExtra("noteId", notification.contentId)
                    intent.putExtra("noteUploadTime", notification.time)
                    intent.putExtra("notificationId", notification.notificationId)
                    intent.putExtra("noteTitle", notification.title)
                    intent.putExtra("noteContent", notification.content)
                    context.startActivity(intent)
                }

                (context as NotificationActivity).finish()
            }
        }
    }
}
