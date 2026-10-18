package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class MainMealFragment : Fragment(R.layout.fragment_main_meal) {

    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val group = view.findViewById<RadioGroup>(R.id.rgMain)

        view.findViewById<Button>(R.id.btnDone).setOnClickListener {
            val id = group.checkedRadioButtonId
            if (id != -1) {
                vm.setMainMeal(view.findViewById<RadioButton>(id).text.toString())
            }
            (requireActivity() as MainActivity).showHome()
        }
    }
}