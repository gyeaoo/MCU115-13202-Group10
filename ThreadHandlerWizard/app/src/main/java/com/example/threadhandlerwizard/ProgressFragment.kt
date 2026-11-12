package com.example.threadhandlerwizard

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ProgressFragment : Fragment(R.layout.frag_progress) {

    private val vm: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val tvPercent = view.findViewById<TextView>(R.id.tvPercent)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)

        vm.progress.observe(viewLifecycleOwner) { p ->
            progressBar.progress = p
            tvPercent.text = "$p%"
        }

        vm.progressText.observe(viewLifecycleOwner) { text ->
            tvStatus.text = text
        }
    }
}