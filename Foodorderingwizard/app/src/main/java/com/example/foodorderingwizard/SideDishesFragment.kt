package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class SideDishesFragment : Fragment(R.layout.fragment_side_dishes) {

    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val boxes = listOf(R.id.cbFries, R.id.cbSalad, R.id.cbCorn)
            .map { view.findViewById<CheckBox>(it) }

        view.findViewById<Button>(R.id.btnDone).setOnClickListener {
            vm.setSideDishes(boxes.filter { it.isChecked }.map { it.text.toString() })
            (requireActivity() as MainActivity).showHome()
        }
    }
}