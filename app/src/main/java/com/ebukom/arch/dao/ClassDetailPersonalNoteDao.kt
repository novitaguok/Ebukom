package com.ebukom.arch.dao

import java.io.Serializable

data class ClassDetailPersonalNoteDao(
    var profilePicture: Int,
    var noteTitle: String,
    var noteContent: String,
    var comments : List<ClassDetailAnnouncementCommentDao> = arrayListOf(),
    var time: String,
    var attachments: List<ClassDetailAttachmentDao> = arrayListOf()
) : Serializable
