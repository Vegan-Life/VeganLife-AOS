package com.project.veganlife.lifecheck.ui.view

import android.content.Context
import android.graphics.Insets
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.project.veganlife.databinding.DialogLifeCheckDietSelectBinding

class LifeCheckDietSelectDialogFragment : DialogFragment() {

    private var _binding: DialogLifeCheckDietSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLifeCheckDietSelectBinding.inflate(inflater, container, false)
        return binding.root
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}