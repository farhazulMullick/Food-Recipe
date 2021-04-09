package com.example.foodrecipe.ui.fragments.overview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.example.foodrecipe.R
import com.example.foodrecipe.databinding.FragmentOverViewBinding
import com.example.foodrecipe.modals.Result
import org.jsoup.Jsoup
import java.lang.Exception


class OverViewFragment : Fragment() {
    private var _binding: FragmentOverViewBinding? = null
    private val binding get () = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOverViewBinding.inflate(inflater, container, false)

        val myBundle = arguments
        val args: Result? = myBundle?.getParcelable("recipeBundle")

        try {
            binding.mainRecipeImageView.load(args?.image)
            binding.textViewLikes.text = args?.aggregateLikes.toString()
            binding.textViewTime.text = args?.readyInMinutes.toString()

            binding.recipeName.text = args?.title

            val summary = args?.summary
            binding.summaryTxt.text = Jsoup.parse(summary).text()




                if(args?.vegan == true) {
                    binding.veganCheck
                            .setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))

                    binding.veganTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }

                if (args?.vegetarian == true)  {
                    binding.vegetarianCheck
                            .setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))

                    binding.vegetarianTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }

                if(args?.dairyFree == true)  {
                    binding.diaryFreeCheck
                            .setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))

                    binding.diaryFreeTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }

                if(args?.cheap == true) {
                    binding.cheapCheck
                            .setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))

                    binding.cheapTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }

                if (args?.veryHealthy  == true) {
                    binding.healthyCheck
                            .setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))

                    binding.healthyTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }

                if(args?.glutenFree == true)  {
                    binding.glutenFreeCheck
                            .setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))

                    binding.glutenFreeTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }

        }catch (e: Exception){
            Log.d("OverViewFragment", e.message.toString())
        }



        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}