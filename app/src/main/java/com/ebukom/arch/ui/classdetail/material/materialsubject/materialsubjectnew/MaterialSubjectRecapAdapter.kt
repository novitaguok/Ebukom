package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectnew

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMaterialRecapDao
import com.ebukom.arch.dao.ClassDetailMaterialSubjectSectionDao
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.item_subject_material.view.*

class MaterialSubjectRecapAdapter(
    val sectionList: ArrayList<ClassDetailMaterialRecapDao>,
    var context: Context
) :
    RecyclerView.Adapter<MaterialSubjectRecapAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subject_material, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var dayName = ""
        var monthName = ""
        val date = sectionList.get(position).timestamp.toDate().date.toString()
        val year = sectionList.get(position).timestamp.toDate().year.toString()

        when (sectionList.get(position).timestamp.toDate().day.toString()) {
            "0" -> dayName = "Minggu"
            "1" -> dayName = "Senin"
            "2" -> dayName = "Selasa"
            "3" -> dayName = "Rabu"
            "4" -> dayName = "Kamis"
            "5" -> dayName = "Jumat"
            "6" -> dayName = "Sabtu"
        }

        when (sectionList.get(position).timestamp.toDate().month.toString()) {
            "0" -> monthName = "Januari"
            "1" -> monthName = "Febuari"
            "2" -> monthName = "Maret"
            "3" -> monthName = "April"
            "4" -> monthName = "Mei"
            "5" -> monthName = "Juni"
            "6" -> monthName = "Juli"
            "7" -> monthName = "Agustus"
            "8" -> monthName = "September"
            "9" -> monthName = "Oktober"
            "10" -> monthName = "November"
            "11" -> monthName = "Desember"
        }

        holder.view.tvItemSubjectMaterial.text = dayName + ", " + date + " " + monthName + " " + year
//        holder.view.ibItemSubjectMaterial.setOnClickListener {
//            (context as MaterialSubjectNewActivity).popUpMenu(list?.get(position), position)
//        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}