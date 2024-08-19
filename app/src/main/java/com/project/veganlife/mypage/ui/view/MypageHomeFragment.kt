package com.project.veganlife.mypage.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentMypageHomeBinding
import com.project.veganlife.mypage.ui.viewmodel.MypageViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageHomeFragment : Fragment() {
    private var _binding: FragmentMypageHomeBinding? = null
    private val binding get() = _binding!!
    private val mypageViewmodel: MypageViewmodel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageHomeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moveToFragment()

        // get
        getUserInfo()

        // setting ui
        setUserInfoUi()

        // logout
        setUserLogout()

        // membership withdrawal
        setUserWithdrawal()
    }

    private fun moveToFragment() {
        binding.apply {
            btnMypageModifyInfo.setOnClickListener {
                findNavController().navigate(R.id.action_mypageHomeFragment_to_mypageModifyFragment)
            }

            btnMypageWroteFeed.setOnClickListener {
                findNavController().navigate(R.id.action_mypageHomeFragment_to_mypagePostedFeedFragment)
            }

            btnMypageWroteComments.setOnClickListener {
                findNavController().navigate(R.id.action_mypageHomeFragment_to_mypagePostedCommentsFragment)
            }

            btnMypagePickedRecipe.setOnClickListener {
                findNavController().navigate(R.id.action_mypageHomeFragment_to_mypageScrapsRecipeFragment)
            }
        }
    }
    private fun setUserLogout() {
        binding.apply {
            btnMypageLogout.setOnClickListener {
                val dialog = MypageCustomDialogFragment("로그아웃")
                dialog.show(parentFragmentManager,"logoutDialog")
            }
        }
    }

    private fun setUserWithdrawal() {
        binding.apply {
            btnMypageWithdrawalMembership.setOnClickListener {
                val dialog = MypageCustomDialogFragment("회원탈퇴")
                dialog.show(parentFragmentManager,"withdrawalDialog")
            }
        }
    }

    private fun getUserInfo() {
        mypageViewmodel.getUserInfo()
    }

    private fun setUserInfoUi() {
        mypageViewmodel.apply {
            binding.apply {
                profileInfoResponse.observe(viewLifecycleOwner) { profile ->
                    tvMypageNickname.text = profile.nickname
                    tvMypageEmail.text = profile.email
                    Glide.with(requireContext()).load(profile.imageUrl).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(ivMypageProfile)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}