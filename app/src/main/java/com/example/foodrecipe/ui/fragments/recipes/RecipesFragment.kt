package com.example.foodrecipe.ui.fragments.recipes

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.foodrecipe.R
import com.example.foodrecipe.viewmodels.MainViewModel
import com.example.foodrecipe.adapter.RecipeAdapter
import com.example.foodrecipe.databinding.FragmentRecipesBinding
import com.example.foodrecipe.util.Constants.Companion.API_KEY
import com.example.foodrecipe.util.NetworkListener
import com.example.foodrecipe.util.NetworkResult
import com.example.foodrecipe.util.observeOnce
import com.example.foodrecipe.viewmodels.RecipesViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding  get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()
    private val recipeViewModel by viewModels<RecipesViewModel>()

    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var  networkListener: NetworkListener

    private lateinit var recyclerView: RecyclerView
    private val adapter  by lazy { RecipeAdapter() }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.mainViewModels = mainViewModel
        binding.lifecycleOwner = this

        setUpRecyclerView()
        readfromDataBase()
        setHasOptionsMenu(true)

        recipeViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipeViewModel.backOnline = it
            Log.d("BackOnline", "${recipeViewModel.backOnline}")
        })

        binding.swipeToRefresh.setOnRefreshListener {
            Log.d("Recipe Fragment", "Swipe to refresh action called")
            requestApi()

            binding.swipeToRefresh.isRefreshing = false

        }

        binding.recipesFab.setOnClickListener {
            if(recipeViewModel.networkStatus){
                findNavController().navigate(R.id.action_recipeFragment_to_recipeBottomSheet)
            }else{
                Toast.makeText(requireContext(), "You are Offline", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                    .collect(){ status-> // Boolean
                        recipeViewModel.networkStatus = status
                        recipeViewModel.showNetworkStatus()
                        Log.d("NetworkListener", "$status")
                    }
        }


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.recipe_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(searchQuery: String?): Boolean {
                if (searchQuery != null) {
                    showShimmerEffect()
                    searchQuery(searchQuery)
                }
                return true
            }

            override fun onQueryTextChange(searchQuery: String?): Boolean {
                return true
            }

        })
    }

    private fun searchQuery(searchQuery: String){

        Log.d("Recipe Fragment", "Search query started")
        mainViewModel.searchRecipes(recipeViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    adapter.setData(response.data!!)
                }

                is NetworkResult.Error ->{
                    hideShimmerEffect()
                    loadFromCache()
                    Toast.makeText(requireContext(), response.message.toString()
                    , Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }

        })
    }


    private fun readfromDataBase() {
        mainViewModel.readOfflineRecipe.observeOnce(viewLifecycleOwner,  {database->
            if(database.isNotEmpty() && !args.backFromBottomSheet){
                loadFromCache()

            }else{
                requestApi()
            }
        })
    }

    private fun loadFromCache(){
        lifecycleScope.launch {
            mainViewModel.readOfflineRecipe.observe(viewLifecycleOwner, {
                if(it.isNotEmpty()){
                    adapter.setData(it[0].foodRecipe)
                    hideShimmerEffect()
                    Log.i("Recipe Fragment", "local database is called!")
                }
            })
        }
    }

    private fun requestApi(){
        Log.i("Recipe Fragment", "Api is called")
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is NetworkResult.Success ->{
                    hideShimmerEffect()
                    adapter.setData(response.data!!)
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadFromCache()
                    Toast.makeText(requireContext(), response.message.toString(),
                    Toast.LENGTH_LONG
                    ).show()
                }

                is NetworkResult.Loading ->{
                    showShimmerEffect()
                }
            }
        })
    }

    private fun setUpRecyclerView(){
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //Scroll Down hide FAB
                if (dy > 0){
                    binding.recipesFab.hide()
                }
                //Scroll Up Show FAB
                else if (dy < 0){
                    binding.recipesFab.show()
                }
            }
        })
        showShimmerEffect()


    }

    private fun showShimmerEffect(){
        (recyclerView as ShimmerRecyclerView).showShimmer()
    }

    private fun hideShimmerEffect(){
        (recyclerView as ShimmerRecyclerView).hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}