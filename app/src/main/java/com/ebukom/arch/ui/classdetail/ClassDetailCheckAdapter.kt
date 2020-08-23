package com.ebukom.arch.ui.classdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailItemCheckDao
import kotlinx.android.synthetic.main.item_check.view.*

class ClassDetailCheckAdapter(
    public val list: ArrayList<ClassDetailItemCheckDao>,
    val callback: OnCheckListener
) :
    RecyclerView.Adapter<ClassDetailCheckAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_check, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list?.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.tvItemCheck.text = list?.get(position)?.item

        holder.view.cbItemCheck.isChecked = list[position].isChecked

        if (position == list?.size - 1) {
            holder.view.vItemCheck.visibility = View.INVISIBLE
        }
    }

    inner class ViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view) {
        init {
            view.cbItemCheck.setOnCheckedChangeListener { _, isChecked ->
                list[adapterPosition].isChecked = isChecked
                callback.onCheckChange()
            }
        }
    }

    interface OnCheckListener {
        fun onCheckChange()
    }
}