package com.ebukom.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import org.angmarch.views.NiceSpinner

fun ImageView.load(context: Context,url : String){
    Glide.with(context)
        .load(url)
        .into(this)
}
fun ImageView.load(context: Context,res : Int){
    Glide.with(context)
        .load(res)
        .into(this)
}

fun NiceSpinner.load(context: Context, datas : List<String>){
    this.attachDataSource(datas)
    this.selectedIndex = 0
    this.setText(datas[0])
}