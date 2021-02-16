package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectnew

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailMaterialSubjectSectionDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfile.MaterialSubjectFileActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfilepreview.MaterialPreviewActivity
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoActivity
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

                if (sectionList.get(position).sectionName.contains(",")) {
                    var mFileList: ArrayList<ClassDetailAttachmentDao> = arrayListOf()

                    db.collection("material_subjects").document(sectionList.get(position).subjectId)
                        .collection("subject_sections")
                        .document(sectionList.get(position).sectionId).collection("files")
                        .get().addOnSuccessListener {
                            it.forEach {
                                mFileList.add(
                                    ClassDetailAttachmentDao(
                                        it["title"] as String,
                                        (it["category"] as Long).toInt()
                                    )
                                )
                            }

                            var url = mFileList[0].path
                            if (!url!!.startsWith("http://")) {
                                url = "http://" + url
                            }

                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            context.startActivity(intent)
                        }
                } else {
                    val intent = Intent(
                        (context as MaterialSubjectNewActivity),
                        MaterialSubjectFileActivity::class.java
                    )
                    intent.putExtra("subjectId", sectionList.get(position).subjectId)
                    intent.putExtra("sectionId", sectionList.get(position).sectionId)
                    intent.putExtra("sectionName", sectionList.get(position).sectionName)
                    (context as MaterialSubjectNewActivity).startActivity(intent)
                }
            } else if (context is MainClassDetailActivity) {
                val intent = Intent(
                    (context as MainClassDetailActivity),
                    MaterialSubjectFileActivity::class.java
                )
                intent.putExtra("subjectId", sectionList.get(position).subjectId)
                intent.putExtra("sectionId", sectionList.get(position).sectionId)
                intent.putExtra("sectionName", sectionList.get(position).sectionName)
                intent.putExtra("layout", "education")
                (context as MainClassDetailActivity).startActivity(intent)
            } else if (context is MaterialSubjectFileActivity) {
                val intent = Intent(
                    (context as MaterialSubjectFileActivity),
                    MaterialPreviewActivity::class.java
                )
                intent.putExtra("subjectId", sectionList.get(position).subjectId)
                intent.putExtra("sectionId", sectionList.get(position).sectionId)
                intent.putExtra("sectionName", sectionList.get(position).sectionName)
//                intent.putExtra("layout", "educationPreview")
                (context as MaterialSubjectFileActivity).startActivity(intent)
            }
        }

        holder.view.ibItemSubjectMaterial.setOnClickListener {
            if (context is MaterialSubjectNewActivity) {
                if (sectionList.get(position).sectionName.contains(",")) (context as MaterialSubjectNewActivity).popUpMenu(
                    sectionList.get(position).sectionId,
                    true
                )
                else (context as MaterialSubjectNewActivity).popUpMenu(sectionList.get(position).sectionId)

            } else if (context is MainClassDetailActivity)
                (context as MainClassDetailActivity).popupMenuInfo(
                    sectionList.get(position).sectionId,
                    position
                )
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface OnMoreCallback {
        fun onMoreClicked(id: String, position: Int)
    }
}