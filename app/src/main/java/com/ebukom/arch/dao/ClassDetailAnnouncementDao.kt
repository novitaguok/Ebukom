package com.ebukom.arch.dao

import java.io.Serializable

data class ClassDetailAnnouncementDao (
    var announcementTitle: String,
    var announcementContent : String,
    var comments : List<ClassDetailAnnouncementCommentDao> = arrayListOf(),
    var time : String = "",
    var attachments : List<ClassDetailAttachmentDao> = arrayListOf(),
    var teacherName : String = ""
) : Serializable
