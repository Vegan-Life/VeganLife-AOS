package com.project.veganlife.mypage.ui.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.databinding.FragmentMypageModifyFragmentBinding
import com.project.veganlife.data.model.ProfileRequestDTO
import com.project.veganlife.mypage.ui.viewmodel.MypageViewmodel
import com.project.veganlife.utils.PhotoUtils
import com.project.veganlife.utils.PhotoUtils.Companion.createImageMultipart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MypageModifyFragment : Fragment() {
    private var _binding: FragmentMypageModifyFragmentBinding? = null
    private val binding get() = _binding!!

    private val mypageViewmodel: MypageViewmodel by activityViewModels()

    private val PICK_IMAGE_REQUEST = 1

    val requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageModifyFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // toolbar
        setToolbarListener()

        replaceProfilePhoto()

        // ui
        setUserInfoUi()

        selectVeganType()

        modifyUserInfo()
    }

    private fun setToolbarListener() {
        binding.toolbarMypageToolbar.run {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setUserInfoUi() {
        mypageViewmodel.apply {
            profileInfoResponse.observe(viewLifecycleOwner) { profile ->
                updateUIWithProfile(profile)
            }
        }
    }

    private fun replaceProfilePhoto() {
        binding.apply {
            binding.ivMypagePhoto.setOnClickListener {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {

                if (PhotoUtils.getFileExtension(imageUri, requireContext())
                        .equals("webp", ignoreCase = true)
                ) {
                    makeToast("지원하지 않는 이미지 형식입니다.")
                    return
                }

                Glide.with(this)
                    .load(imageUri)
                    .apply(requestOptions)
                    .into(binding.ivMypageProfile)

                lifecycleScope.launch {
                    if (context != null) {
                        val imageMultipart = withContext(Dispatchers.IO) {
                            // 1. 최적화된 비트맵을 임시 파일로 저장
                            val imagePath = PhotoUtils.optimizeBitmap(requireContext(), imageUri)
                            // 2. 임시 파일 경로를 사용해 MultipartBody.Part로 변환
                            createImageMultipart(imagePath)
                        }

                        if (imageMultipart != null) {
                            mypageViewmodel.putProfilePhotoMultipart(imageMultipart)
                        }
                    }
                }
            }
        }
    }

    private fun setVeganTypeUi(Type: String) {
        binding.apply {
            when (Type) {
                "VEGAN" -> {
                    veganType.apply {
                        ivMypageImage.setImageResource(R.drawable.all_vegan_type_vegan)
                        tvMypageVeganType.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.base3
                            )
                        )
                        tvMypageVeganType.isSelected = true
                    }
                    lactoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_lacto_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    ovoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_ovo_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    lactoOvoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_lacto_ovo_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    pescoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_pesco_gray)
                        tvMypageVeganType.isSelected = false
                    }
                }

                "LACTO" -> {
                    veganType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_vegan_gray)
                        tvMypageVeganType.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray2
                            )
                        )
                        tvMypageVeganType.isSelected = false
                    }
                    lactoType.apply {
                        ivMypageImage.setImageResource(R.drawable.all_vegan_type_lacto)
                        tvMypageVeganType.isSelected = true
                    }
                    ovoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_ovo_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    lactoOvoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_lacto_ovo_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    pescoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_pesco_gray)
                        tvMypageVeganType.isSelected = false
                    }
                }

                "OVO" -> {
                    veganType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_vegan_gray)
                        tvMypageVeganType.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray2
                            )
                        )
                        tvMypageVeganType.isSelected = false
                    }
                    lactoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_lacto_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    ovoType.apply {
                        ivMypageImage.setImageResource(R.drawable.all_vegan_type_ovo)
                        tvMypageVeganType.isSelected = true
                    }
                    lactoOvoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_lacto_ovo_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    pescoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_pesco_gray)
                        tvMypageVeganType.isSelected = false
                    }
                }

                "LACTO_OVO" -> {
                    veganType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_vegan_gray)
                        tvMypageVeganType.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray2
                            )
                        )
                        tvMypageVeganType.isSelected = false
                    }
                    lactoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_lacto_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    ovoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_ovo_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    lactoOvoType.apply {
                        ivMypageImage.setImageResource(R.drawable.all_vegan_type_lacto_ovo)
                        tvMypageVeganType.isSelected = true
                    }
                    pescoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_pesco_gray)
                        tvMypageVeganType.isSelected = false
                    }
                }

                "PESCO" -> {
                    veganType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_vegan_gray)
                        tvMypageVeganType.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray2
                            )
                        )
                        tvMypageVeganType.isSelected = false
                    }
                    lactoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_lacto_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    ovoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_ovo_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    lactoOvoType.apply {
                        ivMypageImage.setImageResource(R.drawable.mypage_vegan_type_lacto_ovo_gray)
                        tvMypageVeganType.isSelected = false
                    }
                    pescoType.apply {
                        ivMypageImage.setImageResource(R.drawable.all_vegan_type_pesco)
                        tvMypageVeganType.isSelected = true
                    }
                }
            }
        }
    }

    private fun setGenderUi(gender: String) {
        when (gender) {
            "M" -> {
                binding.apply {
                    btnMypageMale.setBackgroundResource(R.color.base3)
                    btnMypageMale.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    btnMypageFemale.setBackgroundResource(R.color.gray1)
                    btnMypageFemale.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.sub_gray2
                        )
                    )
                }
            }

            "F" -> {
                binding.apply {
                    btnMypageFemale.setBackgroundResource(R.color.base3)
                    btnMypageFemale.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    btnMypageMale.setBackgroundResource(R.color.gray1)
                    btnMypageMale.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.sub_gray2
                        )
                    )
                }
            }
        }
    }

    private fun selectVeganType() {
        mypageViewmodel.apply {

            binding.apply {
                veganType.clVeganTypeLayout.setOnClickListener {
                    setVeganTypeUi("VEGAN")
                }

                lactoType.clLactoLayout.setOnClickListener {
                    setVeganTypeUi("LACTO")
                }

                ovoType.clOvoLayout.setOnClickListener {
                    setVeganTypeUi("OVO")
                }

                lactoOvoType.clLactoOvoLayout.setOnClickListener {
                    setVeganTypeUi("LACTO_OVO")
                }

                pescoType.clPescoLayout.setOnClickListener {
                    setVeganTypeUi("PESCO")
                }
            }
        }
    }

    private fun modifyUserInfo() {
        mypageViewmodel.apply {
            binding.apply {
                btnMypageModify.setOnClickListener {
                    val profileDTO = ProfileRequestDTO(
                        nickname = tietMypageNickname.text.toString(),
                        vegetarianType = profileInfoResponse.value!!.vegetarianType,
                        gender = profileInfoResponse.value!!.gender,
                        birthYear = tietMypageAge.text.toString().toInt(),
                        height = tietMypageHeight.text.toString().toInt(),
                        weight = tietMypageWeight.text.toString().toInt()
                    )

                    lifecycleScope.launch {

                        if (isUserInfoStateCheck(profileDTO)) {
                            val profileRequestBody = withContext(Dispatchers.IO) {
                                PhotoUtils.createProfileRequestBody(profileDTO)
                            }
                            putProfileRequestBody(profileRequestBody)
                            getProfileModifyInfo()
                        } else {
                            makeToast("모든 정보를 올바르게 입력해주세요")
                        }

                        responseCode.observe(viewLifecycleOwner) { response ->
                            if (response != null) handleSignupResponse(response)
                        }
                    }
                }
            }
        }
    }

    private fun updateUIWithProfile(profile: ProfileResponse) {
        binding.apply {
            Glide.with(requireContext())
                .load(profile.imageUrl)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(ivMypageProfile)

            tietMypageNickname.setText(profile.nickname)
            tietMypageId.setText(profile.email)
            tietMypageHeight.setText(profile.height.toString())
            tietMypageWeight.setText(profile.weight.toString())
            tietMypageAge.setText(profile.birthYear.toString())

            setVeganTypeUi(profile.vegetarianType)
            setGenderUi(profile.gender)
        }
    }

    private fun handleSignupResponse(responseCode: String) {
        when (responseCode) {
            "200" -> {
                findNavController().navigate(R.id.action_mypageModifyFragment_to_mypageHomeFragment)
                makeToast("정보가 수정되었습니다.")
            }

            "409" -> {
                makeToast("중복된 닉네임입니다.")
            }
        }
    }

    private fun makeToast(message: String) {
        if (message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}