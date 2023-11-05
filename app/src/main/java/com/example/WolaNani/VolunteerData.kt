package com.example.WolaNani

data class VolunteerData(
    val name: String,
    val reason: String,
    val phoneNumber: String,
    val address: String
) {
    constructor() : this("", "", "", "")
}


