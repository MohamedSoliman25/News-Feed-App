package com.example.newsfeedapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeedapp.MainActivity
import com.example.newsfeedapp.R
import com.example.newsfeedapp.adapters.NewsAdapter
import com.example.newsfeedapp.databinding.FragmentSavedNewsBinding
import com.example.newsfeedapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    private val TAG = "SavedNewsFragment"
    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSavedNewsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles->
            newsAdapter.differ.submitList(articles)
//            newsAdapter.setList(articles)
//            newsAdapter.notifyDataSetChanged()
        })

        newsAdapter.setOnItemClickListener {
            val bundle  = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        deleteArticle(view,binding.rvSavedNews)
    }

    private fun deleteArticle(view:View,rvSavedNews:RecyclerView) {
        val itemTouchHelperCallBack = object:ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article  = newsAdapter.differ.currentList[position]
//                val article  = newsAdapter.getList().get(position)
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Successfully deleted article ",Snackbar.LENGTH_LONG).apply {
//                    setAction("Undo"){
//                        viewModel.insertArticle(article)
//                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(rvSavedNews)
        }
    }


    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()

        binding.rvSavedNews.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}



