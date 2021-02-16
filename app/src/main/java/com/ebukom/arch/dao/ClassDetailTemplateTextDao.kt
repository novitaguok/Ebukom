package com.ebukom.arch.dao

import java.io.Serializable

data class ClassDetailTemplateTextDao (
    var title: String,
    var content: String,
    var materi: Int = -1
) : Serializable
