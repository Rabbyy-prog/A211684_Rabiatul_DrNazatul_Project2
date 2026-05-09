package com.example.a211684_rabiatul_drnazatulaini_project1.data

enum class PaymentType{
    EWALLET, CARD,FPX
}

data class PaymentMethod(
    val name: String,
    val type: PaymentType
)

data class Fee(
    val label:String,
    val value: String
)