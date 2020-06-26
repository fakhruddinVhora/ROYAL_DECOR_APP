package com.example.royal_decor.Models

data class TallyLog(
    val id: String = "",
    val date: String = "",
    val paintername: String = "",
    val paintermob: String = "",
    /*val productsordered: String = "",*/
    val productMap: HashMap<String, Int> = HashMap(),
    val totalPoints: Int = 0
)