package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectnew

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMaterialSubjectSectionDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
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
        holder.view.tvItemSubjectMaterial.text = sectionList.get(position).sectionName
        holder.view.clItemSubjectMaterial.setOnClickListener {
            if (context is MaterialSubjectNewActivity) {
                val intent = Intent(
                    (context as MaterialSubjectNewActivity),
                    MaterialSubjectFileActivity::class.java
                )
                intent.putExtra("subjectId", sectionList.get(position).subjectId)
                intent.putExtra("sectionId", sectionList.get(position).sectionId)
                intent.putExtra("sectionName", sectionList.get(position).sectionName)
                (context as MaterialSubjectNewActivity).startActivity(intent)
            } else {
                val intent = Intent(
                    (context as MainClassDetailActivity),
                    MaterialSubjectFileActivity::class.java
                )
                intent.putExtra("subjectId", sectionList.get(position).subjectId)
                intent.putExtra("sectionId", sectionList.get(position).sectionId)
                intent.putExtra("sectionName", sectionList.get(position).sectionName)
                intent.putExtra("layout", "educationNew")
                (context as MainClassDetailActivity).startActivity(intent)
            }
        }

        holder.view.ibItemSubjectMaterial.setOnClickListener {
            if (context is MaterialSubjectNewActivity) (context as MaterialSubjectNewActivity).popUpMenu(
                sectionList.get(position).sectionId
            )
            else {
                (context as MainClassDetailActivity).popupMenuInfo(sectionList.get(position).sectionId, position)
            }
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface OnMoreCallback {
        fun onMoreClicked(id: String, position: Int)
    }
}