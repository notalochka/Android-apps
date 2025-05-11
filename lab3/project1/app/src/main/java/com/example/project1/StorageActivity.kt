package com.example.project1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class StorageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        val textView = findViewById<TextView>(R.id.storageTextView)
        val file = File(filesDir, "results.txt")

        val content = if (file.exists() && file.readText().isNotBlank()) {
            file.readText()
        } else {
            "Сховище порожнє."
        }

        textView.text = content
    }
}
