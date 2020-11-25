package com.ebukom.arch.dao

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

data class ClassDetailAnnouncementDao (
    var announcementTitle: String,
    var announcementContent : String,
    var comments : List<ClassDetailAnnouncementCommentDao> = arrayListOf(),
    var time : String = "",
    var attachments : List<ClassDetailAttachmentDao> = arrayListOf(),
    var teacherName : String = "",
    var announcementId: String = "",
    var timestamp : Timestamp = Timestamp(Date())
) : Serializable
