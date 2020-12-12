package com.ebukom.arch.dao.firebase

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class RegisterParentRequestDao(
    val name : String,
    val child : String,
    val phone : String,
    val eskul : List<String>,
    @Exclude val pass : String,
    val profilePic : String,
    val level : Int = 1
) : Serializable