package com.example.android.marveltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.marveltest.data.network.Network
import com.example.android.marveltest.ui.home.CharacterPagedListAdapter
import com.example.android.marveltest.ui.home.CharacterPagedListRepository
import com.example.android.marveltest.ui.home.HomeViewModel
import com.example.android.marveltest.ui.home.HomeViewModelFactory
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var characterPagedListRepository: CharacterPagedListRepository
    private lateinit var characterPagedListAdapter: CharacterPagedListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Network.marvelService.getCharacters(10, 1470)
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                Timber.i("Success $it")
//            },{
//                Timber.i("Failure $it")
//            })

        characterPagedListRepository =  CharacterPagedListRepository(Network.marvelService)
        viewModel = ViewModelProviders.of(this, HomeViewModelFactory(characterPagedListRepository))
            .get(HomeViewModel::class.java)
        characterPagedListAdapter = CharacterPagedListAdapter()
        recycler.adapter = characterPagedListAdapter


        viewModel.characterPagedList.observe(this,  Observer {
            characterPagedListAdapter.submitList(it)
        })

    }
}
