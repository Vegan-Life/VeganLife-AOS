package com.project.veganlife.community.ui.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.veganlife.databinding.FragmentCommunityHomeBinding
import com.project.veganlife.utils.ui.DisplayUtils

class CommunityHomeFragment : Fragment() {

    private var fabOpen = false
    private var _binding: FragmentCommunityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.efabCommunityhomeWrite.setOnClickListener {

            //피드 - 레시피  -> 4dp
            //레시피 - 글쓰기 -> 12dp
            if(!fabOpen) {
                ObjectAnimator.ofFloat(binding.efabCommunityhomeWriteRecipe, "translationY", -1*DisplayUtils.dpToPx(requireContext(), 52f)).apply { start() }
                ObjectAnimator.ofFloat(binding.efabCommunityhomeWriteFeed, "translationY", -1*DisplayUtils.dpToPx(requireContext(), 96f)).apply { start() }
            } else {
                ObjectAnimator.ofFloat(binding.efabCommunityhomeWriteRecipe, "translationY", 0f).apply { start() }
                ObjectAnimator.ofFloat(binding.efabCommunityhomeWriteFeed, "translationY", 0f).apply { start() }
            }

            fabOpen = !fabOpen

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
