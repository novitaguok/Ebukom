package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.content.Context
import android.view.View
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_announcement.view.*

class SchoolAnnouncementViewHolder(var view: View, var context: Context) :
    BaseViewHolder<ClassDetailAnnouncementDao>(view) {

    override fun onBind(pos: Int, count: Int, item: ClassDetailAnnouncementDao) {

        view.tvAnnouncementTitle.text = item.announcementTitle
        view.tvAnnouncementContent.text = item.announcementContent
        view.tvAnnouncementComment.text = item.comment
        view.tvAnnouncementTime.text = item.time

//        if (context is MainClassDetailActivity) {
//            view.ivAnnouncementMoreButton.setOnClickListener {
//                (context as MainClassDetailActivity).popupMenu()
//            }
//        } else {
//            view.ibItemClassTwo.visibility = View.INVISIBLE
//        }
    }
}