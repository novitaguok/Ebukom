package com.ebukom.arch.dao

data class ChooseClassDao(
    var classNumber: String? = null,
    var className: String,
    var teacher: String? = null,
    var background: Int,
    var colorTheme: Int? = null,
    var classId: String = ""
)
