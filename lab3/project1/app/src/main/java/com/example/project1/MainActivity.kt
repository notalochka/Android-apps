package com.example.project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), InputFragment.OnDataSubmittedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, InputFragment())
                .commit()
        }
    }

    override fun onDataSubmitted(result: String) {
        val outputFragment = OutputFragment().apply {
            arguments = Bundle().apply {
                putString("result", result)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, outputFragment)
            .addToBackStack(null)
            .commit()
    }
}