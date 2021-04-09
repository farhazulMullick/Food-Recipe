package com.example.foodrecipe.ui.fragments.instruction

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipe.R
import com.example.foodrecipe.adapter.IngredientsAdapter
import com.example.foodrecipe.databinding.FragmentIngredientBinding
import com.example.foodrecipe.databinding.FragmentInstructionBinding
import com.example.foodrecipe.modals.Result
import java.util.*


class InstructionFragment : Fragment() {
    private var _binding: FragmentInstructionBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientAdapter: IngredientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)

        val myBundle = arguments
        val args: Result? = myBundle?.getParcelable("recipeBundle")

        val myWebView = binding.webView
        val progressBar = binding.webViewProgress
        progressBar.visibility = View.VISIBLE

        myWebView.webViewClient = object : WebViewClient(){

            override fun onPageFinished(view: WebView?, url: String?) {

                if(progressBar.visibility == View.VISIBLE){
                    progressBar.visibility = View.INVISIBLE
                }
                super.onPageFinished(view, url)
            }
        }

        val instructionUrl = args?.sourceUrl!!
        myWebView.loadUrl(instructionUrl)

        return binding.root
    }
    

}