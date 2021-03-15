package com.example.foodrecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodrecipe.R
import com.example.foodrecipe.modals.ExtendedIngredient
import com.example.foodrecipe.util.Constants.Companion.BASE_INGREDIENT_IMAGE_URL
import com.example.foodrecipe.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.recipe_ingredients_layout.view.*

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredients = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_ingredients_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentIngredient = ingredients[position]

        holder.itemView.ingredient_imageView
                .load(BASE_INGREDIENT_IMAGE_URL + currentIngredient.image )

        holder.itemView.ingredient_title_textView.text = currentIngredient.name
        holder.itemView.amount_textView.text =  currentIngredient.amount.toString()
        holder.itemView.unit_textView.text = currentIngredient.unit
        holder.itemView.consistency_textView.text = currentIngredient.consistency
        holder.itemView.original_textView.text = currentIngredient.original
    }

    override fun getItemCount(): Int {
       return ingredients.size
    }

    fun setData(newIngredient: List<ExtendedIngredient>){
        val ingredientsDiffUtil = RecipesDiffUtil(oldList = ingredients, newList = newIngredient )
        val resultIngredientDiffUtil = DiffUtil.calculateDiff(ingredientsDiffUtil)

        ingredients = newIngredient
        resultIngredientDiffUtil.dispatchUpdatesTo(this)
    }
}