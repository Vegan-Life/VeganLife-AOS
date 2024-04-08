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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentSignupAddInfoBinding
import com.project.veganlife.signup.ui.viewmodel.SignupAddInfoViewModel
import com.project.veganlife.signup.ui.viewmodel.SignupVeganTypeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignupAddInfoFragment : Fragment() {
    private var _binding: FragmentSignupAddInfoBinding? = null
    private val binding get() = _binding!!

    private val signupVeganTypeViewModel: SignupVeganTypeViewModel by activityViewModels()
    private val signupAddInfoViewModel: SignupAddInfoViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupAddInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 툴바 설정
        setToolbarListener()

        // SignupVeganTypeFragment에서 비건 타입 입력 받기
        getVeganType()

        // 입력란 설정
        setNickname()
        setHeight()
        setWeight()
        setBirthyear()

        // 성별 버튼 색상
        setGenderButtonColor()

        // 전체 상태 체크 후 다음 버튼 색상 변경
        setNextButtonBackgorundColor()

        binding.apply {
            btnSignupFemale.setOnClickListener {
                signupAddInfoViewModel.apply {
                    onFemaleSelected()
                    isAllStateValid()
                }

            }

            btnSignupMale.setOnClickListener {
                signupAddInfoViewModel.apply {
                    onMaleSelected()
                    isAllStateValid()
                }
            }

            btnSignupNext.setOnClickListener {
                moveNextFragment()
            }
        }
    }

    private fun getVeganType() {
        signupAddInfoViewModel.setVeganType(signupVeganTypeViewModel.signupVeganTypeInfo.value.toString())
    }

    private fun setNickname() {
        binding.tietSignupNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val enteredText = p0.toString()
                signupAddInfoViewModel.apply {
                    setNickname(enteredText)
                    isAllStateValid()
                }
            }
        })
    }

    private fun setHeight() {
        binding.tietSignupHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                try {
                    val enteredText = p0.toString().toInt()
                    signupAddInfoViewModel.apply {
                        setHeight(enteredText)
                        isAllStateValid()
                    }
                } catch (e: NumberFormatException) {
                    makeToast("정수만 입력 가능 합니다")
                }
            }
        })
    }

    private fun setWeight() {
        binding.tietSignupWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                try {
                    val enteredText = p0.toString().toInt()
                    signupAddInfoViewModel.apply {
                        setWeight(enteredText)
                        isAllStateValid()
                    }
                } catch (e: NumberFormatException) {
                    makeToast("정수만 입력 가능 합니다")
                }
            }

        })
    }

    private fun setBirthyear() {
        binding.tietSignupAge.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                try {
                    val enteredText = p0.toString().toInt()
                    signupAddInfoViewModel.apply {
                        setBirthyear(enteredText)
                        isAllStateValid()
                    }
                } catch (e: NumberFormatException) {
                    makeToast("정수만 입력 가능 합니다")
                }
            }
        })

    }

    private fun setGenderButtonColor() {
        signupAddInfoViewModel.apply {
            femaleBackgroundColor.observe(viewLifecycleOwner) { backgroundColorResId ->
                binding.btnSignupFemale.setBackgroundResource(
                    backgroundColorResId
                )
            }

            femaleTextColor.observe(viewLifecycleOwner) { textColorResId ->
                binding.btnSignupFemale.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        textColorResId
                    )
                )
            }

            maleBackgroundColor.observe(viewLifecycleOwner) { backgroundColorResId ->
                binding.btnSignupMale.setBackgroundResource(
                    backgroundColorResId
                )
            }

            maleTextColor.observe(viewLifecycleOwner) { textColorResId ->
                binding.btnSignupMale.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        textColorResId
                    )
                )
            }
        }
    }

    private fun setNextButtonBackgorundColor() {
        signupAddInfoViewModel.apply {
            allStateCheck.observe(viewLifecycleOwner) { allState ->
                binding.btnSignupNext.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        setNextButtonBackgroundColor(allState)
                    )
                )
            }
        }
    }

    private fun moveNextFragment() {
        signupAddInfoViewModel.apply {
            setSignupRequest(allStateCheck.value)

            signupRespone.observe(viewLifecycleOwner) { response ->
                response?.let { handleSignupResponse(it) }
            }
        }
    }

    private fun handleSignupResponse(response: String) {
        signupAddInfoViewModel.apply {
            when (response) {
                nickname.value -> {
                    findNavController().navigate(R.id.action_signupAddInfoFragment_to_signupCompleteFragment)
                }

                "중복된 닉네임입니다.", "모든 정보를 올바르게 입력해주세요" -> {
                    makeToast(response)
                    setSignupResponse("")
                }
            }
        }
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
        binding.toolbarSignupToolbar.run {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}