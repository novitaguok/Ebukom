package com.ebukom.arch.dao

data class AdminPaymentItemFormDao(
    var itemName: String,
    var itemFee: String,
    var itemEdit: Boolean = false
)
