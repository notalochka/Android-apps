package com.example.project1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import java.io.File
import android.content.Intent



class OutputFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_output, container, false)

        val resultTextView = view.findViewById<TextView>(R.id.resultTextView)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val openButton = view.findViewById<Button>(R.id.openButton)

        val result = arguments?.getString("result") ?: "Немає даних"
        resultTextView.text = result

        cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        val fileName = "results.txt"
        val file = File(requireContext().filesDir, fileName)

        try {
            file.appendText(result + "\n\n")
            Toast.makeText(requireContext(), "Результат збережено у файл", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Помилка запису: ${e.message}", Toast.LENGTH_LONG).show()
        }

        openButton.setOnClickListener {
            val intent = Intent(requireContext(), StorageActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}