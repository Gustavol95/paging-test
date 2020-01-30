package com.example.android.marveltest.ui.home

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.android.marveltest.data.network.Character
import com.example.android.marveltest.data.network.MarvelService
import com.example.android.marveltest.data.repository.CharacterDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class CharacterPagedListRepository (private val marvelService: MarvelService) {

    lateinit var characterPagedList: LiveData<PagedList<Character>>
    lateinit var characterDataSourceFactory: CharacterDataSourceFactory

    fun fetchLiveCharacterPagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Character>> {
        characterDataSourceFactory = CharacterDataSourceFactory(marvelService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        characterPagedList = LivePagedListBuilder(characterDataSourceFactory, config).build()

        return characterPagedList
    }


}