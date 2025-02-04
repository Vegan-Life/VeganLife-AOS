package com.project.veganlife.lifecheck.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentLifeCheckDietDetailBinding

class LifeCheckDietDetailFragment : Fragment() {

    private var _binding: FragmentLifeCheckDietDetailBinding? = null
    private val binding get() = _binding!!

    private val args: LifeCheckDietDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckDietDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealLogId = args.mealLogId
        Log.d("MealLogID", "$mealLogId")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}