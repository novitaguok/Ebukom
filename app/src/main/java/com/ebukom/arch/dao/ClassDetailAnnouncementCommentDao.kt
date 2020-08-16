package com.ebukom.arch.dao

import android.media.Image
import java.io.Serializable

data class ClassDetailAnnouncementCommentDao (
    var name: String,
    var comment : String,
    var profilePic : Int
): Serializable
