package com.ebukom.arch.dao

import android.media.Image
import java.io.Serializable

data class ClassDetailCommentDao (
    var name: String,
    var comment : String,
    var profilePic : Int,
    var time: String = "",
    var commentId: String = ""
): Serializable
