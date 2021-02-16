package com.ebukom.arch.dao

import android.net.Uri
import java.io.Serializable

data class ClassDetailAttachmentDao(
    var path: String?,
    var category: Int = 0,
    var attachmentId: String = "",
    var subjectId: String = "",
    var sectionId: String = "",
    var classTime: String = "",
    var fileName: String = ""
) : Serializable
