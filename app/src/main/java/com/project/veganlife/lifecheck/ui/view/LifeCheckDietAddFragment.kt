package com.project.veganlife.lifecheck.ui.view

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentLifeCheckDietAddBinding
import com.project.veganlife.databinding.LayoutLifecheckDietAddBinding
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogDTO
import com.project.veganlife.lifecheck.ui.adapter.LifeCheckDietAddAdapter
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel
import com.project.veganlife.utils.PhotoUtils
import com.project.veganlife.utils.getCurrentTimestamp
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class LifeCheckDietAddFragment : Fragment() {

    private var _binding: FragmentLifeCheckDietAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LifeCheckViewModel by activityViewModels()

    private val photoAdapter = LifeCheckDietAddAdapter { uri ->
        removePhoto(uri)
    }
    private val photoList = mutableListOf<Uri>()
    private lateinit var pickImagesLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckDietAddBinding.inflate(inflater, container, false)

        initLauncher()
        setupToolbar()
        setupObservers()
        setupClickListeners()
        setupRecyclerView()

        return binding.root
    }

    private fun setupToolbar() {
        binding.toolbarLifecheckDietAdd.apply {
            setNavigationOnClickListener {
                viewModel.clearDynamicMealList()
                findNavController().popBackStack()
            }
        }
    }

    private fun initLauncher() {
        pickImagesLauncher =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                if (uris.isNotEmpty()) {
                    val maxSelect = 5 - photoList.size
                    val photo = uris.take(maxSelect)
                    photoList.addAll(photo)
                    photoAdapter.submitList(photoList.toList())
                    binding.rvLifecheckDietAddPhoto.visibility = View.VISIBLE
                    updatePhotoCount()
                }
            }
    }

    private fun setupRecyclerView() {
        binding.rvLifecheckDietAddPhoto.apply {
            adapter = photoAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun removePhoto(uri: Uri) {
        photoList.remove(uri)
        photoAdapter.submitList(photoList.toList())
        if (photoList.isEmpty()) {
            binding.rvLifecheckDietAddPhoto.visibility = View.GONE
        }
        updatePhotoCount()
    }

    private fun updatePhotoCount() {
        val currentCount = photoList.size
        val maxCount = 5
        binding.tvLiffecheckDietAddPhotoCount.text = "(${currentCount}/${maxCount})"
    }

    private fun setupObservers() {
        val mealId = arguments?.getLong("mealId") ?: -1
        if (mealId != -1L) {
            viewModel.fetchMealDataById(mealId)
        }

        viewModel.dynamicMealList.observe(viewLifecycleOwner) { mealDataList ->
            updateDynamicViews(mealDataList)
        }

        viewModel.mealDataById.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val mealData = result.data
                        if (viewModel.getMealDataList().any { it.id == mealData.id }) {
                            Toast.makeText(context, "해당 메뉴는 이미 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addMealData(mealData)
                        }
                    }

                    is ApiResult.Error -> {
                        Log.e("LifeCheckDietAdd", "Error fetching mealData: ${result.description}")
                    }

                    is ApiResult.Exception -> {
                        Log.e("LifeCheckDietAdd", "Exception fetching mealData", result.e)
                    }
                }
            }
        }

        viewModel.registerMealLogResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Log.d("DietAddFragment_registerMealLogResult", "식사 기록 등록 성공")
                        viewModel.clearDynamicMealList()
                        findNavController().navigate(
                            R.id.action_lifeCheckDietAddFragment_to_lifeCheckHomeFragment
                        )
                    }

                    is ApiResult.Error -> {
                        Log.d(
                            "DietAddFragment_registerMealLogResult",
                            "오류 발생: ${result.description}"
                        )
                    }

                    is ApiResult.Exception -> {
                        Log.d("DietAddFragment_registerMealLogResult", "예외 발생: ${result.e.message}")
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            clLifecheckDietAddDietPlus.setOnClickListener {
                findNavController().navigate(
                    LifeCheckDietAddFragmentDirections
                        .actionLifeCheckDietAddFragmentToLifeCheckMenuSearchFragment("dietAdd")
                )
            }

            ibLifecheckDietAddUploadPhoto.setOnClickListener {
                val maxSelect = 5 - photoList.size
                if (maxSelect > 0) {
                    pickImagesLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                } else {
                    Toast.makeText(context, "최대 5장까지 첨부 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            btnLifecheckDietAddImport.setOnClickListener {
                registerMealLog()
            }

            btnLifecheckDietAddCancel.setOnClickListener {
                viewModel.clearDynamicMealList()
                findNavController().navigate(
                    R.id.action_lifeCheckDietAddFragment_to_lifeCheckHomeFragment
                )
            }
        }
    }

    private fun updateDynamicViews(mealDataList: List<LifeCheckMealDataDetail>) {
        val parentLayout = binding.root.findViewById<LinearLayout>(R.id.root_layout)
        val existingTags = mutableSetOf<Long>()
        for (i in 0 until parentLayout.childCount) {
            val child = parentLayout.getChildAt(i)
            val tag = child.tag as? Long
            if (tag != null) {
                existingTags.add(tag)
            }
        }

        mealDataList.forEach { mealData ->
            if (!existingTags.contains(mealData.id)) {
                addNewDietEntry(mealData)
            }
        }
    }

    private fun addNewDietEntry(mealData: LifeCheckMealDataDetail) {
        val inflater = LayoutInflater.from(requireContext())
        val parentLayout = binding.root.findViewById<LinearLayout>(R.id.root_layout)
        val newDietBinding = LayoutLifecheckDietAddBinding.inflate(inflater, binding.root, false)
        var intakeValue = viewModel.getIntakeValue(mealData.id)
        val displayIntakeValue =
            if (intakeValue >= 1.0) intakeValue.toInt().toString() else intakeValue.toString()

        newDietBinding.tvLifecheckDietAddMenuIntake.text = displayIntakeValue
        newDietBinding.btnLifecheckDietAddMenuPlus.setOnClickListener {
            intakeValue = when (intakeValue) {
                0.25 -> {
                    newDietBinding.btnLifecheckDietAddMenuMinus.setBackgroundResource(R.drawable.lifecheck_minus)
                    0.5
                }

                0.5 -> 1.0
                else -> intakeValue + 1
            }
            viewModel.setIntakeValue(mealData.id, intakeValue)
            val displayValue =
                if (intakeValue >= 1.0) intakeValue.toInt().toString() else intakeValue.toString()

            newDietBinding.tvLifecheckDietAddMenuIntake.text = displayValue
            setupUI(newDietBinding, intakeValue, mealData)
        }

        newDietBinding.btnLifecheckDietAddMenuMinus.setOnClickListener {
            intakeValue = when (intakeValue) {
                0.25 -> {
                    parentLayout.removeView(newDietBinding.root)
                    viewModel.removeMealData(mealData)
                    viewModel.removeIntakeValue(mealData.id)
                    updateTotalCalories()
                    return@setOnClickListener
                }

                0.5 -> {
                    newDietBinding.btnLifecheckDietAddMenuMinus.setImageDrawable(null)
                    newDietBinding.btnLifecheckDietAddMenuMinus.setBackgroundResource(R.drawable.lifecheck_delete)
                    0.25
                }

                1.0 -> {
                    newDietBinding.btnLifecheckDietAddMenuMinus.setBackgroundResource(R.drawable.lifecheck_minus)
                    0.5
                }

                else -> intakeValue - 1
            }
            viewModel.setIntakeValue(mealData.id, intakeValue)
            val displayValue =
                if (intakeValue >= 1.0) intakeValue.toInt().toString() else intakeValue.toString()

            newDietBinding.tvLifecheckDietAddMenuIntake.text = displayValue
            setupUI(newDietBinding, intakeValue, mealData)
        }

        setupUI(newDietBinding, intakeValue, mealData)

        val index = parentLayout.indexOfChild(binding.clLifecheckDietAddDietPlus)
        newDietBinding.root.tag = mealData.id
        parentLayout.addView(newDietBinding.root, index)
        updateTotalCalories()
    }

    private fun setupUI(
        binding: LayoutLifecheckDietAddBinding,
        intakeValue: Double,
        mealData: LifeCheckMealDataDetail
    ) {
        binding.apply {
            tvLifecheckDietAddMenuName.text = mealData.name
            etLifecheckDietAddMenuCarbohydrate.text =
                String.format(
                    "%.2f%s",
                    mealData.amountPerServe * mealData.carbsPerUnit * intakeValue,
                    "g"
                )
            etLifecheckDietAddMenuProtein.text =
                String.format(
                    "%.2f%s",
                    mealData.amountPerServe * mealData.proteinPerUnit * intakeValue,
                    "g"
                )
            etLifecheckDietAddMenuFat.text =
                String.format(
                    "%.2f%s",
                    mealData.amountPerServe * mealData.fatPerUnit * intakeValue,
                    "g"
                )
            tvLifecheckDietAddMenuKcalValue.text =
                String.format(
                    "%.2f",
                    mealData.amountPerServe * mealData.caloriePerUnit * intakeValue
                )
            tvLifecheckDietAddMenuOneServing.text =
                String.format(
                    "%d%s",
                    (mealData.amountPerServe * intakeValue).toInt(),
                    mealData.intakeUnit.lowercase()
                )
        }
        updateTotalCalories()
    }

    private fun updateTotalCalories() {
        val parentLayout = binding.root.findViewById<LinearLayout>(R.id.root_layout)
        var totalCalories = 0.0

        for (i in 0 until parentLayout.childCount) {
            val child = parentLayout.getChildAt(i)
            val kcalTextView =
                child.findViewById<TextView>(R.id.tv_lifecheck_diet_add_menu_kcal_value)
            if (kcalTextView != null) {
                val kcalValue = kcalTextView.text.toString().toDoubleOrNull() ?: 0.0
                totalCalories += kcalValue
            }
        }

        val totalKcalTextView =
            binding.root.findViewById<TextView>(R.id.tv_lifecheck_diet_add_total_kcal_value)
        totalKcalTextView.text = String.format("%.2f", totalCalories)
    }

    private fun registerMealLog() {
        val mealType = when (viewModel.selectedDietType.value) {
            getString(R.string.lifecheck_morning) -> "BREAKFAST"
            getString(R.string.lifecheck_lunch) -> "LUNCH"
            getString(R.string.lifecheck_dinner) -> "DINNER"
            getString(R.string.lifecheck_morning_snack) -> "BREAKFAST_SNACK"
            getString(R.string.lifecheck_afternoon_snack) -> "LUNCH_SNACK"
            getString(R.string.lifecheck_dinner_snack) -> "DINNER_SNACK"
            else -> "UNKNOWN"
        }

        val mealLogList = viewModel.getMealDataList().map { mealData ->
            val intakeValue = viewModel.getIntakeValue(mealData.id)
            LifeCheckMealLogDTO(
                intake = (mealData.amountPerServe * intakeValue).toInt(),
                calorie = (mealData.amountPerServe * mealData.caloriePerUnit * intakeValue).toInt(),
                carbs = (mealData.amountPerServe * mealData.carbsPerUnit * intakeValue).toInt(),
                protein = (mealData.amountPerServe * mealData.proteinPerUnit * intakeValue).toInt(),
                fat = (mealData.amountPerServe * mealData.fatPerUnit * intakeValue).toInt(),
                mealDataId = mealData.id
            )
        }

        if (mealLogList.isEmpty()) {
            Toast.makeText(context, "추가된 음식이 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val gson = Gson()
        val requestBody = gson.toJson(
            mapOf(
                "mealType" to mealType,
                "date" to getCurrentTimestamp().substring(0,10),
                "meals" to mealLogList
            )
        ).toRequestBody("application/json".toMediaTypeOrNull())

        val imageParts = mutableListOf<MultipartBody.Part>()
        for (uri in photoList) {
            val filePath =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    PhotoUtils.optimizeBitmap(requireContext(), uri)
                } else {
                    null
                }

            if (filePath != null) {
                val imagePart = PhotoUtils.createImagesMultipart(filePath)
                if (imagePart != null) {
                    imageParts.add(imagePart)
                }
            }
        }

        viewModel.registerMealLog(requestBody, imageParts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}