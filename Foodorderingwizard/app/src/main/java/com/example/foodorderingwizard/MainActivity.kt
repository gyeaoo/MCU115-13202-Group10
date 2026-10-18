package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private val vm: OrderViewModel by viewModels()
    private lateinit var pager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        pager = findViewById(R.id.pager)
        pager.adapter = WizardPagerAdapter(this)
        pager.isUserInputEnabled = false

        findViewById<Button>(R.id.btnMain).setOnClickListener { openPage(0) }
        findViewById<Button>(R.id.btnSide).setOnClickListener { openPage(1) }
        findViewById<Button>(R.id.btnDrink).setOnClickListener { openPage(2) }
        findViewById<Button>(R.id.btnOrder).setOnClickListener { openPage(3) }

        val tvSelection = findViewById<TextView>(R.id.tvSelection)
        vm.order.observe(this) { o ->
            tvSelection.text = "Current Selection:\n" +
                    "Main: ${o.mainMeal ?: "--"}\n" +
                    "Sides: ${if (o.sideDishes.isEmpty()) "--" else o.sideDishes.joinToString(", ")}\n" +
                    "Drink: ${o.drink ?: "--"}"
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (pager.visibility == View.VISIBLE) {
                    showHome()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun openPage(index: Int) {
        pager.setCurrentItem(index, false)
        pager.visibility = View.VISIBLE
    }

    // 給 Fragment 呼叫：隱藏 ViewPager2，回到主畫面
    fun showHome() {
        pager.visibility = View.GONE
    }
}