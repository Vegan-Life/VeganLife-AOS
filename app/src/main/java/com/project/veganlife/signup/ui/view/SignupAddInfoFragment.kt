package com.project.veganlife.signup.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.veganlife.R
import com.project.veganlife.data.model.UiState
import com.project.veganlife.databinding.FragmentSignupAddInfoBinding
import com.project.veganlife.signup.ui.viewmodel.SignupAddInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupAddInfoFragment : Fragment() {
    private var _binding: FragmentSignupAddInfoBinding? = null
    private val binding get() = _binding!!

    private val args: SignupAddInfoFragmentArgs by navArgs()
    private val viewModel: SignupAddInfoViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupAddInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // SignupveganTypeFragment에서 넘긴 vegan Type
        initVeganType()
        // 툴바 설정
        setToolbarListener()
        setupInputListeners()
        setupGenderButtons()
        setupNextButton()
        observeStates()

    }

    private fun initVeganType() {
        viewModel.setVeganType(args.veganType)
    }

    private fun setupInputListeners() {
        binding.apply {
            // 닉네임 입력 리스너
            tietSignupNickname.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.updateInfo { copy(nickname = s.toString()) }
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })

            // 출생연도 입력 리스너
            tietSignupAge.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.toString()?.toIntOrNull()?.let { year ->
                        viewModel.updateInfo { copy(birthYear = year) }
                    }
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })

            // 키 입력 리스너 (height)
            tietSignupHeight.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.toString()?.toIntOrNull()?.let { height ->
                        viewModel.updateInfo { copy(height = height) }
                    }
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })

            // 몸무게 입력 리스너 (weight)
            tietSignupWeight.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.toString()?.toIntOrNull()?.let { weight ->
                        viewModel.updateInfo { copy(weight = weight) }
                    }
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
        }
    }

    private fun setupGenderButtons() {
        binding.apply {
            btnSignupMale.setOnClickListener {
                viewModel.updateInfo { copy(gender = "M") }
                updateGenderButtonUI(isMaleSelected = true)
            }

            btnSignupFemale.setOnClickListener {
                viewModel.updateInfo { copy(gender = "F") }
                updateGenderButtonUI(isMaleSelected = false)
            }
        }
    }

    private fun updateGenderButtonUI(isMaleSelected: Boolean) {
        binding.apply {
            val maleColor = if (isMaleSelected) R.drawable.signup_vegan_type_selected else R.drawable.signup_vegan_type
            val maleTextColor = if(isMaleSelected) R.color.white else R.color.gray3
            val femaleColor = if (!isMaleSelected) R.drawable.signup_vegan_type_selected else R.drawable.signup_vegan_type
            val femaleTextColor = if(!isMaleSelected) R.color.white else R.color.gray3

            btnSignupMale.setBackgroundResource(maleColor)
            btnSignupMale.setTextColor(ContextCompat.getColor(requireContext(), maleTextColor))
            btnSignupFemale.setBackgroundResource(femaleColor)
            btnSignupFemale.setTextColor(ContextCompat.getColor(requireContext(), femaleTextColor))
        }
    }

    private fun observeStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAllValid.collect { isValid ->
                    updateNextButtonUI(isValid)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signupState.collect { state ->
                    handleSignupState(state)
                }
            }
        }
    }

    private fun updateNextButtonUI(shouldEnable: Boolean) {
        binding.btnSignupNext.apply {
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (shouldEnable) R.color.base3 else R.color.gray3
                )
            )
            isEnabled = shouldEnable
        }
    }

    private fun setupNextButton() {
        binding.btnSignupNext.setOnClickListener {
            viewModel.submitSignup()
        }
    }

    private fun handleSignupState(state: UiState) {
        when (state) {
            is UiState.Success -> {
                navigateToComplete()
            }
            is UiState.Error -> {
                state.message?.let { makeToast(it) }
            }
            UiState.Loading -> {
                // 로딩 UI 처리
            }
            UiState.Idle -> Unit
        }
    }

    private fun navigateToComplete() {
        findNavController().navigate(R.id.action_signupAddInfoFragment_to_signupCompleteFragment)
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

    private fun setToolbarListener() {
        binding.toolbarSignupToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}