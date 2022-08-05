package com.example.softxpertnewtask.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softxpertnewtask.home.geners.GenresData
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
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : AuthNetworkViewModel() {


    private val _viewState = MutableLiveData<GenresData?>(null)
    val viewState: LiveData<GenresData?>
        get() = _viewState


    fun loadGenresList() {
        setNetworkState(AuthNetworkState.LOADING)
        viewModelScope.launch(dispatcher) {
            repository.getGenres()
                .onSuccess {
                    setNetworkState(AuthNetworkState.SUCCESS)
                    Log.d("TAGModel" , "success")

                    _viewState.postValue(data)

                }.onError {
                    // loadData.postValue(false)
                    Log.d("TAGModel" , "errorCode $statusCode")
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
                    Log.d("TAGModel" , "errorExc")
                    Log.d("TAGModel" , "msg $message")
                    setNetworkState(AuthNetworkState.FAILURE)

                }
        }
    }


}