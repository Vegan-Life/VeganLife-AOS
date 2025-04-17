package com.project.veganlife.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialFadeThrough
import com.project.veganlife.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupExitTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startSplashDelay()
    }

    private fun setupExitTransition() {
        exitTransition = MaterialFadeThrough().apply {
            duration = 1500L
        }
    }

    private fun startSplashDelay() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1500L)
            navigateToNextScreen()
        }
    }

    private fun navigateToNextScreen() {
        parentFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .remove(this@SplashFragment)
            .commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
