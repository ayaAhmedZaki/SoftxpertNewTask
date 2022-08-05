package com.example.softxpertnewtask.shared

import com.example.softxpertnewtask.details.MovieDetailsData
import com.example.softxpertnewtask.home.geners.GenresData
import com.example.softxpertnewtask.home.movies.MoviesData
import com.example.softxpertnewtask.search.SearchResultData
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {


    @GET("genre/movie/list")
    suspend fun getGenresList(@Query("api_key") apiKey: String): ApiResponse<GenresData>


    @GET("discover/movie")
    suspend fun getMoviesList(@Query("api_key") apiKey: String
                              , @Query("with_genres") genreId: String?)
    : ApiResponse<MoviesData>


    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int? , @Query("api_key") apiKey: String)
            : ApiResponse<MovieDetailsData>


    @GET("search/movie")
    suspend fun getSearchResult(@Query("api_key") apiKey: String
                              , @Query("query") searchQuery: String?)
            : ApiResponse<SearchResultData>

}