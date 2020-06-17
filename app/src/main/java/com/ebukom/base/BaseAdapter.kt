package com.ebukom.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


abstract class BaseAdapter <T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var data: MutableList<T> = ArrayList()
    protected var listCallback: ListCallback<T>? = null

    val all: List<T>
        get() = data

    @LayoutRes
    protected abstract fun setView(viewType: Int): Int
    protected abstract fun itemViewHolder(context: Context, view: View, viewType: Int): RecyclerView.ViewHolder

    fun setCallback(listCallback: ListCallback<T>) {
        this.listCallback = listCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(setView(viewType), parent, false)
        return itemViewHolder(parent.context, view, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            listCallback?.onReachTop(get(position))
        } else if (position == data.size - 1) {
            listCallback?.onReachEnd(position, get(position))
        }

        (holder as BaseViewHolder<T>).onBind(position,data.size,data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun add(t: T) {
        data.add(t)
        notifyItemInserted(itemCount - 1)
    }

    fun add(t: T, position: Int) {
        data.add(position, t)
        notifyItemInserted(position)
    }

    fun update(t: T, position: Int) {
        data.set(position, t)
        notifyItemChanged(position)
    }

    fun addAll(data: List<T>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun addNew(data: List<T>) {
        val oldSize = this.data.size
        this.data.addAll(data)
        notifyItemRangeInserted(oldSize, this.data.size)
    }

    fun remove(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    operator fun get(position: Int): T {
        return data[position]
    }

    fun clear() {
        this.data.clear()
        notifyDataSetChanged()
    }

    interface ListCallback<T> {
        fun onReachTop(item: T)

        fun onReachEnd(position: Int, item: T)
    }
}