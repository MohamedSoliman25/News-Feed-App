package com.example.newsfeedapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.newsfeedapp.R
import com.example.newsfeedapp.databinding.ItemArticleBinding
import com.example.newsfeedapp.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private var binding:ItemArticleBinding ?=null
//    private var articleList:List<Article> = ArrayList<Article>()
//    fun setList(articleList:List<Article>){
//        this.articleList = articleList
//    }
inner class ArticleViewHolder(itemBinding: ItemArticleBinding):RecyclerView.ViewHolder(itemBinding.root)
    private val differCallback = object:DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url ==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        binding = ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false
        )
        return ArticleViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
//        val article  = articleList.get(position)
        holder.itemView.apply {
//            Glide.with(this).load(article.urlToImage).into(binding?.ivArticle!!)
            //here i'm using coroutine image loader (coil) to display image instead of glide
            binding?.ivArticle!!.load(article.urlToImage){
                crossfade(true)
                crossfade(1000)
            }
            binding?.tvSource?.text = article.source?.name
            binding?.tvArticleTitle?.text = article.title
            binding?.tvPublished?.text = article.publishedAt
            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener:((Article)->Unit)? = null

    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener = listener
    }



}