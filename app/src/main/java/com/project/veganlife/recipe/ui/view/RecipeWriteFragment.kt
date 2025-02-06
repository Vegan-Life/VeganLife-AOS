package com.project.veganlife.recipe.ui.view

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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentRecipeWriteBinding
import com.project.veganlife.recipe.data.model.RecipeRequestDTO
import com.project.veganlife.recipe.data.model.RecipeWriteDescription
import com.project.veganlife.recipe.data.model.RecipeWriteFeedPhoto
import com.project.veganlife.recipe.data.model.RecipeWriteIngredient
import com.project.veganlife.recipe.ui.adapter.RecipeWriteImagesViewPagerAdapter
import com.project.veganlife.recipe.ui.adapter.RecipeWriteDescriptionAdapter
import com.project.veganlife.recipe.ui.adapter.RecipeWriteIngredientAdapter
import com.project.veganlife.recipe.ui.viewmodel.RecipeViewmodel
import com.project.veganlife.utils.PhotoUtils
import com.project.veganlife.utils.PhotoUtils.Companion.createImageMultipart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

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
    private val imagesMulipartList = mutableListOf<MultipartBody.Part>()

    private val MAX_PHOTO_COUNT = 5 // 최대 사진 개수
    private var remain = MAX_PHOTO_COUNT - photoList.size

    private var setRecipeTitle = ""
    private var setRecipeId = 0L

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
                    lifecycleScope.async {
                        if (context != null) {
                            val imageMultipart = withContext(Dispatchers.IO) {
                                // 1. 최적화된 비트맵을 임시 파일로 저장
                                val imagePath = PhotoUtils.optimizeBitmap(requireContext(), uri)
                                // 2. 임시 파일 경로를 사용해 MultipartBody.Part로 변환
                                createImageMultipart(imagePath)
                            }

                            imageMultipart?.let { imagesMulipartList.add(it) }
                        }
                    }

                    RecipeWriteFeedPhoto(
                        number = photoList.size + index,
                        photo = uri.toString()
                    )
                }
                photoList.addAll(newPhotos)
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
                    photoList.add((RecipeWriteFeedPhoto(i, imageUrls[i])))
                }
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

        setToolbarListener()

        binding.etRecipeRecipeTitle.setText(setRecipeTitle)

        if (args.isEditing) for (i in args.recipeIngredientDescription!!.recipeTypes) setVeganTypeUi(
            i
        )
        else selectVeganType()

        setRecyclerviewAdapter()

        // 게시글 등록 및 수정
        binding.btnRecipeUpload.setOnClickListener {
            if (args.isEditing) {
                // 게시글 등록
                registerRecipe("수정")
            } else {
                // 게시글 수정
                registerRecipe("등록")
            }
        }
    }

    private fun setToolbarListener() {
        //TODO: 수정기능으로 왔을 때 saveHandle로 레시피 갱신 해야할 수도 있나 ?
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
                    Log.d("리스트", ingredientList.toString())
                }
            },
            onTextChange = { position, text ->
                if (position in ingredientList.indices) {
                    ingredientList[position] = ingredientList[position].copy(ingredient = text)
                    Log.d("리스트", ingredientList.toString())
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
                    Log.d("리스트", descriptionList.toString())
                }
            },
            onTextChange = { position, text ->
                if (position in descriptionList.indices) {
                    descriptionList[position] =
                        descriptionList[position].copy(description = text)
                    Log.d("리스트", descriptionList.toString())
                }
            }
        )

        // 사진 어댑터 설정
        photoAdapter = RecipeWriteImagesViewPagerAdapter(
            onDeleteClick = { position ->
                if (position in photoList.indices) {
                    photoList.removeAt(position)
                    imagesMulipartList.removeAt(position)
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
                if (ingredientList.size >= 20) {
                    messageToast("재료")
                } else {
                    val newNumber = ingredientList.size
                    ingredientList.add(RecipeWriteIngredient(newNumber, ""))
                    ingredientAdapter.submitList(ingredientList.toList())
                }
            }

            btnRecipeDescriptionAdd.setOnClickListener {
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
                pickPhotos()
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
        Log.d("사진 리스트", photoList.toString())
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

    private fun registerRecipe(text: String) {
        binding.apply {
            Log.d("레시피 제목", etRecipeRecipeTitle.text.toString())
            Log.d("비건 타입", selectedVeganTypes.toString())
            Log.d("재료", ingredientList.size.toString())
            Log.d("순서", descriptionList.size.toString())
            Log.d("사진", imagesMulipartList.size.toString())
            if (etRecipeRecipeTitle.text.isNullOrBlank() || ingredientList.size == 0 || descriptionList.size == 0 ||
                photoList.size == 0
            ) messageToast("미기입된 항목이 있습니다. 제목, 재료, 방법, 사진을 확인해주세요.")
            else {
                Log.d("레시피 사진", imagesMulipartList.toString())
                val recipeDTO = RecipeRequestDTO(
                    recipeTitle = etRecipeRecipeTitle.text.toString(),
                    recipeType = selectedVeganTypes.toList(),
                    ingredients = ingredientList.map { it.ingredient },
                    descriptions = descriptionList.map { it.description },
                )

                lifecycleScope.launch {
                    val recipeRequestBody = withContext(Dispatchers.IO) {
                        PhotoUtils.createProfileRequestBody(recipeDTO)
                    }
                    Log.d("사진 list", imagesMulipartList.toString())

                    if (args.isEditing) viewModel.registerRecipe(
                        recipeRequestBody,
                        imagesMulipartList
                    )
                    else viewModel.modifyRecipe(setRecipeId, recipeRequestBody, imagesMulipartList)

                    viewModel.recipeRegisterResponse.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            201 -> {
                                findNavController().navigate(
                                    R.id.action_recipeWriteFragment_to_recipeHomeFragment,
                                    null,
                                    NavOptions.Builder()
                                        .setPopUpTo(
                                            R.id.recipeWriteFragment,
                                            true
                                        ) // 등록 화면을 백스택에서 제거
                                        .build()
                                )
                                messageToast("레시피가 ${text}되었습니다.")
                            }

                            else -> messageToast("미기입된 항목이 있습니다. 제목, 재료, 방법, 사진을 확인해주세요.")
                        }
                    }
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