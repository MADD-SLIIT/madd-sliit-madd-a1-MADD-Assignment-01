package com.example.usedmart.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.usedmart.Model.SliderModel
import com.example.usedmart.SliderAdapter
import com.example.usedmart.ViewModel.MainViewModel
import com.example.usedmart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()
    }

    private fun initBanner() {
        binding.progressBarAd.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer { items ->
            banners(items)
            binding.progressBarAd.visibility=View.GONE
        })
        viewModel.loadBanners()
    }

    private fun banners(images: List<SliderModel>) {
        binding.viewPageSlider.adapter = SliderAdapter(images, binding.viewPageSlider)
        binding.viewPageSlider.clipToPadding = false
        binding.viewPageSlider.clipChildren = false
        binding.viewPageSlider.offscreenPageLimit = 3
        binding.viewPageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPageSlider.setPageTransformer(compositePageTransformer)
        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewPageSlider)
        }
    }
}