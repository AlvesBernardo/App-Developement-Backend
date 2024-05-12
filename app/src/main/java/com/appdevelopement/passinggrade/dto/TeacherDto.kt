package com.appdevelopement.passinggrade.dto
//this is good for or rest api serv. as it will reduce the number of client server calles
data class TeacherDto(
    val idTeacher: Int,
    val dtEmail: String,
    val dtName: String,
    val courses: List<CourseDto>
)
