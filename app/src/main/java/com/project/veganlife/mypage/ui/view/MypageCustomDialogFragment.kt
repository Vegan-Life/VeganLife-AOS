package com.project.veganlife.mypage.ui.view

import android.content.Context
import android.graphics.Insets
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.veganlife.MainActivity
import com.project.veganlife.R
import com.project.veganlife.databinding.ActivityMainBinding
import com.project.veganlife.databinding.DialogMypageLogoutBinding
import com.project.veganlife.databinding.DialogMypageWithdrawalBinding
import com.project.veganlife.mypage.ui.viewmodel.MypageLogoutWithdrawalViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageCustomDialogFragment(val type: String) : DialogFragment() {
    private var _logoutBinding: DialogMypageLogoutBinding? = null
    private val logoutBinding get() = _logoutBinding!!

    private var _withdrawalBinding: DialogMypageWithdrawalBinding? = null
    private val withdrawalBinding get() = _withdrawalBinding!!

    private lateinit var activityBinding: ActivityMainBinding

    private val viewModel: MypageLogoutWithdrawalViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return when (type) {
            "로그아웃" -> {
                _logoutBinding = DialogMypageLogoutBinding.inflate(inflater, container, false)
                logoutBinding.root
            }

            else -> {
                _withdrawalBinding =
                    DialogMypageWithdrawalBinding.inflate(inflater, container, false)
                withdrawalBinding.root
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawableResource(R.color.gray2)
        when (type) {
            "로그아웃" -> {
                logoutBinding.apply {
                    dialog?.setContentView(logoutBinding.root)
                    btnMypageCancel.setOnClickListener {
                        dismiss()
                    }

                    btnMypageConfirm.setOnClickListener {
                        viewModel.doUserLogout()
                        dismiss()
                        bottomNavigationInitialization()
                        findNavController().navigate(R.id.action_mypageHomeFragment_to_loginFragment)

                    }
                }
            }

            else -> {
                withdrawalBinding.apply {

                    dialog?.setContentView(withdrawalBinding.root)

                    btnMypageCancel.setOnClickListener {
                        dismiss()
                    }

                    btnMypageConfirm.setOnClickListener {
                        viewModel.deleteWithDrawal()
                        dismiss()
                        bottomNavigationInitialization()
                        findNavController().navigate(R.id.action_mypageHomeFragment_to_mypageCompletedWithdrawalFragment)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        val params: ViewGroup.LayoutParams? = window?.attributes
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()

        // 현재 화면의 크기를 가져옴
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets: Insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            size.x = windowMetrics.bounds.width() - insets.left - insets.right
            size.y = windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getSize(size)
        }

        // 너비를 화면 너비의 90%로 설정
        params?.width = (size.x * 0.9).toInt()
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window?.attributes = params as WindowManager.LayoutParams
    }


    override fun onDestroyView() {
        super.onDestroyView()
        when (type) {
            "로그아웃" -> _logoutBinding = null
            else -> _withdrawalBinding = null
        }
    }

    fun bottomNavigationInitialization() {
        // MainActivity의 resetBottomNavigationToHome 메서드를 호출합니다.
        (activity as? MainActivity)?.resetBottomNavigationToHome()

        // 로그인 프래그먼트로 이동합니다.
        findNavController().popBackStack() // 백스택을 비워줍니다.
    }
}