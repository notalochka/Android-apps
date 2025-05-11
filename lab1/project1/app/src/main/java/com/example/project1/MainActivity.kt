package com.example.lab1

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var spinnerDishes: Spinner
    private lateinit var checkBoxBrand1: CheckBox
    private lateinit var checkBoxBrand2: CheckBox
    private lateinit var checkBoxBrand3: CheckBox
    private lateinit var radioGroupPrice: RadioGroup
    private lateinit var resultTextView: TextView
    private lateinit var okButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerDishes = findViewById(R.id.spinnerDishes)
        checkBoxBrand1 = findViewById(R.id.checkBoxBrand1)
        checkBoxBrand2 = findViewById(R.id.checkBoxBrand2)
        checkBoxBrand3 = findViewById(R.id.checkBoxBrand3)
        radioGroupPrice = findViewById(R.id.radioGroupPrice)
        resultTextView = findViewById(R.id.resultTextView)
        okButton = findViewById(R.id.okButton)

        val dishes = arrayOf("Оберіть посуд", "Каструлі", "Сковороди", "Тарелі", "Чашки", "Келихи")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dishes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDishes.adapter = adapter

        okButton.setOnClickListener {
            showResult()
        }
    }

    private fun showResult() {
        if (spinnerDishes.selectedItemPosition == 0) {
            showAlertDialog("Будь ласка, оберіть тип посуду")
            return
        }

        if (!checkBoxBrand1.isChecked && !checkBoxBrand2.isChecked && !checkBoxBrand3.isChecked) {
            showAlertDialog("Будь ласка, оберіть хоча б одного виробника")
            return
        }

        val selectedRadioButtonId = radioGroupPrice.checkedRadioButtonId
        if (selectedRadioButtonId == -1) {
            showAlertDialog("Будь ласка, оберіть діапазон цін")
            return
        }

        val result = StringBuilder()
        result.append("Обрано посуд: ${spinnerDishes.selectedItem}\n\n")

        result.append("Виробники:\n")
        if (checkBoxBrand1.isChecked) result.append("- ${checkBoxBrand1.text}\n")
        if (checkBoxBrand2.isChecked) result.append("- ${checkBoxBrand2.text}\n")
        if (checkBoxBrand3.isChecked) result.append("- ${checkBoxBrand3.text}\n")
        result.append("\n")

        val selectedPriceRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        result.append("Діапазон цін: ${selectedPriceRadioButton.text}")

        resultTextView.text = result.toString()
    }

    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Увага!")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}