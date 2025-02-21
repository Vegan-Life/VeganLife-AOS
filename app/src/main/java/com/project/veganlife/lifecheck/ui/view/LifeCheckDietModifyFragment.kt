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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentLifeCheckDietModifyBinding
import com.project.veganlife.databinding.LayoutLifecheckDietAddBinding
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogDTO
import com.project.veganlife.lifecheck.ui.adapter.LifeCheckDietAddAdapter
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel
import com.project.veganlife.utils.PhotoUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class LifeCheckDietModifyFragment : Fragment() {

    private var _binding: FragmentLifeCheckDietModifyBinding? = null
    private val binding get() = _binding!!

    private val args: LifeCheckDietModifyFragmentArgs by navArgs()
    private val viewModel: LifeCheckViewModel by activityViewModels()

    private val photoAdapter = LifeCheckDietAddAdapter { uri ->
        removePhoto(uri)
    }
    private val serverPhotoList = mutableListOf<Uri>()
    private val userPhotoList = mutableListOf<Uri>()
    private lateinit var pickImagesLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckDietModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initLauncher()
        setupObservers()
        setupRecyclerView()
        setupClickListeners()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.includeLifecheckDietModify.toolbarLifecheckDietAdd.apply {
            setNavigationOnClickListener {
                viewModel.clearDynamicMealList()
                findNavController().popBackStack()
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            includeLifecheckDietModify.clLifecheckDietAddDietPlus.setOnClickListener {
                findNavController().navigate(
                    LifeCheckDietModifyFragmentDirections
                        .actionLifeCheckDietModifyFragmentToLifeCheckMenuSearchFragment("dietModify")
                )
            }

            includeLifecheckDietModify.ibLifecheckDietAddUploadPhoto.setOnClickListener {
                val maxSelect = 5 - (serverPhotoList.size + userPhotoList.size)
                if (maxSelect > 0) {
                    pickImagesLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                } else {
                    Toast.makeText(context, "최대 5장까지 첨부 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            includeLifecheckDietModify.btnLifecheckDietAddImport.apply {
                setOnClickListener {
//                    modifyMealLog()
                }
                text = getString(R.string.all_button_modify)
            }

            includeLifecheckDietModify.btnLifecheckDietAddCancel.apply {
                setOnClickListener {

                }
                text = getString(R.string.all_dialog_cancel)
            }
        }
    }

    private fun initData() {
        val mealLogId = args.mealLogId
        if (mealLogId != -1L) {
            viewModel.fetchMealLogDetail(mealLogId)
        }

        val mealId = args.mealId
        if (mealId != -1L) {
            viewModel.fetchMealDataById(mealId)
        }

        // ViewModel에서 저장된 사진 리스트 불러오기
        viewModel.serverPhotoList.value.let { serverPhotoList.addAll(it) }
        viewModel.userPhotoList.value.let { userPhotoList.addAll(it) }
        updatePhotoUI()
    }

    private fun setupObservers() {
        viewModel.dynamicMealList.observe(viewLifecycleOwner) { mealDataList ->
            updateDynamicViews(mealDataList)
        }

        viewModel.mealLogDetail.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val mealDetail = result.data
                        val mappedMeals = mealDetail.meals.map { meal ->
                            viewModel.setIntakeValue(
                                meal.mealData.id,
                                meal.intake.toDouble() / meal.mealData.amountPerServe
                            )
                            meal.mealData.copy(amount = meal.intake)
                        }

                        viewModel.addMealDataList(mappedMeals)

                        viewModel.setServerPhotoList(mealDetail.imageUrls.map { Uri.parse(it) })
//                        serverPhotoList.addAll(mealDetail.imageUrls.map { Uri.parse(it) })

                        updatePhotoUI()
                    }

                    is ApiResult.Error -> Log.e("LifeCheckDietModify", result.description)
                    is ApiResult.Exception -> Log.e(
                        "LifeCheckDietModify",
                        result.e.message ?: "Unknown error"
                    )
                }
            }
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
                        Log.e(
                            "LifeCheckDietModify",
                            "Error fetching mealData: ${result.description}"
                        )
                    }

                    is ApiResult.Exception -> {
                        Log.e("LifeCheckDietModify", "Exception fetching mealData", result.e)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.serverPhotoList.collect { list ->
                    serverPhotoList.clear()
                    serverPhotoList.addAll(list)
                    updatePhotoUI()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userPhotoList.collect { list ->
                    userPhotoList.clear()
                    userPhotoList.addAll(list)
                    updatePhotoUI()
                }
            }
        }

        viewModel.modifyMealLogResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Log.d("DietModifyFragment_modifyMealLogResult", "식사 기록 수정 성공")
                        viewModel.clearDynamicMealList()
                        findNavController().navigate(
                            R.id.action_lifeCheckDietModifyFragment_to_lifeCheckHomeFragment
                        )
                    }

                    is ApiResult.Error -> {
                        Log.d(
                            "DietModifyFragment_modifyMealLogResult",
                            "오류 발생: ${result.description}"
                        )
                    }

                    is ApiResult.Exception -> {
                        Log.d("DietModifyFragment_modifyMealLogResult", "예외 발생: ${result.e.message}")
                    }
                }
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

        val index =
            parentLayout.indexOfChild(binding.includeLifecheckDietModify.clLifecheckDietAddDietPlus)
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

    private fun setupRecyclerView() {
        binding.includeLifecheckDietModify.rvLifecheckDietAddPhoto.apply {
            adapter = photoAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initLauncher() {
        pickImagesLauncher =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                if (uris.isNotEmpty()) {
                    val maxSelect = 5 - userPhotoList.size
                    val photo = uris.take(maxSelect)

                    val updatedList = userPhotoList.toMutableList().apply { addAll(photo) }

                    viewModel.setUserPhotoList(updatedList)
                }
            }
    }

    private fun removePhoto(uri: Uri) {
        if (serverPhotoList.contains(uri)) {
            // 서버에서 불러온 사진 삭제
            serverPhotoList.remove(uri)
        } else if (userPhotoList.contains(uri)) {
            // 사용자가 추가한 사진 삭제
            userPhotoList.remove(uri)
        }
        viewModel.removePhoto(uri)
        updatePhotoUI()
    }

    private fun updatePhotoUI() {
        val mergedPhotoList = serverPhotoList + userPhotoList // 두 리스트를 합침

        if (mergedPhotoList.isEmpty()) {
            binding.includeLifecheckDietModify.rvLifecheckDietAddPhoto.visibility = View.GONE
        } else {
            binding.includeLifecheckDietModify.rvLifecheckDietAddPhoto.visibility = View.VISIBLE
        }

        photoAdapter.submitList(mergedPhotoList)
        updatePhotoCount()
    }

    private fun updatePhotoCount() {
        val currentCount = serverPhotoList.size + userPhotoList.size
        val maxCount = 5
        binding.includeLifecheckDietModify.tvLiffecheckDietAddPhotoCount.text =
            "(${currentCount}/${maxCount})"
    }

    private fun modifyMealLog() {
        val mealLogId = args.mealLogId
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
                "meals" to mealLogList,
                "existingImageUrls" to serverPhotoList
            )
        ).toRequestBody("application/json".toMediaTypeOrNull())

        val imageParts = mutableListOf<MultipartBody.Part>()
        for (uri in userPhotoList) {
            val filePath =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    PhotoUtils.optimizeBitmap(requireContext(), uri)
                } else {
                    null
                }

            if (filePath != null) {
                val imagePart = PhotoUtils.createImageMultipart(filePath)
                if (imagePart != null) {
                    imageParts.add(imagePart)
                }
            }
        }

        viewModel.modifyMealLog(mealLogId,requestBody, imageParts)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}