package com.exame.intento212

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.exame.intento212.databinding.ActivityGrtStartedBinding

class GrtStartedActivity : AppCompatActivity() {

    private var binding: ActivityGrtStartedBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrtStartedBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.cvGetStarted?.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }
}