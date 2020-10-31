package com.ebukom.arch.dao

import java.io.Serializable

data class ClassDetailSchoolAnnouncementMonthDao (
    var month: String,
    var announcement: List<ClassDetailAnnouncementDao> = arrayListOf(),
    var viewType : Int = 0
)
