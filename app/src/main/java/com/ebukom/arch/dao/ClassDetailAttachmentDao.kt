package com.ebukom.arch.dao

import java.io.Serializable

data class ClassDetailAttachmentDao(
    var path: String,
    var category: Int
) : Serializable
