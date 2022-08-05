package com.example.softxpertnewtask.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class AuthNetworkViewModel: ViewModel(){

    private val _networkState = MutableLiveData<AuthNetworkState>(AuthNetworkState.NONE)
    val networkState: LiveData<AuthNetworkState>
        get() = _networkState


    fun setNetworkState(state:AuthNetworkState){
        _networkState.postValue(state)
    }



}