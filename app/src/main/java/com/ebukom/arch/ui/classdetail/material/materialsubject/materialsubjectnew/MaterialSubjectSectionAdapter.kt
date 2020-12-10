package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectnew

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMaterialSubjectSectionDao
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfile.MaterialSubjectFileActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_subject_material.view.*
import timber.log.Timber

class MaterialSubjectSectionAdapter(
    val sectionList: ArrayList<ClassDetailMaterialSubjectSectionDao>,
    var context: Context
) :
    RecyclerView.Adapter<MaterialSubjectSectionAdapter.ViewHolder>() {

    var id = ""

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

        val db = FirebaseFirestore.getInstance()

        db.collection("material_subjects").addSnapshotListener { value, error ->
            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            for (document in value!!.documents) {
                if ((document["order_number"] as Long).toInt() == 0) {
                    id = document.id
                }
            }
        }

//        if (sectionList.get(position).subjectId == id) {
//            var dayName = ""
//            var monthName = ""
//            val date = sectionList.get(position).timestamp.toDate().date.toString()
//            val year = sectionList.get(position).timestamp.toDate().year.toString()
//
//            when (sectionList.get(position).timestamp.toDate().day.toString()) {
//                "0" -> dayName = "Minggu"
//                "1" -> dayName = "Senin"
//                "2" -> dayName = "Selasa"
//                "3" -> dayName = "Rabu"
//                "4" -> dayName = "Kamis"
//                "5" -> dayName = "Jumat"
//                "6" -> dayName = "Sabtu"
//            }
//
//            when (sectionList.get(position).timestamp.toDate().month.toString()) {
//                "0" -> monthName = "Januari"
//                "1" -> monthName = "Febuari"
//                "2" -> monthName = "Maret"
//                "3" -> monthName = "April"
//                "4" -> monthName = "Mei"
//                "5" -> monthName = "Juni"
//                "6" -> monthName = "Juli"
//                "7" -> monthName = "Agustus"
//                "8" -> monthName = "September"
//                "9" -> monthName = "Oktober"
//                "10" -> monthName = "November"
//                "11" -> monthName = "Desember"
//            }
//
//            holder.view.tvItemSubjectMaterial.text =
//                dayName + ", " + date + " " + monthName + " " + year
//        } else {
        holder.view.tvItemSubjectMaterial.text = sectionList.get(position).sectionName
        
//        }

        holder.view.clItemSubjectMaterial.setOnClickListener {
            val intent = Intent(
                (context as MaterialSubjectNewActivity),
                MaterialSubjectFileActivity::class.java
            )
            intent.putExtra("subjectId", sectionList.get(position).subjectId)
            intent.putExtra("sectionId", sectionList.get(position).sectionId)
            intent.putExtra("sectionName", sectionList.get(position).sectionName)
            (context as MaterialSubjectNewActivity).startActivity(intent)
        }

        holder.view.ibItemSubjectMaterial.setOnClickListener {
            (context as MaterialSubjectNewActivity).popUpMenu(sectionList.get(position).sectionId)
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}