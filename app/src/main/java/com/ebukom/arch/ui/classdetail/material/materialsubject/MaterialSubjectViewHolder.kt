package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.arch.dao.ClassDetailMaterialSubjectDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd.MaterialSubjectRecapActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectnew.MaterialSubjectNewActivity
import com.google.firebase.auth.internal.RecaptchaActivity
import kotlinx.android.synthetic.main.item_subject.view.*

class MaterialSubjectViewHolder(var view: View, var context: Context) :
    RecyclerView.ViewHolder(view) {
    fun onBind(item: ClassDetailMaterialSubjectDao) {
        view.tvItemSubjectName.text = item?.subjectName
        view.ivItemSubject.setImageResource(item?.background)
        view.clItemSubject.setOnClickListener {
            var intent =
                Intent((context as MainClassDetailActivity), MaterialSubjectNewActivity::class.java)
            intent.putExtra("subjectId", item.subjectId)
            intent.putExtra("classId", item.classId)
            intent.putExtra("subjectName", item.subjectName)
            (context as MainClassDetailActivity).startActivity(intent)
        }
    }
}