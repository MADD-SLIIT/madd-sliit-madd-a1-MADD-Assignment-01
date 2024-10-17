package com.example.usedmart.activity

import android.content.Intent
import android.os.Bundle
import com.example.usedmart.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.introBtn.setOnClickListener {
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
        }

        binding.signupTxt.setOnClickListener {
            startActivity(Intent(this@IntroActivity, SignupActivity::class.java))
        }

    }
}