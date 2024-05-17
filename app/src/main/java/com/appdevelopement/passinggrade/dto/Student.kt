package com.appdevelopement.passinggrade.dto

//
//Sure, happy to explain!
//A Data Transfer Object (DTO) is a simple object that carries data between processes. It's typically used when you want to transfer more complex data structures across network boundaries or across layers in an application, and particularly when that transfer involves expensive operations such as database calls, API requests, serialization, etc.
data class Student(
    val studentName: String,
    val StudentNumber: Int,
    val isGraded: Boolean
)