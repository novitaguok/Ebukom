package com.ebukom.arch.dao

data class ClassDetailItemCheckThumbnailDao (
    var name: String,
    var desc: String,
    var profilePic: Int,
    var isChecked : Boolean = false,
    var userId : String = ""
)
