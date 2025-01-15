package com.project.veganlife.recipe.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentRecipeWriteBinding
import com.project.veganlife.recipe.ui.adapter.RecipeWriteImagesViewPagerAdapter
import com.project.veganlife.recipe.ui.adapter.RecipeWriteDescriptionAdapter
import com.project.veganlife.recipe.ui.adapter.RecipeWriteIngredientAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeWriteFragment : Fragment() {
    private var _binding: FragmentRecipeWriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientAdapter: RecipeWriteIngredientAdapter
    private lateinit var descriptionAdapter: RecipeWriteDescriptionAdapter
    private lateinit var photoAdapter: RecipeWriteImagesViewPagerAdapter

    private val args: RecipeWriteFragmentArgs by navArgs()

    private val selectedVeganTypes = mutableSetOf<String>()
    private val ingredientList = mutableListOf<String>()
    private val descriptionList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarListener()

        if (args.isEditing) for (i in args.recipeIngredientDescription!!.recipeTypes) setVeganTypeUi(i) else selectVeganType()

        setRecyclerviewAdapter()
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

    private fun setRecyclerviewAdapter() {
        // 글쓰기 리싸이클러뷰 셋팅
        if(args.isEditing == false) {
            ingredientList.add("")
            descriptionList.add("")

            ingredientAdapter = RecipeWriteIngredientAdapter(
                onDeleteClick = { position ->
                    ingredientList.removeAt(position)
                    ingredientAdapter.submitList(ingredientList)
                    Log.d("리스트",ingredientList.toString())
                },
                onTextChange = { position, text ->
                    ingredientList[position] = text
                    Log.d("리스트",ingredientList.toString())
                }
            )

            descriptionAdapter = RecipeWriteDescriptionAdapter { position ->
                // x 버튼 클릭 시 해당 아이템 삭제
                descriptionList.removeAt(position)
                descriptionAdapter.submitList(descriptionList.toList())
            }

            binding.apply {
                rvRecipeIngredinet.adapter = ingredientAdapter
                rvRecipeDescription.adapter = descriptionAdapter

                ingredientAdapter.submitList(ingredientList.toList())
                descriptionAdapter.submitList(descriptionList.toList())

                btnRecipeIngredientAdd.setOnClickListener {
                    if(ingredientList.size >= 20) {
                        messageToast("재료")
                    } else {
                        ingredientList.add("")
                        ingredientAdapter.submitList(ingredientList.toList())
                    }
                }

                btnRecipeDescriptionAdd.setOnClickListener {
                    if(descriptionList.size >= 20) {
                        messageToast("레시피")
                    } else {
                        descriptionList.add("")
                        descriptionAdapter.submitList(descriptionList.toList())
                    }
                }
            }
        }

        else {
            // 수정 리싸이클러뷰 셋팅
            args.recipeIngredientDescription?.apply {
                ingredientList.addAll(ingredients.toList())
                descriptionList.addAll(descriptions.toList())
            }
        }
    }

    private fun messageToast(text: String) {
        if(text == "재료")
            Toast.makeText(context,"${text}는 최대 20개까지 추가할 수 있습니다.",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"${text} 설명은 최대 20개까지 추가할 수 있습니다.",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}