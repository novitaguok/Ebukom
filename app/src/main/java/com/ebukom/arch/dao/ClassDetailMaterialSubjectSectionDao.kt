package com.ebukom.arch.dao

data class ClassDetailMaterialSubjectSectionDao(
    var sectionName: String,
    var sectionFile: String,
    var sectionVideo: String,
    var sectionLink: String,
    var sectionId: String = "",
    var subjectId: String = ""
)
