package com.example.foodrecipe.ui.fragments.recipes.recipebottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodrecipe.R
import com.example.foodrecipe.databinding.RecipeBottomSheetBinding
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodrecipe.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RecipeBottomSheet : BottomSheetDialogFragment() {

    private val recipeViewModel: RecipesViewModel by viewModels()
    private var _binding: RecipeBottomSheetBinding? = null
    private val binding get() = _binding!!

    // Global variables
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding =  RecipeBottomSheetBinding.inflate(inflater, container, false)

        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)

            mealTypeChip = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChipId = checkedId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)

            dietTypeChip = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChipId = checkedId
        }

        recipeViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, {value->
            updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)

        })

        // Apply button
        binding.applyButton.setOnClickListener{
            recipeViewModel.writeMealAndDietType(
                    mealTypeChip,
                    mealTypeChipId,
                    dietTypeChip,
                    dietTypeChipId
            )

            val action = RecipeBottomSheetDirections.actionRecipeBottomSheetToRecipeFragment(true)

            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if(chipId != 0){
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            }catch (e: Exception){
                Log.e("RecipeChipError", e.message.toString())
            }
        }
    }

}