package com.example.android.marveltest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.android.marveltest.data.network.Network
import com.example.android.marveltest.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val repository = CharacterPagedListRepository(Network.marvelService)
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this, HomeViewModelFactory(repository))
            .get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.recycler.adapter = CharacterPagedListAdapter()
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        return binding.root
    }
}