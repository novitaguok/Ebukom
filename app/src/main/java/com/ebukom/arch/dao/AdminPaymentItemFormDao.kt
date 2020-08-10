package com.ebukom.arch.dao

import java.util.*

data class AdminPaymentItemFormDao (
    var itemName: String,
    var itemFee: String,
    var itemEdit: Boolean = false
)
