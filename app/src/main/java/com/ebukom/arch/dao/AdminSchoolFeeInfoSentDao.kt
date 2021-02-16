package com.ebukom.arch.dao

import java.io.Serializable

data class AdminSchoolFeeInfoSentDao (
    var title: String,
    var detail: String,
    var date: String,
    var isChecked: Boolean = false
) : Serializable
