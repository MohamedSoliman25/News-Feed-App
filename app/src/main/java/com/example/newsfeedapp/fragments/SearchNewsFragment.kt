package com.example.newsfeedapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeedapp.MainActivity
import com.example.newsfeedapp.R
import com.example.newsfeedapp.adapters.NewsAdapter
import com.example.newsfeedapp.databinding.FragmentSearchNewsBinding
import com.example.newsfeedapp.utils.Constants
import com.example.newsfeedapp.utils.Resource
import com.example.newsfeedapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private  val TAG = "SearchNewsFragment"
    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsViewModel

    private lateinit var newsAdapter: NewsAdapter


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchNewsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setUpRecyclerView()
        var job:Job? = null
        binding.etSearch.addTextChangedListener{editable->
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_TIME_DELAY)
                editable?.let {
                    if(!editable.toString().trim().isEmpty()){
                        viewModel.getSearchNews(editable.toString())

                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles?.toList())
//                        newsAdapter.setList(newsResponse.articles.toList())
//                        newsAdapter.notifyDataSetChanged()
                        val totalPages = newsResponse.totalResults!! / Constants.QUERY_PAGE_SIZE +2
                        isLastPage = viewModel.searchNewsPageNumber ==totalPages
                        if(isLastPage){
                            binding.rvSearchNews.setPadding(0,0,0,0)

                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message " )
                        Toast.makeText(activity,"An error occurred :$message", Toast.LENGTH_LONG).show()

                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })


        newsAdapter.setOnItemClickListener {
            val bundle  = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()

        binding.rvSearchNews.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }

    val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPostion = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadindAndNotLastPag = !isLoading &&!isLastPage
            val isAtLastItem = firstVisibleItemPostion+visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPostion >=0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadindAndNotLastPag && isAtLastItem &&isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getSearchNews(binding.etSearch.text.toString())
                isScrolling = false
            }




        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling =true

            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

