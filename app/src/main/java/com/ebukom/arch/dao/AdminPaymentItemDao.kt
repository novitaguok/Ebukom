package com.ebukom.arch.dao

import java.io.Serializable

data class AdminPaymentItemDao(
    var parentName: String,
    var childName: String?,
    var eskuls: String,
    var classes: String?,
    var time: String,
    var note: String?,
    var items: List<AdminPaymentItemFormDao> = arrayListOf(),
    var recipients: List<AdminSchoolFeeInfoSentDao> = arrayListOf()
) : Serializable
