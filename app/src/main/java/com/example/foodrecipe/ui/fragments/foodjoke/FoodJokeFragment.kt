package com.example.foodrecipe.ui.fragments.foodjoke

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.foodrecipe.R
import com.example.foodrecipe.databinding.FragmentFoodJokeBinding
import com.example.foodrecipe.util.Constants.Companion.API_KEY
import com.example.foodrecipe.util.NetworkResult
import com.example.foodrecipe.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {
    private var _binding: FragmentFoodJokeBinding? = null
    private val  binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        requestJokes()
        return binding.root
    }

    private fun requestJokes(){
        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner, { response->
            when(response){
                is NetworkResult.Success ->{
                    binding.jokeCardView.visibility = View.VISIBLE
                    binding.noJokeImageView.visibility = View.INVISIBLE
                    binding.noJokeTextView.visibility = View.INVISIBLE
                    binding.jokeTextView.text = response.data!!.text

                }

                is NetworkResult.Error ->{
                    loadJokesFromCache()
                    Toast.makeText(
                        requireContext(),
                        "Couldn't Load Jokes",
                        Toast.LENGTH_SHORT
                    )

                }

                is NetworkResult.Loading ->{

                }

            }
        })
    }

    private fun loadJokesFromCache(){
        mainViewModel.readOfflineJokes.observe(viewLifecycleOwner, {jokes->
            if(jokes.isNotEmpty()) {
                binding.jokeTextView.text = jokes[0].foodJoke.text
                Log.d("FoodjokeFragment", "local database is called")
            }
            else{
                binding.jokeCardView.visibility = View.INVISIBLE
                binding.noJokeImageView.visibility = View.VISIBLE
                binding.noJokeTextView.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}