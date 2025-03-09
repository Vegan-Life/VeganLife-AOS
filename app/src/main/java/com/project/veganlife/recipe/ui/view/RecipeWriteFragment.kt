package com.project.veganlife.recipe.ui.view

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentRecipeWriteBinding
import com.project.veganlife.recipe.data.model.RecipeWriteDescription
import com.project.veganlife.recipe.data.model.RecipeWriteFeedPhoto
import com.project.veganlife.recipe.data.model.RecipeWriteIngredient
import com.project.veganlife.recipe.ui.adapter.RecipeWriteImagesViewPagerAdapter
import com.project.veganlife.recipe.ui.adapter.RecipeWriteDescriptionAdapter
import com.project.veganlife.recipe.ui.adapter.RecipeWriteIngredientAdapter
import com.project.veganlife.recipe.ui.viewmodel.RecipeViewmodel
import com.project.veganlife.utils.KeyboardUtils.hideKeyboard
import com.project.veganlife.utils.KeyboardUtils.hideKeyboardOnTouch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeWriteFragment : Fragment() {
    private var _binding: FragmentRecipeWriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientAdapter: RecipeWriteIngredientAdapter
    private lateinit var descriptionAdapter: RecipeWriteDescriptionAdapter
    private lateinit var photoAdapter: RecipeWriteImagesViewPagerAdapter
    private val viewModel: RecipeViewmodel by viewModels()

    private val args: RecipeWriteFragmentArgs by navArgs()

    private val selectedVeganTypes = mutableSetOf<String>()
    private var ingredientList = mutableListOf<RecipeWriteIngredient>()
    private var descriptionList = mutableListOf<RecipeWriteDescription>()

    private val photoList = mutableListOf<RecipeWriteFeedPhoto>() // 사진 리스트
    private val imageUris = mutableListOf<Uri>()
    private val existingImageList = mutableListOf<String>()

    private val MAX_PHOTO_COUNT = 5 // 최대 사진 개수
    private var remain = MAX_PHOTO_COUNT - photoList.size

    private var setRecipeTitle = ""
    private var setRecipeId = 0L

    private var result = ""

    @RequiresApi(Build.VERSION_CODES.R)
    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(remain)) { uris ->
            val availableSlots = MAX_PHOTO_COUNT - photoList.size

            // 선택한 미디어 항목의 URI 리스트 처리
            if (uris.size > availableSlots) {
                // 남은 개수를 초과했을 경우 알림
                messageToast("최대 ${MAX_PHOTO_COUNT - photoList.size}장까지만 첨부 가능합니다.")
            } else {
                // 선택한 사진을 photoList에 추가
                val newPhotos = uris.mapIndexed { index, uri ->
                    RecipeWriteFeedPhoto(
                        number = photoList.size + index,
                        photo = uri.toString(),
                        isExisting = false
                    )
                }
                photoList.addAll(newPhotos)
                imageUris.addAll(uris)
                updatePhotoList() // RecyclerView 업데이트
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.isEditing) {
            setRecipeId = args.recipeId!!.id
            args.recipeIngredientDescription?.apply {
                setRecipeTitle = recipeTitle

                for (i in 0 until ingredients.size) {
                    ingredientList.add(RecipeWriteIngredient(i, ingredients[i]))
                }
                for (i in 0 until descriptions.size) {
                    descriptionList.add(RecipeWriteDescription(i, descriptions[i]))
                }
                for (i in 0 until imageUrls.size) {
                    photoList.add((RecipeWriteFeedPhoto(i, imageUrls[i], true)))
                }
                existingImageList.addAll(imageUrls)
            }
        } else {
            ingredientList.add(RecipeWriteIngredient(0, ""))
            descriptionList.add(RecipeWriteDescription(0, ""))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboardOnTouch(this, binding.root)

        setToolbarListener()

        binding.etRecipeRecipeTitle.setText(setRecipeTitle)

        if (args.isEditing) for (i in args.recipeIngredientDescription!!.recipeTypes) setVeganTypeUi(
            i
        )

        if(args.isEditing) {
            val photoCnt = args.recipeIngredientDescription!!.imageUrls.size.takeIf { it > 0 }?.toString() ?: "0"

            binding.tvRecipeUploadPhotoCnt.text = "${photoCnt}/$MAX_PHOTO_COUNT"
        }

        selectVeganType()
        setRecyclerviewAdapter()

        // 게시글 등록 및 수정
        binding.btnRecipeUpload.setOnClickListener {
            if (args.isEditing) {
                // 게시글 등록
                result = registerRecipe("수정")
            } else {
                // 게시글 수정
                result = registerRecipe("등록")
            }
        }
        val responseLiveData =
            if (args.isEditing) viewModel.recipeModifyResponse else viewModel.recipeRegisterResponse
        observeRecipeResponse(responseLiveData)
    }

    private fun setToolbarListener() {
        binding.toolbarRecipeWriteRecipe.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun selectVeganType() {
        binding.apply {
            includeRecipeVeganType.clVeganTypeLayout.setOnClickListener { setVeganTypeUi("VEGAN") }
            includeRecipeLactoType.clLactoLayout.setOnClickListener { setVeganTypeUi("LACTO") }
            includeRecipeOvoType.clOvoLayout.setOnClickListener { setVeganTypeUi("OVO") }
            includeRecipeLactoOvoType.clLactoOvoLayout.setOnClickListener { setVeganTypeUi("LACTO_OVO") }
            includeRecipePescoType.clPescoLayout.setOnClickListener { setVeganTypeUi("PESCO") }
        }
    }

    private fun setVeganTypeUi(type: String) {
        binding.apply {
            // 선택 상태를 토글
            if (selectedVeganTypes.contains(type)) {
                selectedVeganTypes.remove(type) // 이미 선택된 경우 해제
            } else {
                if (selectedVeganTypes.size >= 2) {
                    // 최대 2개 초과 시 Snackbar 표시
                    Toast.makeText(context, "채식 타입은 최대 2개까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    return
                }
                selectedVeganTypes.add(type) // 새로 선택
            }

            // 각 타입에 대한 UI 업데이트
            includeRecipeVeganType.apply {
                val isSelected = selectedVeganTypes.contains("VEGAN")
                ivMypageImage.setImageResource(
                    if (isSelected) R.drawable.all_vegan_type_vegan else R.drawable.mypage_vegan_type_vegan_gray
                )
                tvMypageVeganType.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (isSelected) R.color.base3 else R.color.gray2
                    )
                )
                tvMypageVeganType.isSelected = isSelected
            }
            includeRecipeLactoType.apply {
                val isSelected = selectedVeganTypes.contains("LACTO")
                ivMypageImage.setImageResource(
                    if (isSelected) R.drawable.all_vegan_type_lacto else R.drawable.mypage_vegan_type_lacto_gray
                )
                tvMypageVeganType.isSelected = isSelected
            }
            includeRecipeOvoType.apply {
                val isSelected = selectedVeganTypes.contains("OVO")
                ivMypageImage.setImageResource(
                    if (isSelected) R.drawable.all_vegan_type_ovo else R.drawable.mypage_vegan_type_ovo_gray
                )
                tvMypageVeganType.isSelected = isSelected
            }
            includeRecipeLactoOvoType.apply {
                val isSelected = selectedVeganTypes.contains("LACTO_OVO")
                ivMypageImage.setImageResource(
                    if (isSelected) R.drawable.all_vegan_type_lacto_ovo else R.drawable.mypage_vegan_type_lacto_ovo_gray
                )
                tvMypageVeganType.isSelected = isSelected
            }
            includeRecipePescoType.apply {
                val isSelected = selectedVeganTypes.contains("PESCO")
                ivMypageImage.setImageResource(
                    if (isSelected) R.drawable.all_vegan_type_pesco else R.drawable.mypage_vegan_type_pesco_gray
                )
                tvMypageVeganType.isSelected = isSelected
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun setRecyclerviewAdapter() {
        ingredientAdapter = RecipeWriteIngredientAdapter(
            onDeleteClick = { position ->
                if (position in ingredientList.indices) {
                    ingredientList.removeAt(position)
                    updateIngredientNumbers() // 삭제 후 number를 재정렬
                    ingredientAdapter.submitList(ingredientList.toList())
                    binding.rvRecipeIngredinet.layoutManager?.requestLayout() // 레이아웃 갱신
                }
            },
            onTextChange = { position, text ->
                if (position in ingredientList.indices) {
                    ingredientList[position] = ingredientList[position].copy(ingredient = text)
                }
            }
        )

        descriptionAdapter = RecipeWriteDescriptionAdapter(
            // x 버튼 클릭 시 해당 아이템 삭제
            onDeleteClick = { position ->
                if (position in descriptionList.indices) {
                    descriptionList.removeAt(position)
                    updateDescriptionNumbers() // 삭제 후 number를 재정렬
                    descriptionAdapter.submitList(descriptionList.toList())
                    binding.rvRecipeIngredinet.layoutManager?.requestLayout() // 레이아웃 갱신
                }
            },
            onTextChange = { position, text ->
                if (position in descriptionList.indices) {
                    descriptionList[position] =
                        descriptionList[position].copy(description = text)
                }
            }
        )

        // 사진 어댑터 설정
        photoAdapter = RecipeWriteImagesViewPagerAdapter(
            onDeleteClick = { position ->
                if (position in photoList.indices) {
                    val deleteItem = photoList[position]
                    if (deleteItem.isExisting) {
                        if (existingImageList.contains(deleteItem.photo)) existingImageList.remove(
                            deleteItem.photo
                        )
                    } else {
                        val uri = Uri.parse(deleteItem.photo)
                        if (imageUris.contains(uri)) imageUris.remove(uri)
                    }

                    // Recyclerview 리스트에서도 제거
                    photoList.removeAt(position)
                    updatePhotoList()
                }
            })

        binding.apply {
            rvRecipeIngredinet.adapter = ingredientAdapter
            rvRecipeDescription.adapter = descriptionAdapter
            rvRecipeWriteEditFeedPhoto.adapter = photoAdapter

            ingredientAdapter.submitList(ingredientList.toList())
            descriptionAdapter.submitList(descriptionList.toList())
            photoAdapter.submitList(photoList.toList())

            btnRecipeIngredientAdd.setOnClickListener {
                hideKeyboard(requireActivity())
                if (ingredientList.size >= 20) {
                    messageToast("재료")
                } else {
                    val newNumber = ingredientList.size
                    ingredientList.add(RecipeWriteIngredient(newNumber, ""))
                    ingredientAdapter.submitList(ingredientList.toList())
                }
            }

            btnRecipeDescriptionAdd.setOnClickListener {
                hideKeyboard(requireActivity())
                if (descriptionList.size >= 20) {
                    messageToast("레시피")
                } else {
                    val newNumber = descriptionList.size
                    descriptionList.add(RecipeWriteDescription(newNumber, ""))
                    descriptionAdapter.submitList(descriptionList.toList())
                }
            }

            // 앨범 버튼 클릭 이벤트 처리
            ibRecipeWriteEditFeedUploadPhoto.setOnClickListener {
                hideKeyboard(requireActivity())
                pickPhotos()
                requireActivity().currentFocus?.clearFocus()
            }
        }
    }

    private fun updateIngredientNumbers() {
        ingredientList = ingredientList.mapIndexed { index, ingredient ->
            ingredient.copy(number = index) // number만 갱신
        }.toMutableList()
    }

    private fun updateDescriptionNumbers() {
        descriptionList = descriptionList.mapIndexed { index, description ->
            description.copy(number = index) // number만 갱신
        }.toMutableList()
    }

    private fun updatePhotoList() {
        photoAdapter.submitList(photoList.toList())
        binding.tvRecipeUploadPhotoCnt.text = "(${photoList.size}/${MAX_PHOTO_COUNT})"
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun pickPhotos() {
        val availableSlots = MAX_PHOTO_COUNT - photoList.size
        if (availableSlots > 0) {
            pickMultipleMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            // 추가 가능한 이미지가 없을 경우 메시지 출력
            Toast.makeText(
                requireContext(),
                "최대 ${MAX_PHOTO_COUNT}장까지만 첨부 가능합니다.",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun registerRecipe(text: String): String {
        binding.apply {
            if (etRecipeRecipeTitle.text.isNullOrBlank() || ingredientList.size == 0 || descriptionList.size == 0)
                messageToast("미기입된 항목이 있습니다. 제목, 재료, 방법, 사진을 확인해주세요.")
            else {
                lifecycleScope.launch {
                    if (args.isEditing == false) {
                        viewModel.registerRecipe(
                            requireContext(),
                            etRecipeRecipeTitle.text.toString(),
                            selectedVeganTypes.toList(),
                            ingredientList.map { it.ingredient },
                            descriptionList.map { it.description },
                            imageUris
                        )
                    } else {
                        viewModel.modifyRecipe(
                            requireContext(),
                            setRecipeId,
                            etRecipeRecipeTitle.text.toString(),
                            selectedVeganTypes.toList(),
                            ingredientList.map { it.ingredient },
                            descriptionList.map { it.description },
                            existingImageList,
                            imageUris
                        )
                    }
                }
            }
        }
        return text
    }

    private fun observeRecipeResponse(responseLiveData: LiveData<Any>) {
        responseLiveData.observe(viewLifecycleOwner) { response ->
            Log.d("RecipeWriteFragment", "API 응답: $response")
            when (response) {
                201 -> {
                    findNavController().navigate(
                        R.id.action_recipeWriteFragment_to_recipeHomeFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.recipeWriteFragment, true) // 등록 화면을 백스택에서 제거
                            .build()
                    )
                    messageToast("레시피가 ${result}되었습니다.")
                }

            }
        }
    }

    private fun messageToast(text: String) {
        if (text == "재료")
            Toast.makeText(context, "${text}는 최대 20개까지 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
        else if (text == "레시피")
            Toast.makeText(context, "${text} 설명은 최대 20개까지 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}