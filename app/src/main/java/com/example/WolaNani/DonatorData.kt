package com.example.WolaNani

data class DonatorData(
    val reference: String,
    val item: String,
    val phoneNumber: String
) {
    constructor() : this("", "", "")
}


