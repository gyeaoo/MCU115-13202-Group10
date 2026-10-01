package com.example.mycalculator

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.math.BigDecimal
import android.widget.Button
import java.math.RoundingMode
import android.widget.ImageButton

class MainActivity : ComponentActivity() {
    // Define variables for UI elements

    // Define variables for UI elements
    lateinit var tvInput: TextView // Explicitly type tvInput
    lateinit var btnOne: Button
    lateinit var btnTwo: Button
    lateinit var btnThree: Button
    lateinit var btnFour: Button
    lateinit var btnFive: Button
    lateinit var btnSix: Button
    lateinit var btnSeven: Button
    lateinit var btnEight: Button
    lateinit var btnNine: Button
    lateinit var btnZero: Button
    lateinit var btnDot: Button
    lateinit var btnPLus: Button
    lateinit var btnMinus: Button
    lateinit var btnMultiply: Button
    lateinit var btnDivide: Button
    lateinit var btnEqual: Button
    lateinit var clear: Button
    lateinit var allClear: Button
    lateinit var btnBackspace: ImageButton


    lateinit var tvOldInput: TextView
    lateinit var tvCurrentOperand: TextView


// Add similar declarations for other buttons

    // Initialize currentInput as a StringBuilder
    var currentInput = StringBuilder()
    var currentOperator = Operator.NONE
    var operand1: BigDecimal? = null
    var isResultShown = false

    enum class Operator {
        NONE, ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize views after setContentView
        tvInput = findViewById(R.id.tvInput)
        btnOne = findViewById(R.id.btnOne)
        btnTwo = findViewById(R.id.btnTwo)
        btnThree = findViewById(R.id.btnThree)
        btnFour = findViewById(R.id.btnFour)
        btnFive = findViewById(R.id.btnFive)
        btnSix = findViewById(R.id.btnSix)
        btnSeven = findViewById(R.id.btnSeven)
        btnEight = findViewById(R.id.btnEight)
        btnNine = findViewById(R.id.btnNine)
        btnZero = findViewById(R.id.btnZero)
        btnDot = findViewById(R.id.btnDot)
        btnPLus = findViewById(R.id.btnPLus)
        btnMinus = findViewById(R.id.btnMinus)
        btnMultiply = findViewById(R.id.btnMultiply)
        btnDivide = findViewById(R.id.btnDivide)
        btnEqual = findViewById(R.id.btnEqual)
        clear = findViewById(R.id.clear)
        allClear = findViewById(R.id.allClear)
        btnBackspace = findViewById(R.id.btnBackspace)
        tvOldInput = findViewById(R.id.tvOldInput)
        tvCurrentOperand = findViewById(R.id.tvCurrentOperand)

        // Set click listeners for number buttons
        btnOne.setOnClickListener { appendNumber("1") }
        btnTwo.setOnClickListener { appendNumber("2") }
        btnThree.setOnClickListener { appendNumber("3") }
        btnFour.setOnClickListener { appendNumber("4") }
        btnFive.setOnClickListener { appendNumber("5") }
        btnSix.setOnClickListener { appendNumber("6") }
        btnSeven.setOnClickListener { appendNumber("7") }
        btnEight.setOnClickListener { appendNumber("8") }
        btnNine.setOnClickListener { appendNumber("9") }
        btnZero.setOnClickListener { appendNumber("0") }
        btnDot.setOnClickListener {
            if (currentInput.isEmpty()) appendNumber("0")        // 開頭補 0 → 0.
            if (!currentInput.contains(".")) appendNumber(".")   // 已經有小數點就不再加
        }
        // Set click listeners for operator buttons
        btnPLus.setOnClickListener { setOperator(Operator.ADD) }
        btnMinus.setOnClickListener { setOperator(Operator.SUBTRACT) }
        btnMultiply.setOnClickListener { setOperator(Operator.MULTIPLY) }
        btnDivide.setOnClickListener { setOperator(Operator.DIVIDE) }

        // Handle equals button click
        btnEqual.setOnClickListener { calculateResult() }

        // Handle clear button click
        clear.setOnClickListener { clearInput() }
        allClear.setOnClickListener { allClearInput() }

        //backSpace button
        btnBackspace.setOnClickListener { handleBackspace() }
    }
    private fun appendNumber(number: String) {
        if (isResultShown) {              // 剛算完又開始輸入 → 當作全新的計算
            operand1 = null
            currentOperator = Operator.NONE
            tvOldInput.text = ""
            tvCurrentOperand.text = ""
            isResultShown = false
        }
        currentInput.append(number)
        updateDisplay()
    }
    private fun handleBackspace() {
        if (currentInput.isNotEmpty()) {
            currentInput.deleteCharAt(currentInput.length - 1)
            updateDisplay()
        }
    }

    private fun setOperator(operator: Operator) {
        // 已有第一個數，又輸入了第二個數 → 先算出來（連續運算 5+3+）
        if (operand1 != null && currentInput.isNotEmpty()) calculateResult()

        if (operand1 == null) {
            operand1 = currentInput.toString().toBigDecimalOrNull() ?: return
            currentInput.clear()
        }
        isResultShown = false
        tvOldInput.text = operand1?.let { format(it) } ?: ""
        tvInput.text = ""
        currentOperator = operator
        tvCurrentOperand.text = operatorToString(operator)
    }
    private fun operatorToString(operator: Operator): String {
        return when (operator) {
            Operator.ADD -> "+"
            Operator.SUBTRACT -> "-"
            Operator.MULTIPLY -> "×"
            Operator.DIVIDE -> "÷"
            Operator.NONE -> ""
        }
    }
    private fun calculateResult() {
        val operand2 = currentInput.toString().toBigDecimalOrNull() ?: return
        if (operand1 == null || currentOperator == Operator.NONE) return
        var result: BigDecimal?
        Log.d("CalculatorApp", "operand1= $operand1")
        Log.d("CalculatorApp", "operand2= $operand2")
        Log.d("CalculatorApp", "currentInput= $currentInput")

        when (currentOperator) {
            Operator.ADD -> result = operand1?.add(operand2)
            Operator.SUBTRACT -> result = operand1?.subtract(operand2)
            Operator.MULTIPLY -> result = operand1?.multiply(operand2)
            Operator.DIVIDE -> {
                if (operand2 != BigDecimal.ZERO) {
                    //result = operand1?.divide(operand2, 10, BigDecimal.ROUND_HALF_UP)
                    result = operand1?.divide(operand2, 10, RoundingMode.HALF_UP)
                } else {
                    Log.e("CalculatorApp", "Division by zero attempted.")
                    tvInput.text = "Error: Div by 0" // Or some other user-friendly message
                    // Reset state appropriately after division by zero
                    currentInput.clear()
                    operand1 = null
                    currentOperator = Operator.NONE
                    tvOldInput.text = ""
                    tvCurrentOperand.text = ""
                    return // Exit early
                }
            }
            Operator.NONE -> result = operand2
        }

        // Display the result and reset the state
        if (result != null) {
            tvOldInput.text = "${operand1?.let { format(it) }}${operatorToString(currentOperator)}${format(operand2)}"
            tvInput.text = format(result)
            operand1 = result
            currentInput.clear()       // 算完把輸入清空
            isResultShown = true       // 記錄「剛算完」
        }
//        Log.d("CalculatorApp", "Result: ${result?.toString()}")

    }
    private fun allClearInput() {
        currentInput.clear()
        operand1 = null
        currentOperator = Operator.NONE
        tvOldInput.text = ""
        tvInput.text = "0"
        tvCurrentOperand.text = ""
    }
    private fun clearInput() {
        currentInput.clear()
        currentOperator = Operator.NONE
        tvInput.text = "0"
        operand1 = null
    }

    private fun updateDisplay() {
        tvInput.text = currentInput.toString()
    }
    private fun format(value: BigDecimal): String {
        return value.stripTrailingZeros().toPlainString()
    }
}
