package com.ebukom.arch.dao

data class ChooseClassDao(
    var classNumber: String? = null,
    var className: String,
    var teacher: String? = null,
    var background: Int = 0,
    var colorTheme: Int? = null,
    var classId: String = "",
    var isChecked: Boolean = false
)
