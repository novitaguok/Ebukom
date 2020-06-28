package com.ebukom.arch.ui.chooseclass

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.base.BaseAdapter

class ChooseClassAdapter : BaseAdapter<ChooseClassDao>() {

    companion object{
        const val TYPE_GREEN = 0
        const val TYPE_BLUE = 1
    }

    override fun setView(viewType: Int): Int = when (viewType) {
        TYPE_GREEN -> R.layout.item_class_green
        else -> R.layout.item_class_blue
    }

    override fun itemViewHolder(
        context: Context,
        view: View,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        TYPE_GREEN -> ChooseClassViewHolderGreen(view, context)
        else -> ChooseClassViewHolderBlue(view, context)
    }

    override fun getItemViewType(position: Int): Int = data[position].viewType

}