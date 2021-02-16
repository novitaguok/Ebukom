package com.ebukom.arch.dao

import java.io.Serializable

data class ClassDetailSchoolAnnouncementMonthDao (
    var month: String,
    var announcement: List<ClassDetailAnnouncementDao> = arrayListOf(),
    var viewType : Int = 0,
    var monthId : Int = 0,
    var date : Int = 0,
    var day : Int = 0,
    var dayName : String = "",
    var isSelected : Boolean = false
)
