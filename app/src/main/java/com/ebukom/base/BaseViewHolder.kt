package com.ebukom.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class BaseViewHolder<T>(itemview: View) : RecyclerView.ViewHolder(itemview) {
    abstract fun onBind(pos: Int, count: Int, item: T)
}