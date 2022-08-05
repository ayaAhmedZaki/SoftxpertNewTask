package com.example.softxpertnewtask.search

data class SearchResultData(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)