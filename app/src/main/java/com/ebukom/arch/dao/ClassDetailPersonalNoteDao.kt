package com.ebukom.arch.dao

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class ClassDetailPersonalNoteDao(
    var profilePicture: Int,
    var noteTitle: String,
    var noteContent: String,
    var comments : ArrayList<ClassDetailAnnouncementCommentDao> = arrayListOf(),
    var time: String,
    var attachments: List<ClassDetailAttachmentDao> = arrayListOf(),
    var noteId: String = "",
    var uploadTime: Timestamp = Timestamp(Date())
) : Serializable
