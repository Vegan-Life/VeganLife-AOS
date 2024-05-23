package com.project.veganlife.mypage.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentMypageHomeBinding
import com.project.veganlife.mypage.ui.viewmodel.MypageViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageHomeFragment : Fragment() {
    private var _binding: FragmentMypageHomeBinding? = null
    private val binding get() = _binding!!
    private val mypageViewmodel: MypageViewmodel by viewModels()
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
        setUserInfo()

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
                findNavController().navigate(R.id.action_mypageHomeFragment_to_mypageModifyFramgnet)
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

    private fun setUserInfo() {
        mypageViewmodel.apply {
            resultUserInfo.observe(viewLifecycleOwner) { apiResult ->
                when (apiResult) {
                    is ApiResult.Error -> {
                        val response = apiResult.description
                        Log.d("get User Info Error", response)
                    }

                    is ApiResult.Exception -> {
                        Log.d(
                            "recommended Exception",
                            apiResult.e.message ?: "No message available"
                        )
                    }

                    is ApiResult.Success -> {
                        val response = apiResult.data
                        setUserInfo(response)
                    }
                }
            }
        }
    }

    private fun setUserInfoUi() {
        mypageViewmodel.apply {
            binding.apply {
                nickname.observe(viewLifecycleOwner) { nickname ->
                    tvMypageNickname.text = nickname
                }

                email.observe(viewLifecycleOwner) { email ->
                    tvMypageEmail.text = email
                }

                photo.observe(viewLifecycleOwner) { photo ->
                    if(photo != null) {
                        Log.d("photo", photo)
                        Glide.with(requireContext()).load(photo).apply(
                            RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                        ) // 캐시 사용하지 않도록 설정
                            .into(ivMypageProfile)
                    } else {
                        ivMypageProfile.setBackgroundResource(R.drawable.all_profile_basic)
                        ivMypageProfile.borderWidth = 0
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}