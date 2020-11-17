package com.laynepenney.stardroid

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import com.laynepenney.stardroid.databinding.FragmentItemListBinding

class FilmListFragment : Fragment() {

    private val viewModel: ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentItemListBinding.inflate(inflater, container, false)
        val adapter = FilmsAdapter()
        binding.itemList.itemList.adapter = adapter
        observe(adapter)
        return binding.root
    }

    private fun observe(adapter: FilmsAdapter) {
        viewModel.films.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result.films())
            if (result is Result.Failure) {
                showError(result.error)
            }
        }
    }

    private fun showError(e: Throwable) {
        Log.e("main | showError", "error", e)
        Alert(e.message)
            .show(parentFragmentManager, "alert")
    }

    class ViewModel(
        application: Application
    ) : AndroidViewModel(application) {
        val films = application.app.repo.films
    }

    private fun Result<FilmsResponse>?.films(): List<Film> = when (this) {
        is Result.Success<FilmsResponse> -> value.results
        else -> emptyList()
    }
}