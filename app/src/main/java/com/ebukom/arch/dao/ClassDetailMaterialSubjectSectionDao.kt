package com.ebukom.arch.dao

import com.google.firebase.Timestamp
import java.util.*

data class `ClassDetailMaterialSubjectSectionDao`(
    var sectionName: String = "",
    var sectionFile: List<ClassDetailAttachmentDao> = arrayListOf(),
    var time: String = "",
    var sectionId: String = "",
    var subjectId: String = "",
    var classId: String = "",
    var timestamp: Timestamp = Timestamp(Date())
)
