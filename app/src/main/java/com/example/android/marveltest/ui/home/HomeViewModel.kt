package com.example.android.marveltest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.android.marveltest.data.network.Character
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(private val characterPagedListRepository: CharacterPagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val characterPagedList : LiveData<PagedList<Character>> by lazy {
        characterPagedListRepository.fetchLiveCharacterPagedList(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}