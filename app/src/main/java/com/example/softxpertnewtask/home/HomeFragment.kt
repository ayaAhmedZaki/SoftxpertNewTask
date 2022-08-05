package com.example.softxpertnewtask.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.softxpertnewtask.R
import com.example.softxpertnewtask.databinding.FragmentHomeBinding
import com.example.softxpertnewtask.home.geners.CategoriesViewPagerAdapter
import com.example.softxpertnewtask.home.geners.Genre
import com.example.softxpertnewtask.home.geners.GenresData
import com.example.softxpertnewtask.shared.AuthNetworkState
import com.example.softxpertnewtask.shared.SharedConstants
import com.google.android.material.tabs.TabLayoutMediator
import com.skydoves.sandwich.StatusCode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {



    private lateinit var binding: FragmentHomeBinding

    lateinit var categoriesAdapter : CategoriesViewPagerAdapter
    private val viewModel: HomeViewModel by viewModels()
     var categoriesDataList = ArrayList<Genre>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGenresListTabs()

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            updateUI(viewState)
        }

        viewModel.networkState.observe(viewLifecycleOwner) { viewState ->
            updateNetwork(viewState)
        }

        getData()

        binding.searchLayout.searchImage.setOnClickListener {
            if (binding.searchLayout.searchText.text?.isNotEmpty()==true)
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment(binding.searchLayout.searchText.text.toString()))

        }

        binding.searchLayout.searchText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    // search_edit_text?.clearFocus()
    //                    search_edit_text?.let {
    //                        UtilKotlin.hideKeyboardEditText(search_edit_text , rootView)
    //                    }
                    if (binding.searchLayout.searchText.text?.isNotEmpty()==true)
                        findNavController().navigate(HomeFragmentDirections
                            .actionHomeFragmentToSearchFragment(binding.searchLayout.searchText.text.toString()))


                    return true
                }
                return false
            }
        })

    }

    private fun getData() {

        if (SharedConstants.checkForInternet(requireContext())) {
            viewModel.loadGenresList()
        } else {
            viewModel.setNetworkState(AuthNetworkState.CONNECT_ERROR)
        }
    }

    private fun updateNetwork(authNetworkState: AuthNetworkState?) {

        if (authNetworkState == AuthNetworkState.LOADING) {
            binding.loaderContainer.visibility = View.VISIBLE

        } else {
            binding.loaderContainer.visibility = View.GONE
            when (authNetworkState) {
                AuthNetworkState.CONNECT_ERROR ->  SharedConstants.showSnackErrorInto(view, getString(R.string.connection_error))
                AuthNetworkState.INVALID_CREDENTIALS ->
                    SharedConstants.showSnackErrorInto(view, getString(R.string.invalid_credential))
                AuthNetworkState.FAILURE -> SharedConstants.showSnackErrorInto(view, getString(R.string.failure))
                else -> {
                    Log.e("TAGMain" , "Error happened")
                }
            }

        }


    }


    private fun updateUI(viewState: GenresData?) {

        viewState?.let {
            categoriesDataList.clear()
            categoriesDataList.add(Genre(null , getString(R.string.all)))
            categoriesDataList.addAll(viewState.genres)
            categoriesAdapter.notifyDataSetChanged()
        }


    }

    private fun setupGenresListTabs() {
        categoriesAdapter = CategoriesViewPagerAdapter(this@HomeFragment , categoriesDataList)
        binding.moviesViewPager.adapter = categoriesAdapter

        binding.moviesViewPager.isUserInputEnabled = true

        TabLayoutMediator(binding.categoriesTabLayout, binding.moviesViewPager) { tab, position ->
            tab.text = (categoriesDataList[position].name)

        }.attach()


    }

}