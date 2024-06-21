package com.appdevelopement.passinggrade.dto

data class CompentenceDto(
    val idComptence: Int,
    val idExam: Int,
    val dtName: String,
    val dtCompetenceWeight: Int,
    val dtMustPass: Boolean
)
