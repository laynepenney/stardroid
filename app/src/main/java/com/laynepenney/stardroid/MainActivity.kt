package com.laynepenney.stardroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

@ExperimentalStdlibApi
class MainActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        findViewById<FloatingActionButton>(R.id.fab).visibility = View.GONE

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(findViewById(R.id.item_list))
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val repo = app.repo
        val adapter = FilmsRecyclerAdapter(this, repo.films, twoPane)
        recyclerView.adapter = adapter
        repo.films.observe(this) { result ->
            Log.d("main | observe", "result=$result")
            adapter.notifyDataSetChanged()
            when (result) {
                is Result.Failure -> showError(result.error)
                else -> Unit
            }
        }
    }

    private fun showError(e: Throwable) {
        Log.e("main | showError", "error", e)
        Toast.makeText(this, "error = ${e.message}", Toast.LENGTH_LONG).show()
    }
}

@ExperimentalStdlibApi
class FilmsRecyclerAdapter(
    private val parentActivity: AppCompatActivity,
    val data: LiveResult<FilmsResponse>,
    private val twoPane: Boolean
) : RecyclerView.Adapter<FilmsRecyclerAdapter.ViewHolder>() {

    private var values: List<Film> = emptyList()

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Film
            val id = item.episode_id.toString()

            Log.d("main | onClick", "film=$item, id=$id, twopane=$twoPane")
            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_ITEM_ID, id)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, id)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.title
        holder.contentView.text = item.release_date

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override
    fun getItemCount(): Int = data.value.films().let { films ->
        this.values = films
        films.size
    }.apply {
        Log.d("main | getItemCount", "count=$this")
    }

    private fun Result<FilmsResponse>?.films(): List<Film> = when (this) {
        is Result.Success<FilmsResponse> -> value.results
        else -> emptyList()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.id_text)
        val contentView: TextView = view.findViewById(R.id.content)
    }
}