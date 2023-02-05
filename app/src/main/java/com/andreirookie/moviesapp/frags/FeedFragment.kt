package com.andreirookie.moviesapp.frags

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.andreirookie.moviesapp.R
import com.andreirookie.moviesapp.adapter.MovieAdapter
import com.andreirookie.moviesapp.adapter.OnInteractionListener
import com.andreirookie.moviesapp.data.Movie
import com.andreirookie.moviesapp.databinding.FeedFragmentBinding
import com.andreirookie.moviesapp.frags.MovieFragment.Companion.movieArg
import com.andreirookie.moviesapp.viewModel.MovieViewModel

class FeedFragment : Fragment() {

    private val viewModel by viewModels<MovieViewModel>(ownerProducer = ::requireParentFragment)

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding =FeedFragmentBinding.inflate(inflater, container, false)

        val adapter = MovieAdapter(
            object  : OnInteractionListener {
                override fun onLikeClick(movie: Movie) {
                    viewModel.toLike(movie)
                }

                override fun onMovieItemBindingClick(movie: Movie) {
                    viewModel.goToMovieFragment(movie)
                }


            }
        )
        viewModel.loadTop100()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
//        viewModel.dataFromLocalDb.observe(viewLifecycleOwner) {
//            adapter.submitList(it)
//        }
        viewModel.dataFromWeb.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.movies)
            println("state.movies ${state.movies}")
            println("state.loading ${state.loading}")
            binding.progressBar.isVisible= state.loading
            binding.errorGroup.isVisible = state.error
            binding.emptyText.isVisible = state.empty
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadTop100()
        }


        val simpleItemAnimator = binding.recyclerView.itemAnimator as SimpleItemAnimator
        simpleItemAnimator.supportsChangeAnimations = false

        viewModel.navigateToMovieFragEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.from_feedFragment_to_movieFragment,
                Bundle().apply { movieArg = it }
            )
        }

        binding.favoriteButton.setOnClickListener{
            viewModel.showFavorites()
            binding.screenTitleTextView.text = "Избранное"
        }

        binding.popularButton.setOnClickListener {
            viewModel.showAll()
            binding.screenTitleTextView.text = "Популярное"
        }


        val swipeRefreshBinding = binding.swipeRefreshLayout
        swipeRefreshBinding.setColorSchemeColors(
            R.color.blue_200,
            R.color.blue_500,
            R.color.blue_700)
        swipeRefreshBinding.setOnRefreshListener {
            viewModel.loadTop100()
            swipeRefreshBinding.isRefreshing = false
        }



        return binding.root
    }




}