package com.example.android.marveltest.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.android.marveltest.data.network.Character
import com.example.android.marveltest.data.network.MarvelService
import io.reactivex.disposables.CompositeDisposable


class CharacterDataSourceFactory(
    private val marvelService: MarvelService,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Character>() {

    val chatacterLiveDataSource = MutableLiveData<CharacterDataSource>()

    override fun create(): DataSource<Int, Character> {
        val characterDataSource = CharacterDataSource(marvelService, compositeDisposable)
        chatacterLiveDataSource.postValue(characterDataSource)
        return characterDataSource
    }

}