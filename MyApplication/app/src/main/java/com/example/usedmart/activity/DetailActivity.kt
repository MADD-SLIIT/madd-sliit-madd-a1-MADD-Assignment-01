package com.example.usedmart.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usedmart.Adapter.ColorAdapter
import com.example.usedmart.Adapter.SizeAdapter
import com.example.usedmart.Adapter.SliderAdapter
import com.example.usedmart.Helper.ManagementCart
import com.example.usedmart.Model.ItemsModel
import com.example.usedmart.Model.SliderModel
import com.example.usedmart.databinding.ActivityDetailBinding
import java.util.ArrayList

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private var numberOrder = 1
    private lateinit var managementCart: ManagementCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagementCart(this)

        getBundle()
        banners()
        initLists()

    }

    private fun initLists() {
        val sizeList = ArrayList<String>()
        for (size in item.size) {
            sizeList.add(size)
        }

        binding.sizeList.adapter = SizeAdapter(sizeList)
        binding.sizeList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl) {
            colorList.add(imageUrl)
        }

        binding.colorList.adapter = ColorAdapter(colorList)
        binding.colorList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun banners() {
        val sliderItems = ArrayList<SliderModel>()
        for (imageUrl in item.picUrl) {
            sliderItems.add(SliderModel(imageUrl))
        }

        binding.slider.adapter = SliderAdapter(sliderItems, binding.slider)
        binding.slider.clipToPadding = true
        binding.slider.clipChildren = true
        binding.slider.offscreenPageLimit = 1


        if (sliderItems.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.slider)
        }
    }

    private fun getBundle() {
        item = intent.getParcelableExtra("object")!!

        binding.titleTxt.text = item.title
        binding.descriptionTxt.text = item.description
        binding.priceTxt.text = "Rs" + item.price
        binding.ratingTxt.text = "${item.rating} Rating"

        binding.addToCartButton.setOnClickListener {
            item.numberInCart = numberOrder
            managementCart.insertFood(item)
        }


        binding.backBtn.setOnClickListener {
            finish()

        }
        binding.cartBtn.setOnClickListener() {
            startActivity(Intent(this@DetailActivity, CartActivity::class.java))

        }
    }

}