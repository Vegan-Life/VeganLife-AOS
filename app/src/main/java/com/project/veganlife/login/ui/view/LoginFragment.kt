package com.project.veganlife.login.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentLoginBinding
import com.project.veganlife.login.ui.view.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoginKakao.setOnClickListener {
            login("kakao")
        }

        binding.btnLoginNaver.setOnClickListener {
            login("naver")
        }

        loginAfterMoveFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginAfterMoveFragment() {
        loginViewModel.loginResponse.observe(viewLifecycleOwner) { loginResponse ->
            if (loginResponse != null) {
                findNavController().navigate(R.id.action_loginFragment_to_community_graph)
                //TODO: 로그인 다 만들어지면 다시 살리기
                /*if (loginResponse.hasAdditionalInfo == true) {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    findNavController().navigate(R.id.action_loginFragment_to_signupVeganTypeFragment)
                }*/
            }
        }
    }

    private fun login(provider: String) {
        context?.let { loginViewModel.login(provider, it) }
    }
}