package com.ebukom.arch.dao.firebase

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class RegisterSchoolRequestDao(
    val name : String,
    val role : RegisterRolesDao,
    val phone : String,
    @Exclude val pass : String,
    val profilePic : String,
    val level : Int = 0
) : Serializable

data class RegisterRolesDao(
    val classNumber : String,
    val className : String
) : Serializable