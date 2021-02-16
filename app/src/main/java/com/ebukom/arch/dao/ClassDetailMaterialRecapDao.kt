package com.ebukom.arch.dao

import com.google.firebase.Timestamp
import java.util.*

data class ClassDetailMaterialRecapDao(
    var recapLink: String,
    var timestamp: Timestamp = Timestamp(Date()),
    var materialRecapId: String = "",
    var subjectId: String = ""
)
