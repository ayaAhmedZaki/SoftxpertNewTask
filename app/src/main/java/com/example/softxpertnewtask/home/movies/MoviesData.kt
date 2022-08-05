package com.example.softxpertnewtask.home.movies

data class MoviesData(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int,
    val status_message: String,
    val success: Boolean,
    val status_code: Int
)