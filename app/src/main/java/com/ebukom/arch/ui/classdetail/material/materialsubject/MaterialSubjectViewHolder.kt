package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMaterialSubjectDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd.MaterialSubjectRecapActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectnew.MaterialSubjectNewActivity
import com.google.firebase.auth.internal.RecaptchaActivity
import kotlinx.android.synthetic.main.item_subject.view.*

class MaterialSubjectViewHolder(var view: View, var context: Context) :
    RecyclerView.ViewHolder(view) {

    var bg: Int = 0

    fun onBind(item: ClassDetailMaterialSubjectDao) {
        view.tvItemSubjectName.text = item?.subjectName

        when (item?.subjectName) {
            "Rekap Pembelajaran\nOnline" -> {
                bg = R.drawable.bg_subject_recap
            }
            "Matematika" -> {
                bg = R.drawable.bg_subject_math
            }
            "Bahasa Indonesia" -> {
                bg = R.drawable.bg_subject_indo
            }
            "Art" -> {
                bg = R.drawable.bg_subject_art
            }
            "Literasi" -> {
                bg = R.drawable.bg_subject_literature
            }
            "Agama" -> {
                bg = R.drawable.bg_subject_religion
            }
            "PPKn" -> {
                bg = R.drawable.bg_subject_pkn
            }
            "Musik Online" -> {
                bg = R.drawable.bg_subject_music
            }
            "PLH" -> {
                bg = R.drawable.bg_subject_plh
            }
        }
        view.ivItemSubject.setImageResource(bg)
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