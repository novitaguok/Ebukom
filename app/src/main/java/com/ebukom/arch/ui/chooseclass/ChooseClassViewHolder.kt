package com.ebukom.arch.ui.chooseclass

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_class.view.*

class ChooseClassViewHolder(var view: View, var context: Context) : RecyclerView.ViewHolder(view) {
    val db = FirebaseFirestore.getInstance()
    var bg: Int = 0

    fun onBind(item: ChooseClassDao) {
        view.tvItemClassGrade.text = item?.classNumber
        view.tvItemClassGrade.setTextColor(item.colorTheme!!)
        view.tvItemClassName.text = item?.className
        view.tvItemClassTeacher.text = item?.teacher
        view.tvItemClassTeacher.setTextColor(item.colorTheme!!)

        when (item?.className) {
            "Krypton" -> {
                bg = R.drawable.bg_class_krypton
            }
            "Xenon" -> {
                bg = R.drawable.bg_class_xenon
            }
            "Argon" -> {
                bg = R.drawable.bg_class_argon
            }
            "Titanium" -> {
                bg = R.drawable.bg_class_titanium
            }
            "Neon" -> {
                bg = R.drawable.bg_class_neon
            }
            "Helium" -> {
                bg = R.drawable.bg_class_helium
            }
            "Argentum" -> {
                bg = R.drawable.bg_class_argentum
            }
            "Aurum" -> {
                bg = R.drawable.bg_class_aurum
            }
            else -> {
                bg = R.drawable.bg_class_selenium
            }
        }

        view.ivItemClass.setImageResource(bg)

        if (context is ChooseClassActivity) {
            view.ibItemClass.setOnClickListener {
                (context as ChooseClassActivity).popupMenu(item)
            }
            view.clItemClass.setOnClickListener {
                val intent = Intent(
                    (context as ChooseClassActivity),
                    MainClassDetailActivity::class.java
                )

//                db.collection("classes").get()

                intent.putExtra("classId", item.classId)
                intent.putExtra("className", item.className)
                (context as ChooseClassActivity).startActivity(intent)
            }
        } else {
            val uid = context.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
                .getString("uid", "") as String

            view.ibItemClass.visibility = View.INVISIBLE
            view.clItemClass.setOnClickListener {
                db.collection("classes").document(item.classId)
                    .update("class_teacher_ids", FieldValue.arrayUnion(uid))

                (context as Activity).finish()
            }
        }
    }
}