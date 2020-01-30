package com.example.android.marveltest.data.repository

import androidx.paging.PageKeyedDataSource
import com.example.android.marveltest.data.network.Character
import com.example.android.marveltest.data.network.MarvelService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CharacterDataSource(
    private val marvelService: MarvelService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Character>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Character>
    ) {
        compositeDisposable.add(
            marvelService.getCharacters(10,0)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.data.results, null, 10)
                    },
                    {
                       // callback.onRetryableError(it)
                        loadInitial(params, callback)
                    }
                )
        )

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        compositeDisposable.add(
            marvelService.getCharacters(10,params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.data.limit + it.data.offset <= it.data.total) {
                            callback.onResult(it.data.results, params.key+10)
                        } else {
                            Timber.i("Se acabó")
                        }
                    },
                    {
                       // callback.onRetryableError(it)
                        loadAfter(params, callback)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
    }

}