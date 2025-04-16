package com.project.veganlife.mypage.ui.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.project.veganlife.R
import com.project.veganlife.data.model.ProfileRequestDTO
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.databinding.FragmentMypageModifyFragmentBinding
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

    private val mypageViewmodel: MypageViewmodel by viewModels()

    private val PICK_IMAGE_REQUEST = 1

    private val requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)

    // 기존 프로필 사진
    private var existingImageUrl: String? = null

    // 새 프로필 사진
    private var profileImageUri: Uri? = null

    private var vegetarianType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageModifyFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // toolbar
        setToolbarListener()

        replaceProfilePhoto()

        // ui
        getUserInfo()

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
                showProfileImageMenu()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {

                Glide.with(this)
                    .load(imageUri)
                    .apply(requestOptions)
                    .into(binding.ivMypageProfile)

                existingImageUrl = null
                profileImageUri = imageUri
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
                    vegetarianType = "VEGAN"
                }

                lactoType.clLactoLayout.setOnClickListener {
                    setVeganTypeUi("LACTO")
                    vegetarianType = "LACTO"
                }

                ovoType.clOvoLayout.setOnClickListener {
                    setVeganTypeUi("OVO")
                    vegetarianType = "OVO"
                }

                lactoOvoType.clLactoOvoLayout.setOnClickListener {
                    setVeganTypeUi("LACTO_OVO")
                    vegetarianType = "LACTO_OVO"
                }

                pescoType.clPescoLayout.setOnClickListener {
                    setVeganTypeUi("PESCO")
                    vegetarianType = "PESCO"
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun modifyUserInfo() {
        mypageViewmodel.apply {
            binding.apply {
                btnMypageModify.setOnClickListener {
                    lifecycleScope.launch {

                        if (isUserInfoStateCheck(
                                nickname = tietMypageNickname.text.toString(),
                                height = tietMypageHeight.text.toString().toInt(),
                                weight = tietMypageWeight.text.toString().toInt())
                            ) {
                            viewModelScope.launch {
                                mypageViewmodel.modifyProfile(
                                    context = requireContext(),
                                    nickname = tietMypageNickname.text.toString(),
                                    vegetarianType = vegetarianType,
                                    gender = profileInfoResponse.value!!.gender,
                                    birthYear = tietMypageAge.text.toString().toInt(),
                                    height = tietMypageHeight.text.toString().toInt(),
                                    weight = tietMypageWeight.text.toString().toInt(),
                                    existingImageUrl = existingImageUrl,
                                    profileUris = profileImageUri
                                )
                            }

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
            if (!profile.imageUrl.isNullOrEmpty()) {
                existingImageUrl = profile.imageUrl

                Glide.with(requireContext())
                    .load(profile.imageUrl)
                    .apply(
                        RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 사용 안 함
                            .skipMemoryCache(true) // 메모리 캐시 사용 안 함
                            .signature(
                                ObjectKey(
                                    System.currentTimeMillis().toString()
                                )
                            ) // 매번 새로운 signature 사용
                    )
                    .into(ivMypageProfile)
            } else {
                existingImageUrl = null
                Glide.with(requireContext()).load(R.drawable.all_profile_basic)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivMypageProfile)
            }


            tietMypageNickname.setText(profile.nickname)
            tietMypageId.setText(profile.email)
            tietMypageHeight.setText(profile.height.toString())
            tietMypageWeight.setText(profile.weight.toString())
            tietMypageAge.setText(profile.birthYear.toString())

            setVeganTypeUi(profile.vegetarianType)
            vegetarianType = profile.vegetarianType
            setGenderUi(profile.gender)
        }
    }

    // Context Menu를 띄우는 함수
    private fun showProfileImageMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.ivMypagePhoto)
        popupMenu.menuInflater.inflate(R.menu.menu_mypage_profile_context_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.selectImage -> {
                    openGallery() // 갤러리 열기
                    true
                }

                R.id.deleteImage -> {
                    deleteProfileImage() // 프로필 사진 삭제
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    // 프로필 사진 삭제
    private fun deleteProfileImage() {
        existingImageUrl = "" // 기존 서버 이미지 URL 제거
        profileImageUri = null // 새 이미지 Uri도 제거
        binding.ivMypageProfile.setImageResource(R.drawable.all_profile_basic) // 기본 이미지로 변경
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

    private fun getUserInfo() {
        mypageViewmodel.getUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}