package com.appdevelopement.passinggrade.dto

data class StudentWithGradedStatusDTO(
	val student: StudentDTO,
	val isGraded: Boolean
)
