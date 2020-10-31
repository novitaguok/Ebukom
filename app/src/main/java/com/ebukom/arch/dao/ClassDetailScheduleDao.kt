package com.ebukom.arch.dao

data class ClassDetailScheduleDao(
    var type: String,
    var title: String,
    var open: String,
    var file: String,
    var background: Int,
    var colorTheme: Int? = null
)
