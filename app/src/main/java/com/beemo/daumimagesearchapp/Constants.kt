package com.beemo.daumimagesearchapp

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

const val emptyTxt = "검색결과가 없습니다."
const val errorTxt = "검색에 실패하였습니다. 아래로 당겨서 새로고침 해보세요."
object CommonBindingAdapters{
    @BindingAdapter("app:imageUrl")
    @JvmStatic fun loadImage(imageView: ImageView, url: String){
        Glide.with(imageView.context)
            .load(url)
            .error(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(RequestOptions().fitCenter())
            .into(imageView)
    }
}