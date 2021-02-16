package com.ebukom.arch.dao

import java.io.Serializable

data class AdminPaymentItemFormDao(
    var itemName: String,
    var itemFee: String,
    var itemEdit: Boolean = false
): Serializable
