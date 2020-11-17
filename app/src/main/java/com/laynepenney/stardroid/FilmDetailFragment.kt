package com.laynepenney.stardroid

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.navArgs
import com.laynepenney.stardroid.databinding.FragmentItemDetailBinding

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */

class FilmDetailFragment : Fragment() {
    private val viewModel: ViewModel by viewModels()
    val args by navArgs<FilmDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        observe(binding)
        return binding.root
    }

    private fun observe(binding: FragmentItemDetailBinding) {
        viewModel.cache.getFilm(args.episodeId).observe(viewLifecycleOwner) { film ->
            binding.film = film
        }
    }

    class ViewModel(
        application: Application
    ) : AndroidViewModel(application) {
        val cache = application.app.repo.cache
    }
}