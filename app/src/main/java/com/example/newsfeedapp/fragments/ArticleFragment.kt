package com.example.newsfeedapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.newsfeedapp.MainActivity
import com.example.newsfeedapp.R
import com.example.newsfeedapp.databinding.FragmentArticleBinding
import com.example.newsfeedapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel:NewsViewModel

    val args:ArticleFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentArticleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article  = args.article

        if(viewModel.hasInternetConnection()){
        binding.webView.apply{
            webViewClient = WebViewClient()
            loadUrl(article.url!!)
        }
        }
        else{
            Toast.makeText(activity,"No Internet connection",Toast.LENGTH_LONG).show()
        }


        binding.fab.setOnClickListener{
            viewModel.insertArticle(article)
            Snackbar.make(view,"Article saved successfully ",Snackbar.LENGTH_LONG).show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
