package com.ebukom.arch.dao

import android.media.Image

data class ClassDetailMemberContactDao(
    var teacherName: String,
    var childName: String,
    var profilePic: String,
    var phone: String? = "08123456789"
)
