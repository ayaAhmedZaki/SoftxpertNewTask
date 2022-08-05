package com.example.softxpertnewtask.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.softxpertnewtask.home.HomeRepository
import com.example.softxpertnewtask.home.movies.MoviesData
import com.example.softxpertnewtask.shared.AuthNetworkState
import com.example.softxpertnewtask.shared.AuthNetworkViewModel
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel  @Inject constructor(
    private val repository: HomeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : AuthNetworkViewModel()

{


    private val _searchData = MutableLiveData<SearchResultData?>(null)
    val searchData: LiveData<SearchResultData?>
        get() = _searchData


    fun getSearchResultList(queryText : String?) {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch(dispatcher) {
            repository.getSearchResult(queryText)
                .onSuccess {
                    setNetworkState(AuthNetworkState.SUCCESS)
                    Log.d("TAGModel" , "success")
                    _searchData.postValue(data)

                }.onError {
                    // loadData.postValue(false)
                    when (statusCode) {
                        StatusCode.Unauthorized -> setNetworkState(
                            AuthNetworkState.INVALID_CREDENTIALS
                        )
                        StatusCode.NotFound -> setNetworkState(
                            AuthNetworkState.NOT_FOUND
                        )
                        else -> setNetworkState(AuthNetworkState.FAILURE)
                    }
                }.onException {

                    Log.d("TAGModel" , "msg $message")
                    setNetworkState(AuthNetworkState.FAILURE)
                }
        }
    }
}