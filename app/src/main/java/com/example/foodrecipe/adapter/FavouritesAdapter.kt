package com.example.foodrecipe.adapter

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipe.R
import com.example.foodrecipe.data.database.entity.FavouritesEntity
import com.example.foodrecipe.databinding.FavouriteRowLayoutBinding
import com.example.foodrecipe.ui.fragments.favourites.FavouriteRecipesFragmentDirections
import com.example.foodrecipe.util.RecipesDiffUtil
import com.example.foodrecipe.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favourite_row_layout.view.*

class FavouritesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavouritesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private lateinit var rootView: View

    private lateinit var mActionMode: ActionMode

    private var myViewHolder = arrayListOf<MyViewHolder>()
    private var selectedRecipes = arrayListOf<FavouritesEntity>()
    private var favouriteRecipes = emptyList<FavouritesEntity>()

    class MyViewHolder(private val binding: FavouriteRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favouriteRecipeEntity: FavouritesEntity) {
            binding.favouritesEntity = favouriteRecipeEntity
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavouriteRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolder.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favouriteRecipes[position]
        holder.bind(currentRecipe)

        /**
         * Single - Click
         */

        holder.itemView.favourite_row_constraintLayout.setOnClickListener {

            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action = FavouriteRecipesFragmentDirections
                    .actionFavouriteRecipesFragmentToDetailsActivity(currentRecipe.favouriteResults)

                holder.itemView.findNavController().navigate(action)
            }

        }

        /**
         * Long - Click
         */
        holder.itemView.favourite_row_constraintLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                multiSelection = false
                false
            }

        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavouritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.cardStrokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(
                holder,
                R.color.selectedCardBackgroundColor,
                R.color.selectedCardStrokeColor
            )
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, bgColor: Int, strokeColor: Int) {
        holder.itemView.favourite_row_constraintLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, bgColor)
        )
        holder.itemView.favourite_recipe_card_view.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favouriteRecipes.size
    }

    fun setData(newFavouriteRecipe: List<FavouritesEntity>) {
        val favouritesDiffUtil = RecipesDiffUtil(favouriteRecipes, newFavouriteRecipe)
        val resultDiffUtil = DiffUtil.calculateDiff(favouritesDiffUtil)

        favouriteRecipes = newFavouriteRecipe
        resultDiffUtil.dispatchUpdatesTo(this)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mActionMode = mode!!
        mode?.menuInflater?.inflate(R.menu.favourite_contextual_menu, menu)
        setStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_contextual_menu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavouriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} recipe/s deleted")

            selectedRecipes.clear()
            multiSelection = false
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolder.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.cardStrokeColor)

        }
        multiSelection = false
        selectedRecipes.clear()
        setStatusBarColor(R.color.statusBarColor)
    }

    private fun setStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        )
            .setAction("Okay") {}
            .show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }


}