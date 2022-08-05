package com.example.softxpertnewtask.home

import com.example.softxpertnewtask.details.MovieDetailsData
import com.example.softxpertnewtask.home.geners.GenresData
import com.example.softxpertnewtask.home.movies.MoviesData
import com.example.softxpertnewtask.search.SearchResultData
import com.example.softxpertnewtask.shared.RetrofitService
import com.example.softxpertnewtask.shared.SharedConstants.API_KEY
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(private val service : RetrofitService){

    suspend fun getGenres() :ApiResponse<GenresData>{
        return withContext(Dispatchers.IO){
            service.getGenresList(API_KEY)
        }


    }

    suspend fun getMovies(genreId : String?) :ApiResponse<MoviesData>{
        return withContext(Dispatchers.IO){
            service.getMoviesList(API_KEY , genreId)
        }


    }

    suspend fun getMovieDetails(movieId : Int?) :ApiResponse<MovieDetailsData>{
        return withContext(Dispatchers.IO){
            service.getMovieDetails(movieId , API_KEY)
        }


    }

    suspend fun getSearchResult(searchQuery : String?) :ApiResponse<SearchResultData>{
        return withContext(Dispatchers.IO){
            service.getSearchResult(API_KEY , searchQuery)
        }


    }




}