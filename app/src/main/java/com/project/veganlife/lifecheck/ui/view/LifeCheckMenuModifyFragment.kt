package com.project.veganlife.lifecheck.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentLifeCheckMenuModifyBinding
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LifeCheckMenuModifyFragment : Fragment() {

    private var _binding: FragmentLifeCheckMenuModifyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LifeCheckViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckMenuModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealId = arguments?.getLong("mealId") ?: -1
        if (mealId != -1L) {
            fetchMealData(mealId)
        }

        setupToolbar()
        setupCapacityUnitDropdown()
        setupListeners(mealId)
        addTextWatchers()
        observeUpdateResult()
    }

    private fun fetchMealData(mealId: Long) {
        viewModel.fetchMealDataById(mealId)

        viewModel.mealDataById.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    val data = result.data
                    populateUI(data)
                }

                is ApiResult.Error -> {
                    Log.e("LifeCheckMenuModify", "Error: ${result.description}")
                }

                is ApiResult.Exception -> {
                    Log.e("LifeCheckMenuModify", "Exception ", result.e)
                }
            }
        }
    }

    private fun populateUI(data: LifeCheckMealDataDetail) {
        binding.run {
            etLifecheckMenuModifyName.setText(data.name)
            etLifecheckMenuModifyCapacity.setText(data.amount.toString())
            actvLifecheckMenuModifyCapacityUnit.setText(data.intakeUnit, false)
            etLifecheckMenuModifyStandard.setText(data.amountPerServe.toString())
            etLifecheckMenuModifyKcal.setText((data.caloriePerUnit * data.amountPerServe).toInt().toString())
            etLifecheckMenuModifyCarbohydrate.setText((data.carbsPerUnit * data.amountPerServe).toInt().toString())
            etLifecheckMenuModifyProtein.setText((data.proteinPerUnit * data.amountPerServe).toInt().toString())
            etLifecheckMenuModifyFat.setText((data.fatPerUnit * data.amountPerServe).toInt().toString())
        }
    }

    private fun setupListeners(mealId: Long) {
        binding.run {
            btnLifecheckMenuModifyCancel.setOnClickListener {
                findNavController().popBackStack()
            }

            btnLifecheckMenuModifyImport.setOnClickListener {
                if (isValidInput()) {
                    val updatedData = collectMealData()
                    viewModel.modifyMealData(mealId, updatedData)
                }
            }

            tvLifecheckMenuModifyDelete.setOnClickListener {
                val dialog = LifeCheckCustomDialogFragment.newInstance(mealId, LifeCheckCustomDialogFragment.MODE_DELETE)
                dialog.setDialogResultListener(object : LifeCheckCustomDialogFragment.DialogResultListener {
                    override fun onConfirm() {
                        deleteMealData(mealId)
                    }
                })
                dialog.show(parentFragmentManager, "LifeCheckCustomDialogFragment")
            }
        }
    }

    private fun collectMealData(): LifeCheckMealDataRequest {
        return binding.run {
            LifeCheckMealDataRequest(
                name = etLifecheckMenuModifyName.text.toString(),
                amount = etLifecheckMenuModifyCapacity.text.toString().toInt(),
                amountPerServe = etLifecheckMenuModifyStandard.text.toString().toInt(),
                caloriePerServe = etLifecheckMenuModifyKcal.text.toString().toInt(),
                carbsPerServe = etLifecheckMenuModifyCarbohydrate.text.toString().toInt(),
                proteinPerServe = etLifecheckMenuModifyProtein.text.toString().toInt(),
                fatPerServe = etLifecheckMenuModifyFat.text.toString().toInt(),
                intakeUnit = actvLifecheckMenuModifyCapacityUnit.text.toString()
            )
        }
    }

    private fun observeUpdateResult() {
        viewModel.mealDataUpdateResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Toast.makeText(context, "성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }

                    is ApiResult.Error -> {
                        Toast.makeText(context, "오류 발생: ${result.description}", Toast.LENGTH_SHORT).show()
                    }

                    is ApiResult.Exception -> {
                        Toast.makeText(context, "예외 발생: ${result.e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun setupCapacityUnitDropdown() {
        val items = resources.getStringArray(R.array.lifecheck_capacity_unit)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
        binding.actvLifecheckMenuModifyCapacityUnit.setAdapter(adapter)

        binding.actvLifecheckMenuModifyCapacityUnit.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            if (selectedItem == "직접입력") {
                enableDirectInputForCapacityUnit()
            }
        }
    }

    private fun enableDirectInputForCapacityUnit() {
        binding.actvLifecheckMenuModifyCapacityUnit.apply {
            inputType = InputType.TYPE_CLASS_TEXT
            text = null
            hint = "단위 입력"
        }
    }

    private fun addTextWatchers() {
        binding.etLifecheckMenuModifyName.addTextChangedListener(createTextWatcher { validateName() })
        binding.etLifecheckMenuModifyCapacity.addTextChangedListener(createTextWatcher { validateCapacity() })
        binding.actvLifecheckMenuModifyCapacityUnit.addTextChangedListener(createTextWatcher { validateCapacityUnit() })
        binding.etLifecheckMenuModifyStandard.addTextChangedListener(createTextWatcher { validateStandard() })
        binding.etLifecheckMenuModifyKcal.addTextChangedListener(createTextWatcher { validateKcal() })
        binding.etLifecheckMenuModifyCarbohydrate.addTextChangedListener(createTextWatcher { validateCarbs() })
        binding.etLifecheckMenuModifyProtein.addTextChangedListener(createTextWatcher { validateProtein() })
        binding.etLifecheckMenuModifyFat.addTextChangedListener(createTextWatcher { validateFat() })
    }

    private fun createTextWatcher(validationFunction: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validationFunction()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun validateName(): Boolean {
        val name = binding.etLifecheckMenuModifyName.text.toString()
        return if (name.isBlank()) {
            setErrorBackground(binding.etLifecheckMenuModifyName, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuModifyName, false)
            true
        }
    }

    private fun validateCapacity(): Boolean {
        val capacity = binding.etLifecheckMenuModifyCapacity.text.toString()
        return if (capacity.isBlank() || !capacity.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuModifyCapacity, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuModifyCapacity, false)
            true
        }
    }

    private fun validateCapacityUnit(): Boolean {
        val capacityUnit = binding.actvLifecheckMenuModifyCapacityUnit.text.toString()
        return if (capacityUnit.isBlank()) {
            setErrorBackground(binding.actvLifecheckMenuModifyCapacityUnit, true)
            false
        } else {
            setErrorBackground(binding.actvLifecheckMenuModifyCapacityUnit, false)
            true
        }
    }

    private fun validateStandard(): Boolean {
        val standard = binding.etLifecheckMenuModifyStandard.text.toString()
        return if (standard.isBlank() || !standard.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuModifyStandard, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuModifyStandard, false)
            true
        }
    }

    private fun validateKcal(): Boolean {
        val kcal = binding.etLifecheckMenuModifyKcal.text.toString()
        return if (kcal.isBlank() || !kcal.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuModifyKcal, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuModifyKcal, false)
            true
        }
    }

    private fun validateCarbs(): Boolean {
        val carbs = binding.etLifecheckMenuModifyCarbohydrate.text.toString()
        return if (carbs.isBlank() || !carbs.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuModifyCarbohydrate, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuModifyCarbohydrate, false)
            true
        }
    }

    private fun validateProtein(): Boolean {
        val protein = binding.etLifecheckMenuModifyProtein.text.toString()
        return if (protein.isBlank() || !protein.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuModifyProtein, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuModifyProtein, false)
            true
        }
    }

    private fun validateFat(): Boolean {
        val fat = binding.etLifecheckMenuModifyFat.text.toString()
        return if (fat.isBlank() || !fat.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuModifyFat, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuModifyFat, false)
            true
        }
    }

    private fun setErrorBackground(view: View, hasError: Boolean) {
        val backgroundResource = if (hasError) {
            R.drawable.lifecheck_rect_gray1_no
        } else {
            R.drawable.lifecheck_rect_gray1_gray3
        }
        view.background = ContextCompat.getDrawable(requireContext(), backgroundResource)
    }

    private fun isValidInput(): Boolean {

        return validateName() && validateCapacity() && validateCapacityUnit() && validateStandard() &&
                validateKcal() && validateCarbs() && validateProtein() && validateFat()
    }

    private fun setupToolbar() {
        binding.toolbarLifecheckMenuModify.run {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun deleteMealData(mealId: Long) {
        viewModel.deleteMealData(mealId)
        viewModel.mealDataDeleteResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Toast.makeText(context, "메뉴가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    is ApiResult.Error -> {
                        Toast.makeText(context, "오류 발생: ${result.description}", Toast.LENGTH_SHORT).show()
                    }
                    is ApiResult.Exception -> {
                        Toast.makeText(context, "예외 발생: ${result.e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}