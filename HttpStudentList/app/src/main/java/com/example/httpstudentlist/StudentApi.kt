package com.example.httpstudentlist

import retrofit2.Call
import retrofit2.http.GET

interface StudentApi {
    @GET("students")
    fun getStudents(): Call<List<Student>>
}