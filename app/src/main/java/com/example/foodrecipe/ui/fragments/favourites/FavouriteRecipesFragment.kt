package com.example.foodrecipe.ui.fragments.favourites



import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipe.R
import com.example.foodrecipe.adapter.FavouritesAdapter
import com.example.foodrecipe.databinding.FragmentFavouriteRecipesBinding
import com.example.foodrecipe.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable.cancel

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {
    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var recyclerView: RecyclerView
    private val favouriteAdapter by lazy { FavouritesAdapter(requireActivity(), mainViewModel) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mAdapter = favouriteAdapter
        binding.mainViewModel = mainViewModel


        setUpRecyclerView()
        setHasOptionsMenu(true);
        return binding.root
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.favouritesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = favouriteAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourite_recipes_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.deleteAll_favourite_menu -> {
                showWarningDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showWarningDialog(){
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        mainViewModel.deleteAllFavouriteRecipes()
                        showSnackBar("Deleted All Favourite Recipes.")
                    })
                setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            }
            // Set other dialog properties
            builder?.setTitle("Delete All?")
                .setMessage("This action will delete all of your favourite recipes")

            // Create the AlertDialog
            builder.create()
            builder.show()
        }

    }

    private fun showSnackBar(message: String){
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        )
            .setAction("Okay"){}
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        favouriteAdapter.clearContextualActionMode()
    }

}