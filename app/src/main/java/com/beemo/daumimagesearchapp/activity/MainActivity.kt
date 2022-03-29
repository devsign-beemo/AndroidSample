package com.beemo.daumimagesearchapp.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.beemo.daumimagesearchapp.R
import com.beemo.daumimagesearchapp.databinding.ActivityMainBinding
import com.beemo.daumimagesearchapp.databinding.InflateImageItemBinding
import com.beemo.daumimagesearchapp.emptyTxt
import com.beemo.daumimagesearchapp.errorTxt
import com.beemo.daumimagesearchapp.response.KakaoImage
import com.beemo.daumimagesearchapp.viewmodel.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val imageViewModel : ImageViewModel by viewModels()
    private lateinit var imageListAdapter : ImageListAdapter
    private var isEnd = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this@MainActivity
        imageListAdapter = ImageListAdapter(this)
        imageListAdapter.setHasStableIds(true)
        binding.imageRecyclerView.adapter = imageListAdapter
        binding.imageRecyclerView.addOnScrollListener(onScrollListener)
        binding.imageRecyclerView.itemAnimator
        imageViewModel.imageResult.observe(this, Observer { result ->

            if(result.isSuccessful){
                if (result.body()!!.metaData!!.totalCount == 0) {
                    binding.emptyText.visibility = View.VISIBLE
                    binding.emptyText.text = emptyTxt
                } else {
                    binding.emptyText.visibility = View.GONE
                }
                isEnd = result.body()!!.metaData!!.isEnd!!

                imageListAdapter.setItems(result.body()!!.documents!! as ArrayList<KakaoImage>)
            }
            else{
                //실패시 예외처리
                imageListAdapter.clear()
                binding.emptyText.visibility = View.VISIBLE
                binding.emptyText.text = errorTxt

            }
        })



    }
    fun swipeRefresh() {
        //새로고침 리사이클러뷰의 어댑터를 통해 불러온 List와 원래 refreshItemList이 다른 주소를 가지고 있었음.
        if(binding.edtSearch.text.toString().isEmpty()) {
            binding.swipeRefresh.isRefreshing = false
            return
        }
        CoroutineScope(Dispatchers.Default).launch {
            imageListAdapter.clear()
            imageListAdapter.resetPage()
            val awaitJob = this.async {
                imageViewModel.searchImage(binding.edtSearch.text.toString(), "accuracy", 1)

            }
            awaitJob.await()
            binding.swipeRefresh.isRefreshing = false

        }

    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                if (!isEnd) {
                    CoroutineScope(Dispatchers.Default).launch {
                        imageViewModel.searchImage(binding.edtSearch.text.toString(), "accuracy", imageListAdapter.getPageNumber())
                    }
                }

            }
        }
    }
    private var coroutine :Job = Job()
    fun textChanged(s: CharSequence) = runBlocking{
        Log.d("test",s.toString())

        if(coroutine.isActive) {
            coroutine.cancelAndJoin() //코루틴 진행중일 때 text가 변했을 경우 취소

        }
         coroutine = CoroutineScope(Dispatchers.Default).launch {
             delay(2000)
             imageListAdapter.clear()
             imageListAdapter.resetPage()
             imageViewModel.searchImage(s.toString(), "accuracy", 1)
         }

    }
    class ImageListAdapter(private var context: Context): RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

        private var items: ArrayList<KakaoImage> = ArrayList()
        private var page = 1

        fun setItems(items : ArrayList<KakaoImage>) {

            var position = if(this.items.size == 0) {
                0
            } else {
                items.size - 1
            }
            this.items.addAll(items)
            page++
            notifyDataSetChanged()
        }
        fun clear() {
            items.clear()
        }
        fun getPageNumber(): Int {
            return page
        }
        fun resetPage() {
            page = 1
        }

        override fun getItemId(position: Int): Long {
            return items.get(position).hashCode().toLong()
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = InflateImageItemBinding.inflate(
                LayoutInflater.from(context), parent, false)
            return ViewHolder(binding)
        }
        override fun getItemCount(): Int {
            return items.size
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }
        inner class ViewHolder(val binding : InflateImageItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: KakaoImage) {
                binding.image = item
                itemView.setOnClickListener {
                    val intent = Intent(context, FullImageActivity::class.java)
                    intent.putExtra("item",item)
                    context.startActivity(intent)
                }

            }
        }
    }

}
