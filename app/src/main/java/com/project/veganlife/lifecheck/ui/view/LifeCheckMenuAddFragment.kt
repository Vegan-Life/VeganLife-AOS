package com.project.veganlife.lifecheck.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.databinding.FragmentLifeCheckMenuAddBinding
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LifeCheckMenuAddFragment : Fragment() {

    private var _binding: FragmentLifeCheckMenuAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifeCheckViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckMenuAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCapacityUnitDropdown()
        addTextWatchers()
        observeMealDataRegister()
        setupButton()
    }

    private fun observeMealDataRegister() {
        viewModel.mealDataRegister.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    // POST 요청 성공
                    Toast.makeText(context, "성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                is ApiResult.Error -> {
                    // POST 요청 실패 (클라이언트 오류)
                    Toast.makeText(context, "오류 발생: ${result.description}", Toast.LENGTH_SHORT)
                        .show()
                }

                is ApiResult.Exception -> {
                    // 예외 발생
                    Toast.makeText(context, "예외 발생: ${result.e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupButton() {
        binding.run {
            btnLifecheckMenuAddImport.setOnClickListener {
                if (isValidInput()) {
                    val mealData = collectMealData()
                    viewModel.registerMealData(mealData)
                }
            }
            btnLifecheckMenuAddCancel.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupCapacityUnitDropdown() {
        val items = resources.getStringArray(R.array.lifecheck_capacity_unit)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
        binding.actvLifecheckMenuAddCapacityUnit.setAdapter(adapter)

        binding.actvLifecheckMenuAddCapacityUnit.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            if (selectedItem == "직접입력") {
                enableDirectInputForCapacityUnit()
            }
        }
    }

    private fun enableDirectInputForCapacityUnit() {
        binding.actvLifecheckMenuAddCapacityUnit.apply {
            inputType = InputType.TYPE_CLASS_TEXT
            text = null
            hint = "단위 입력"
        }
    }

    private fun addTextWatchers() {
        binding.etLifecheckMenuAddName.addTextChangedListener(createTextWatcher { validateName() })
        binding.etLifecheckMenuAddCapacity.addTextChangedListener(createTextWatcher { validateCapacity() })
        binding.actvLifecheckMenuAddCapacityUnit.addTextChangedListener(createTextWatcher { validateCapacityUnit() })
        binding.etLifecheckMenuAddStandard.addTextChangedListener(createTextWatcher { validateStandard() })
        binding.etLifecheckMenuAddKcal.addTextChangedListener(createTextWatcher { validateKcal() })
        binding.etLifecheckMenuAddCarbohydrate.addTextChangedListener(createTextWatcher { validateCarbs() })
        binding.etLifecheckMenuAddProtein.addTextChangedListener(createTextWatcher { validateProtein() })
        binding.etLifecheckMenuAddFat.addTextChangedListener(createTextWatcher { validateFat() })
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
        val name = binding.etLifecheckMenuAddName.text.toString()
        return if (name.isBlank()) {
            setErrorBackground(binding.etLifecheckMenuAddName, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuAddName, false)
            true
        }
    }

    private fun validateCapacity(): Boolean {
        val capacity = binding.etLifecheckMenuAddCapacity.text.toString()
        return if (capacity.isBlank() || !capacity.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuAddCapacity, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuAddCapacity, false)
            true
        }
    }

    private fun validateCapacityUnit(): Boolean {
        val capacityUnit = binding.actvLifecheckMenuAddCapacityUnit.text.toString()
        return if (capacityUnit.isBlank()) {
            setErrorBackground(binding.actvLifecheckMenuAddCapacityUnit, true)
            false
        } else {
            setErrorBackground(binding.actvLifecheckMenuAddCapacityUnit, false)
            true
        }
    }

    private fun validateStandard(): Boolean {
        val standard = binding.etLifecheckMenuAddStandard.text.toString()
        return if (standard.isBlank() || !standard.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuAddStandard, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuAddStandard, false)
            true
        }
    }

    private fun validateKcal(): Boolean {
        val kcal = binding.etLifecheckMenuAddKcal.text.toString()
        return if (kcal.isBlank() || !kcal.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuAddKcal, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuAddKcal, false)
            true
        }
    }

    private fun validateCarbs(): Boolean {
        val carbs = binding.etLifecheckMenuAddCarbohydrate.text.toString()
        return if (carbs.isBlank() || !carbs.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuAddCarbohydrate, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuAddCarbohydrate, false)
            true
        }
    }

    private fun validateProtein(): Boolean {
        val protein = binding.etLifecheckMenuAddProtein.text.toString()
        return if (protein.isBlank() || !protein.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuAddProtein, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuAddProtein, false)
            true
        }
    }

    private fun validateFat(): Boolean {
        val fat = binding.etLifecheckMenuAddFat.text.toString()
        return if (fat.isBlank() || !fat.isDigitsOnly()) {
            setErrorBackground(binding.etLifecheckMenuAddFat, true)
            false
        } else {
            setErrorBackground(binding.etLifecheckMenuAddFat, false)
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

    private fun collectMealData(): LifeCheckMealDataRequest {
        val name = binding.etLifecheckMenuAddName.text.toString()
        val capacity = binding.etLifecheckMenuAddCapacity.text.toString().toInt()
        val capacityUnit = binding.actvLifecheckMenuAddCapacityUnit.text.toString()
        val standard = binding.etLifecheckMenuAddStandard.text.toString().toInt()
        val kcal = binding.etLifecheckMenuAddKcal.text.toString().toInt()
        val carbs = binding.etLifecheckMenuAddCarbohydrate.text.toString().toInt()
        val protein = binding.etLifecheckMenuAddProtein.text.toString().toInt()
        val fat = binding.etLifecheckMenuAddFat.text.toString().toInt()

        return LifeCheckMealDataRequest(
            name = name,
            amount = capacity,
            amountPerServe = standard,
            caloriePerServe = kcal,
            carbsPerServe = carbs,
            proteinPerServe = protein,
            fatPerServe = fat,
            intakeUnit = capacityUnit
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}