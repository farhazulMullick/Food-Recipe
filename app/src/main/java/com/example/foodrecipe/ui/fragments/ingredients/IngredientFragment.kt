package com.example.foodrecipe.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipe.R
import com.example.foodrecipe.adapter.IngredientsAdapter
import com.example.foodrecipe.databinding.FragmentIngredientBinding
import com.example.foodrecipe.modals.ExtendedIngredient
import com.example.foodrecipe.modals.Result


class IngredientFragment : Fragment() {
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientAdapter: IngredientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)

        val args: Result? = arguments?.getParcelable("recipeBundle")


        ingredientAdapter = IngredientsAdapter()
        val recyclerView = binding.ingredientsRecyclerView

        args?.extendedIngredients?.let {
            ingredientAdapter.setData(it) 
        }
        recyclerView.adapter = ingredientAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

}