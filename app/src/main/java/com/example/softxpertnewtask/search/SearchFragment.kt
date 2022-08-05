package com.example.softxpertnewtask.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.softxpertnewtask.R
import com.example.softxpertnewtask.databinding.FragmentSearchBinding
import com.example.softxpertnewtask.shared.AuthNetworkState
import com.example.softxpertnewtask.shared.ItemOffsetDecoration
import com.example.softxpertnewtask.search.Result
import com.example.softxpertnewtask.shared.SharedConstants

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {


    private val args : SearchFragmentArgs by navArgs()
    private lateinit var binding : FragmentSearchBinding
    private val adapter = SearchResultAdapter(::onItemClicked)

    private val viewModel: SearchViewModel by viewModels()

    var searchText : String ? = ""

    val request = { viewModel.getSearchResultList(searchText) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchText = args.searchText
        binding.searchContainer.searchText.setText(searchText)

        setupRecycler()

        binding.searchContainer.searchImage.setOnClickListener {
            if (binding.searchContainer.searchText.text?.isNotEmpty()==true)
            {
                searchText = binding.searchContainer.searchText.text.toString()
                request.invoke()

            }

        }

        binding.searchContainer.searchText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (binding.searchContainer.searchText.text?.isNotEmpty()==true)
                    {
                        searchText = binding.searchContainer.searchText.text.toString()
                        request.invoke()
                    }

                    return true
                }
                return false
            }
        })



        viewModel.searchData.observe(viewLifecycleOwner) { viewState ->
            updateUI(viewState)
        }
        viewModel.networkState.observe(viewLifecycleOwner) { viewState ->
            updateNetwork(viewState)
        }
        request.invoke()

    }

    private fun updateNetwork(authNetworkState: AuthNetworkState?) {
        if (authNetworkState == AuthNetworkState.LOADING) {
            binding.resultLoaderContainer.visibility = View.VISIBLE

        } else {
            binding.resultLoaderContainer.visibility = View.GONE
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

    private fun updateUI(searchResultData: SearchResultData?) {

        searchResultData?.let {
            adapter.setData(searchResultData.results)
        }

    }

    private fun setupRecycler() {


        val itemDecoration = ItemOffsetDecoration(requireContext(), 24)

        binding.resultRecycler.layoutManager = GridLayoutManager(requireContext() , 2 )
        binding.resultRecycler.addItemDecoration(itemDecoration)
        binding.resultRecycler.adapter = adapter
    }

    private fun onItemClicked(viewState: Result) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(
            viewState.id
        ))

    }
}