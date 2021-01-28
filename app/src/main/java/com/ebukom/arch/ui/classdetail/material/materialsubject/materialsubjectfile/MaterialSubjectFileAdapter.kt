package com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectfilepreview.MaterialPreviewActivity
import kotlinx.android.synthetic.main.item_subject_material_file.view.*

class MaterialSubjectFileAdapter(private val data: List<ClassDetailAttachmentDao>) :
    RecyclerView.Adapter<MaterialSubjectFileAdapter.ViewHolder>() {
    private val items: MutableList<ClassDetailAttachmentDao>
    lateinit var context: Context

    init {
        this.items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subject_material_file, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        itemView: View,
        var context: Context
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(item: ClassDetailAttachmentDao) {
            itemView.tvItemSubjectMaterialFile.text = item.fileName

            when (item.category) {
                0 -> {
                    itemView.ivItemSubjectMaterialFile.setImageResource(R.drawable.ic_link_white)
                    itemView.llItemSubjectMaterialFile.setBackgroundColor(Color.parseColor("#3F3B5C"))
                }
                1 -> {
                    itemView.ivItemSubjectMaterialFile.setImageResource(R.drawable.ic_video_white)
                    itemView.llItemSubjectMaterialFile.setBackgroundColor(Color.parseColor("#155E8C"))
                }
                2 -> {
                    itemView.ivItemSubjectMaterialFile.setImageResource(R.drawable.ic_file_white)
                    itemView.llItemSubjectMaterialFile.setBackgroundColor(Color.parseColor("#A62121"))
                }
                else -> {
                    itemView.llItemSubjectMaterialFile.visibility = View.GONE
                }
            }

            itemView.clItemSubjectMaterialFile.setOnClickListener {
                if (item.category == 1) {
                    val intent = Intent(context, MaterialPreviewActivity::class.java)
                    intent.putExtra("fileId", item.attachmentId)
                    intent.putExtra("subjectId", item.subjectId)
                    intent.putExtra("sectionId", item.sectionId)
                    intent.putExtra("filePath", item.path)
                    intent.putExtra("category", item.category)
                    intent.putExtra("fileName", item.fileName)
                    context.startActivity(intent)
                } else {
                    val webpage = Uri.parse(item.path)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    context.startActivity(intent)
                }
            }

            itemView.ibItemSubjectMaterialFile.setOnClickListener {
                (context as MaterialSubjectFileActivity).popUpMenu(item.attachmentId)
            }
        }
    }
}