package com.beemo.daumimagesearchapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.beemo.daumimagesearchapp.R
import com.beemo.daumimagesearchapp.databinding.ActivityFullImageBinding
import com.beemo.daumimagesearchapp.response.KakaoImage

class FullImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_image)
        binding.activity = this@FullImageActivity
        val intent: Intent = getIntent()
        binding.image = intent.getParcelableExtra<KakaoImage>("item")

    }
}