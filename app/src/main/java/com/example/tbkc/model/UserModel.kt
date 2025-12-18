package com.example.tbkc.model

data class UserModel(
    val id : String = "",
    val fullName : String = "",
    val gender : String = "",
    val dob : String = "",
    val email : String = "",
){
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "id" to id,
            "fullName" to fullName,
            "gender" to gender,
            "dob" to dob,
            "email" to email
        )
    }
}
