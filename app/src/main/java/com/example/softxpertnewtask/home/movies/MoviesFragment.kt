package com.example.softxpertnewtask.home.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.softxpertnewtask.R
import com.example.softxpertnewtask.shared.ItemOffsetDecoration
import com.example.softxpertnewtask.databinding.FragmentMoviesBinding
import com.example.softxpertnewtask.home.HomeFragmentDirections
import com.example.softxpertnewtask.home.MoviesDataItem
import com.example.softxpertnewtask.home.geners.CategoriesViewPagerAdapter
import com.example.softxpertnewtask.shared.AuthNetworkState
import com.example.softxpertnewtask.shared.SharedConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding
    private val adapter =
        MoviesAdapter(::onItemClicked)

    private val viewModel: MoviesViewModel by viewModels()

    var  genreId : String ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoviesBinding.inflate(layoutInflater)

        genreId = arguments?.getString(CategoriesViewPagerAdapter.GENRE_ID)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        viewModel.moviesData.observe(viewLifecycleOwner) { viewState ->
            updateUI(viewState)
        }
        viewModel.networkState.observe(viewLifecycleOwner) { viewState ->
            updateNetwork(viewState)
        }

        viewModel.getMoviesList(genreId)


    }

    private fun updateNetwork(authNetworkState: AuthNetworkState?) {

        if (authNetworkState == AuthNetworkState.LOADING) {
            binding.movieLoaderContainer.visibility = View.VISIBLE

        } else {
            binding.movieLoaderContainer.visibility = View.GONE
            when (authNetworkState) {
                AuthNetworkState.CONNECT_ERROR ->  SharedConstants.showSnackErrorInto(view, getString(
                    R.string.connection_error))
                AuthNetworkState.INVALID_CREDENTIALS ->
                    SharedConstants.showSnackErrorInto(view, getString(R.string.invalid_credential))
                AuthNetworkState.FAILURE -> SharedConstants.showSnackErrorInto(view, getString(R.string.failure))
                else -> {
                    Log.e("TAGMain" , "Error happened")
                }
            }

        }



    }

    private fun updateUI(moviesData: MoviesData?) {

        moviesData?.let {
            adapter.setData(moviesData.results)
        }


    }


    private fun setupRecycler() {
        val itemDecoration = ItemOffsetDecoration(requireContext(), 24)

        binding.moviesRecycler.layoutManager = GridLayoutManager(requireContext() , 2 )
        binding.moviesRecycler.addItemDecoration(itemDecoration)
        binding.moviesRecycler.adapter = adapter

    }

    private fun onItemClicked(viewState: Result) {
       //findNavController().navigate(MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment())
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment2(
            viewState.id))
    }
}