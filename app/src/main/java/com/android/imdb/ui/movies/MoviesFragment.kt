package com.android.imdb.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android.imdb.R
import com.android.imdb.data.model.Movie
import com.android.imdb.databinding.FragmentMoviesBinding
import com.android.imdb.ui.MainActivity
import com.android.imdb.ui.detail.DetailFragment
import com.android.imdb.utils.newFragmentInstance
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped


@AndroidEntryPoint
class MoviesFragment : Fragment() {


    private var _binding: FragmentMoviesBinding? = null

    private val binding get() = _binding // This property is only valid between onCreateView and onDestroyView.
    private val moviesViewModel: MoviesViewModel by viewModels()
    private val adapter = MoviesAdapter(object : MoviesAdapter.OnMoviesItemOnClickListener {

        override fun onItemClick(movie: Movie) {
            if (activity is MainActivity) (activity as MainActivity).navigateToDetail(movie)
        }

    })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater)
        val view = binding?.root

        binding?.moviesRecyclerView?.adapter = adapter

        with(moviesViewModel) {

            loadMovies()

            searchResult.observe(this@MoviesFragment, Observer {
                adapter.mMovieList = it.search
            })

            isLoading.observe(this@MoviesFragment, Observer {
                binding?.movieProgressBar?.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
        return view

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        val FRAGMENT_NAME = MoviesFragment::class.java.simpleName
        private const val TAG = "MoviesFragment"
    }

}