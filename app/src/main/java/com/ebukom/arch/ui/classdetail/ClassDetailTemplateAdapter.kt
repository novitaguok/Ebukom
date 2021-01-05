package com.ebukom.arch.ui.classdetail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.material.materialeducation.MaterialEducationFragment
import com.ebukom.arch.ui.classdetail.material.materialeducation.materialeducationdetail.MaterialEducationDetailActivity
import com.ebukom.arch.ui.classdetail.personal.personalnotenew.PersonalNoteNewActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementdetail.SchoolAnnouncementDetailActivity
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew.SchoolAnnouncementNewActivity
import kotlinx.android.synthetic.main.activity_school_anouncement_new.*
import kotlinx.android.synthetic.main.item_announcement.view.*
import kotlinx.android.synthetic.main.item_template.view.*

class ClassDetailTemplateAdapter(
    var templates: List<ClassDetailTemplateTextDao>,
    var context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var intent: Intent

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_template, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return templates.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(templates[position])
    }

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ClassDetailTemplateTextDao) {
            itemView.tvItemTemplate.text = data.title
            itemView.tvItemTemplate.setOnClickListener {
                if (context is SchoolAnnouncementNewActivity) (context as SchoolAnnouncementNewActivity).setText(data.title, data.content)
                else (context as PersonalNoteNewActivity).setText(data.title, data.content)
            }
        }
    }
}
