package com.project.veganlife.lifecheck.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.veganlife.databinding.FragmentLifeCheckMenuModifyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LifeCheckMenuModifyFragment : Fragment() {

    private var _binding: FragmentLifeCheckMenuModifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckMenuModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealId = arguments?.getLong("mealId") ?: -1
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}