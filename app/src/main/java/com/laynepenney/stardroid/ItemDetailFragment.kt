package com.laynepenney.stardroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */

class ItemDetailFragment : Fragment() {

    private lateinit var item: LiveData<Film>
    private lateinit var episodeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cache = requireContext().app.repo.cache
        episodeId = requireArguments().getString(ARG_ITEM_ID)!!
        item = cache.getFilm(episodeId)
        item.observe(this) { film ->
            activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = film.title
            view?.updateFilm(film)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.film_detail, container, false)
        rootView.updateFilm(item.value)
        return rootView
    }

    fun View.updateFilm(film: Film?) {
        this.findViewById<TextView>(R.id.item_detail)?.text = film?.opening_crawl
        this.findViewById<TextView>(R.id.director)?.text = film?.director
        this.findViewById<TextView>(R.id.release_date)?.text = film?.release_date
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}