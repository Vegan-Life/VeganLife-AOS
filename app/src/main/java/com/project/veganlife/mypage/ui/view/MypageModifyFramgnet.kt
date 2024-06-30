package com.project.veganlife.mypage.ui.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.data.utils.BucketName
import com.project.veganlife.data.utils.PROFILE_FOLDER_PATH
import com.project.veganlife.data.utils.S3Util
import com.project.veganlife.databinding.FragmentMypageModifyFramgnetBinding
import com.project.veganlife.mypage.data.model.ProfileModifyRequest
import com.project.veganlife.mypage.ui.viewmodel.MypageViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.lang.Exception

@AndroidEntryPoint
class MypageModifyFramgnet : Fragment() {
    private var _binding: FragmentMypageModifyFramgnetBinding? = null
    private val binding get() = _binding!!

    private val mypageViewmodel: MypageViewmodel by activityViewModels()

    private val PICK_IMAGE_REQUEST = 1

    private var file: File()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageModifyFramgnetBinding.inflate(inflater, container, false)

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
            binding.apply {
                profileInfoResponse.observe(viewLifecycleOwner) { profile ->
                    Glide.with(requireContext()).load(profile.imageUrl).apply(
                        RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                    )
                        .into(ivMypageProfile)

                    tietMypageNickname.setText(profile.nickname)
                    tietMypageNickname.isEnabled = true

                    tietMypageId.setText(profile.email)
                    tietMypageId.isEnabled = false

                    tietMypageHeight.setText(profile.height.toString())
                    tietMypageWeight.isEnabled = true

                    tietMypageWeight.setText(profile.weight.toString())
                    tietMypageWeight.isEnabled = true

                    tietMypageAge.setText(profile.birthYear.toString())
                    tietMypageAge.isEnabled = false

                    setVeganTypeUi(profile.vegetarianType)
                    setGenderUi(profile.gender)
                }
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
                val filePath = getRealPathFromURI(imageUri)

                Glide.with(this)
                    .load(imageUri)
                    .into(binding.ivMypageProfile)
                file = File(imageUri.path)

                Log.d("modify", imageUri.toString())
                Log.d("modify filePath", filePath.toString())
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity?.contentResolver?.query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        return result
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
                    setVeganType("VEGAN")
                }

                lactoType.clLactoLayout.setOnClickListener {
                    setVeganTypeUi("LACTO")
                    setVeganType("LACTO")
                }

                ovoType.clOvoLayout.setOnClickListener {
                    setVeganTypeUi("OVO")
                    setVeganType("OVO")
                }

                lactoOvoType.clLactoOvoLayout.setOnClickListener {
                    setVeganTypeUi("LACTO_OVO")
                    setVeganType("LACTO_OVO")
                }

                pescoType.clPescoLayout.setOnClickListener {
                    setVeganTypeUi("PESCO")
                    setVeganType("PESCO")
                }
            }
        }
    }

    private fun modifyUserInfo() {
        mypageViewmodel.apply {
            binding.apply {
                btnMypageModify.setOnClickListener {
                    //이미지 aws올리고
                    //url받아와서
                    //저 밑에 넣어주기
                    S3Util.instance.uploadWithTransferUtility(
                        requireContext(),
                        BucketName,
                        PROFILE_FOLDER_PATH,
                        file,
                        object : TransferListener {
                            override fun onStateChanged(id: Int, state: TransferState?) {
                                TODO("Not yet implemented")
                            }

                            override fun onProgressChanged(
                                id: Int,
                                bytesCurrent: Long,
                                bytesTotal: Long
                            ) {
                                TODO("Not yet implemented")
                            }

                            override fun onError(id: Int, ex: Exception?) {
                                TODO("Not yet implemented")
                            }

                        })


                    if (
                        isUserInfoStateCheck(
                            ProfileModifyRequest(
                                nickname = tietMypageNickname.text.toString(),
                                imageUrl = "",
                                vegetarianType = vegetarianType.value.toString(),
                                gender = profileInfoResponse.value!!.gender,
                                birthYear = tietMypageAge.text.toString().toInt(),
                                height = tietMypageHeight.text.toString().toInt(),
                                weight = tietMypageWeight.text.toString().toInt()
                            )
                        )
                    ) {

                    } else {
                        Toast.makeText(requireContext(), "모든 정보를 올바르게 입력해주세요", Toast.LENGTH_SHORT)
                            .show()
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