package com.ebukom.arch.ui.classdetail.school

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailSchoolInfoDao
import com.ebukom.arch.ui.chooseclass.ChooseClassViewHolder
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementFragment
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoFragment
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleActivity
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleFragment
import com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulefile.SchoolScheduleFileActivity
import kotlinx.android.synthetic.main.item_class.view.*
import kotlinx.android.synthetic.main.item_school_info.view.*

class SchoolFragmentAdapter(private val items: List<ClassDetailSchoolInfoDao>, var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_school_info, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind (items: ClassDetailSchoolInfoDao) {
            itemView.tvItemShoolInfoTitle.text = items?.title
            itemView.tvItemShoolInfoMore.text = "Lihat " + items?.type
            itemView.tvItemShoolInfoMore.setTextColor(items?.colorTheme)
            itemView.ivItemShoolInfo.setImageResource(items?.background)

            if (context is SchoolScheduleActivity) {
                itemView.clItemShoolInfo.setOnClickListener {
                    (context as SchoolScheduleActivity).startActivity(Intent((context as SchoolScheduleActivity), SchoolScheduleFileActivity::class.java))
                }
            } else {
                val position = adapterPosition
                if (position == 0) {
                    itemView.clItemShoolInfo.setOnClickListener {
                        (context as MainClassDetailActivity).startActivity(Intent(context, SchoolAnnouncementActivity::class.java))
                    }
                } else if (position == 1) {
                    itemView.clItemShoolInfo.setOnClickListener {
                        (context as MainClassDetailActivity).startActivity(Intent(context, SchoolScheduleActivity::class.java))
                    }
                } else {
                    itemView.clItemShoolInfo.setOnClickListener {
//                        (context as MainClassDetailActivity).startActivity(Intent(context, ::class.java))
                    }
                }
            }
        }
    }
}

//class SchoolFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//    override fun getItem(position: Int): Fragment {
//        when (position) {
//            0 -> {return SchoolAnnouncementFragment()}
//            1 -> {return SchoolScheduleFragment()}
//            2 -> {return SchoolPhotoFragment()}
//            else -> {return SchoolAnnouncementFragment()}
//        }
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        when (position) {
//            0 -> {return "PENGUMUMAN"}
//            1 -> {return "JADWAL"}
//            2 -> {return "FOTO"}
//        }
//        return super.getPageTitle(position)
//    }
//
//    override fun getCount(): Int {
//        return 3
//    }
//}
