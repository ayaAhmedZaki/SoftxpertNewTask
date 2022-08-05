package com.example.softxpertnewtask.home.geners

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.softxpertnewtask.home.movies.MoviesFragment

class CategoriesViewPagerAdapter(fragment: Fragment,
     private val arrayList: ArrayList<Genre>) : FragmentStateAdapter(fragment) {



    private var fragment: Fragment? = null

    override fun createFragment(position: Int): Fragment {
        fragment = MoviesFragment()
       // findNavController(fragment!!).navigate(HomeFragmentDirections.actionHomeFragmentToMoviesFragment())

        return if (position == 0){
            fragment!!
        } else{
            val args  = Bundle()
            args.putString(GENRE_ID, arrayList[position].id.toString())
            fragment!!.arguments = args
            fragment!!
        }



    }

    override fun getItemCount(): Int {
        Log.d("TAGADApter" , "size ${arrayList.size}")

        return arrayList.size
    }


    companion object{

        public val GENRE_ID = "GenreId"

    }


}