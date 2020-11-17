package com.laynepenney.stardroid

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import com.laynepenney.stardroid.databinding.ItemListBinding


class ItemListFragment : Fragment() {

    private val viewModel: ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ItemListBinding.inflate(inflater, container, false)
        val adapter = FilmsAdapter()
        binding.itemList.adapter = adapter
        observe(adapter)
        return binding.root
    }

    private fun observe(adapter: FilmsAdapter) {
        viewModel.films.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result.films())
        }
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


//    private fun setupRecyclerView(recyclerView: RecyclerView) {
//        val repo = app.repo
//        val adapter = FilmsRecyclerAdapter(this, repo.films, twoPane)
//        recyclerView.adapter = adapter
//        repo.films.observe(this) { result ->
//            Log.d("main | observe", "result=$result")
//            adapter.notifyDataSetChanged()
//            when (result) {
//                is Result.Failure -> showError(result.error)
//                else -> Unit
//            }
//        }
//    }
//
//    private fun showError(e: Throwable) {
//        Log.e("main | showError", "error", e)
//        Alert(e.message)
//            .show(supportFragmentManager, "alert")
////        val alert = AlertDialog(this)
////        Toast.makeText(this, "error = ${e.message}", Toast.LENGTH_LONG).show()
//    }
}

class MainActivity : AppCompatActivity() {

}

