package com.example.newsfeedapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsfeedapp.MainActivity
import com.example.newsfeedapp.R
import com.example.newsfeedapp.databinding.FragmentArticleBinding
import com.example.newsfeedapp.databinding.FragmentBreakingNewsBinding
import com.example.newsfeedapp.viewmodel.NewsViewModel


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
        viewModel = (activity as MainActivity).mainViewModel
        val article  = args.article
        binding.webView.apply{
            webViewClient = WebViewClient()
            loadUrl(article.url!!)
        }


    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
