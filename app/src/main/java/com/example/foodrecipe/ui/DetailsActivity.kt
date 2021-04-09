package com.example.foodrecipe.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.example.foodrecipe.R
import com.example.foodrecipe.adapter.PagerAdapter
import com.example.foodrecipe.data.database.entity.FavouritesEntity
import com.example.foodrecipe.databinding.ActivityDetailsBinding
import com.example.foodrecipe.ui.fragments.ingredients.IngredientFragment
import com.example.foodrecipe.ui.fragments.instruction.InstructionFragment
import com.example.foodrecipe.ui.fragments.overview.OverViewFragment
import com.example.foodrecipe.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private  var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var pagerAdapter: PagerAdapter
    private var isSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolBar = binding.toolBar

        setSupportActionBar(toolBar)
        toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = listOf("Overview", "Ingredients", "Instructions")
        val fragments = listOf(OverViewFragment(), IngredientFragment(), InstructionFragment())
        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle", args.result)

        pagerAdapter = PagerAdapter(
            title,
            fragments,
            resultBundle,
            supportFragmentManager)


        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)

        val menuItem = menu?.findItem(R.id.menu_detail_favourite)
        checkSavedRecipe(menuItem!!)
        return super.onCreateOptionsMenu(menu)
    }

    private fun checkSavedRecipe(menuItem: MenuItem) {
        try {
            mainViewModel.readFavouriteRecipes.observe(this, {favouritesEntity ->

                for(savedRecipes in favouritesEntity){
                    if (savedRecipes.favouriteResults.id == args.result.id) {
                        setFavouriteMenuIconColor(menuItem, R.color.red)
                        savedRecipeId = savedRecipes.id
                        isSaved = true
                    }
                    else{
                        setFavouriteMenuIconColor(menuItem, R.color.white)
                    }
                }
            })
        }catch (e: Exception){
            Log.d("menu-icon-color", e.message.toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.menu_detail_favourite && !isSaved) {
            saveToFavourites(item)
        } else if (item.itemId == R.id.menu_detail_favourite && isSaved) {
            deleteFromFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavourites(item: MenuItem) {
        val favouritesEntity = FavouritesEntity(
                0,
                args.result
        )
        mainViewModel.insertFavouriteRecipe(favouritesEntity)
        setFavouriteMenuIconColor(item, R.color.red)
        showSnackBar("Recipe Saved")

        isSaved = true
    }

    private fun deleteFromFavourites(item: MenuItem){
        val favouritesEntity = FavouritesEntity(
                savedRecipeId,
                args.result
        )
        isSaved = false
        mainViewModel.deleteFavouriteRecipe(favouritesEntity)
        setFavouriteMenuIconColor(item, R.color.white)
        showSnackBar("Removed From Favourites.")
    }

    private fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun setFavouriteMenuIconColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}