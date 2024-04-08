package com.project.veganlife.signup.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentSignupVeganTypeBinding
import com.project.veganlife.signup.ui.adapter.SignupAdapter
import com.project.veganlife.signup.ui.viewmodel.SignupVeganTypeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupVeganTypeFragment : Fragment() {
    private var _binding: FragmentSignupVeganTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var signupAdapter: SignupAdapter
    private val signupVeganTypeViewModel: SignupVeganTypeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupVeganTypeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        signupAdapter = SignupAdapter(signupVeganTypeViewModel)

        setToolbarListener()
        initSignupRecyclerview()
        setVeganTypeList()

        observeViewModel()

        binding.btnSignupNext.setOnClickListener {
            moveToFragment()
        }
    }

    private fun initSignupRecyclerview() {
        binding.rvSignupVeganType.apply {
            adapter = signupAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setVeganTypeList() {
        signupVeganTypeViewModel.setVeganList()
        signupVeganTypeViewModel.veganTypeData.observe(viewLifecycleOwner) {
            signupAdapter.submitList(it)
        }
    }

    private fun observeViewModel() {
        signupVeganTypeViewModel.btnBackgroundColor.observe(viewLifecycleOwner) { backgroundColorResId ->
            binding.btnSignupNext.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    backgroundColorResId
                )
            )
        }
    }

    private fun moveToFragment() {
        val selectedVeganName = signupVeganTypeViewModel.veganTypeSelected.value
        if (!selectedVeganName.isNullOrEmpty()) {
            findNavController().navigate(R.id.action_signupVeganTypeFragment_to_signupAddInfoFragment)
        } else {
            makeToast("비건 타입을 선택해주세요")
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setToolbarListener() {
        binding.toolbarSignupToolbar.run {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        signupVeganTypeViewModel.clearSelectedVeganName()
    }
}