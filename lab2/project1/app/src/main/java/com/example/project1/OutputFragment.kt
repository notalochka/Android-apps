package com.example.project1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class OutputFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_output, container, false)

        val resultTextView = view.findViewById<TextView>(R.id.resultTextView)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)

        val result = arguments?.getString("result") ?: "Немає даних"
        resultTextView.text = result

        cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}