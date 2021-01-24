package com.ebukom.arch.dao

import android.media.Image
import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

data class ClassDetailNotificationDao (
    var title: String,
    var content : String,
    var picture : String = "",
    var time: Timestamp = Timestamp(Date()),
    var notificationId: String = "",
    var type: Int = 0, // 0 = announcement, 1 = note
    var contentId: String = "",
    var classId: String = ""
): Serializable
