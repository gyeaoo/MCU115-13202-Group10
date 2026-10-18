package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ConfirmFragment : Fragment(R.layout.fragment_confirm) {

    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tvSummary = view.findViewById<TextView>(R.id.tvSummary)

        // 訂單一變動就更新摘要
        vm.order.observe(viewLifecycleOwner) { o ->
            tvSummary.text = "Main: ${o.mainMeal ?: "--"}\n" +
                    "Sides: ${if (o.sideDishes.isEmpty()) "--" else o.sideDishes.joinToString(", ")}\n" +
                    "Drink: ${o.drink ?: "--"}"
        }

        view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            val order = vm.order.value

            if (order == null || !order.isComplete()) {
                Toast.makeText(
                    requireContext(),
                    "Please select a main meal, at least one side dish, and a drink.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Submit Order")
                .setMessage(
                    "Main: ${order.mainMeal}\n" +
                            "Sides: ${order.sideDishes.joinToString(", ")}\n" +
                            "Drink: ${order.drink}\n\nSubmit this order?"
                )
                .setPositiveButton("Submit") { d, _ ->
                    d.dismiss()
                    Toast.makeText(requireContext(), "Order submitted!", Toast.LENGTH_SHORT).show()
                    // 提交後回主畫面，主畫面會顯示所選餐點
                    (requireActivity() as MainActivity).showHome()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}