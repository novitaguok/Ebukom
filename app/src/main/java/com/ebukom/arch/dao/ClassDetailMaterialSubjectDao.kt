package com.ebukom.arch.dao

data class ClassDetailMaterialSubjectDao(
    var subjectName: String,
    var background: Int,
    var order: Int,
    var subjectId: String = "",
    var classId: String = ""
)
