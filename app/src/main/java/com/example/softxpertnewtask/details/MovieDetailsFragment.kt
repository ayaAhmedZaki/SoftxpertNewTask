package com.example.softxpertnewtask.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.softxpertnewtask.R
import com.example.softxpertnewtask.databinding.FragmentMovieDetailsBinding
import com.example.softxpertnewtask.shared.AuthNetworkState
import com.example.softxpertnewtask.shared.SharedConstants.IMAGE_URL
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var  binding : FragmentMovieDetailsBinding
    private val args : MovieDetailsFragmentArgs by navArgs()
    private val viewModel: MovieDetailsViewModel by viewModels()

    val imageList = ArrayList<SlideModel>()

    var movieId : Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailsBinding.inflate(layoutInflater)


        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar
            .setupWithNavController(findNavController(), appBarConfiguration)

        movieId = args.movieId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.movieDetailsData.observe(viewLifecycleOwner) { viewState ->
            updateUI(viewState)
        }
        viewModel.networkState.observe(viewLifecycleOwner) { viewState ->
            updateNetwork(viewState)
        }

        viewModel.getMovieDetailsList(movieId)

    }

    private fun updateNetwork(authNetworkState: AuthNetworkState?) {

    }

    private fun updateUI(movieDetailsData: MovieDetailsData?) {

       movieDetailsData?.let {
           //setup Slider
           imageList.clear()
           imageList.add(SlideModel("$IMAGE_URL${movieDetailsData?.backdrop_path?:""}"))
           imageList.add(SlideModel("$IMAGE_URL${movieDetailsData?.poster_path?:""}"))
           binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)

           binding.toolbar.title = movieDetailsData?.title?:""

           binding.movieName.text = movieDetailsData?.title?:""
           binding.movieDescription.text = movieDetailsData?.overview?:""

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

               val date  =  LocalDate.parse(movieDetailsData?.release_date
                   , DateTimeFormatter.ofPattern("yyyy-MM-dd"))
               //binding.movieYear.setText(date.year)
               binding.movieYear.text = "${getString(R.string.Year_production)} ${date.year}"

           } else {
               binding.movieYear.text = movieDetailsData?.release_date
           }


           binding.movieRating.rating = (movieDetailsData?.vote_average?.toFloat() ?: 0f) / 2.0f

       }



    }
}