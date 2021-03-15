package com.example.foodrecipe.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.example.foodrecipe.R
import com.example.foodrecipe.adapter.PagerAdapter
import com.example.foodrecipe.databinding.ActivityDetailsBinding
import com.example.foodrecipe.ui.fragments.ingredients.IngredientFragment
import com.example.foodrecipe.ui.fragments.instruction.InstructionFragment
import com.example.foodrecipe.ui.fragments.overview.OverViewFragment

class DetailsActivity : AppCompatActivity() {
    private  var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsActivityArgs>()

    private lateinit var pagerAdapter: PagerAdapter

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}