package com.example.project1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class InputFragment : Fragment() {
    private lateinit var spinnerDishes: Spinner
    private lateinit var checkBoxBrand1: CheckBox
    private lateinit var checkBoxBrand2: CheckBox
    private lateinit var checkBoxBrand3: CheckBox
    private lateinit var radioGroupPrice: RadioGroup
    private lateinit var okButton: Button

    var listener: OnDataSubmittedListener? = null

    interface OnDataSubmittedListener {
        fun onDataSubmitted(result: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_input, container, false)

        spinnerDishes = view.findViewById(R.id.spinnerDishes)
        checkBoxBrand1 = view.findViewById(R.id.checkBoxBrand1)
        checkBoxBrand2 = view.findViewById(R.id.checkBoxBrand2)
        checkBoxBrand3 = view.findViewById(R.id.checkBoxBrand3)
        radioGroupPrice = view.findViewById(R.id.radioGroupPrice)
        okButton = view.findViewById(R.id.okButton)

        val dishes = arrayOf("Оберіть посуд", "Каструлі", "Сковороди", "Тарелі", "Чашки", "Келихи")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dishes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDishes.adapter = adapter

        okButton.setOnClickListener {
            if (validateInput()) {
                val result = buildResultString()
                listener?.onDataSubmitted(result)
            }
        }

        return view
    }

    private fun validateInput(): Boolean {
        if (spinnerDishes.selectedItemPosition == 0) {
            Toast.makeText(requireContext(), "Оберіть тип посуду", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!checkBoxBrand1.isChecked && !checkBoxBrand2.isChecked && !checkBoxBrand3.isChecked) {
            Toast.makeText(requireContext(), "Оберіть хоча б одного виробника", Toast.LENGTH_SHORT).show()
            return false
        }

        if (radioGroupPrice.checkedRadioButtonId == -1) {
            Toast.makeText(requireContext(), "Оберіть діапазон цін", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun buildResultString(): String {
        val result = StringBuilder()
        result.append("Обрано посуд: ${spinnerDishes.selectedItem}\n\n")

        result.append("Виробники:\n")
        if (checkBoxBrand1.isChecked) result.append("- ${checkBoxBrand1.text}\n")
        if (checkBoxBrand2.isChecked) result.append("- ${checkBoxBrand2.text}\n")
        if (checkBoxBrand3.isChecked) result.append("- ${checkBoxBrand3.text}\n")
        result.append("\n")

        val selectedPriceRadioButton = view?.findViewById<RadioButton>(radioGroupPrice.checkedRadioButtonId)
        result.append("Діапазон цін: ${selectedPriceRadioButton?.text}")

        return result.toString()
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnDataSubmittedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnDataSubmittedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}