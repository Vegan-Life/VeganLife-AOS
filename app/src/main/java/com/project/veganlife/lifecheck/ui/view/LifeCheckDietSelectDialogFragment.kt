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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.project.veganlife.R
import com.project.veganlife.databinding.DialogLifeCheckDietSelectBinding
import com.project.veganlife.lifecheck.ui.viewmodel.LifeCheckViewModel

class LifeCheckDietSelectDialogFragment : DialogFragment() {

    private var _binding: DialogLifeCheckDietSelectBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifeCheckViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLifeCheckDietSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnLifecheckDietSelectDialogClose.setOnClickListener {
                dismiss()
            }

            rgLifecheckDietSelectDialog.setOnCheckedChangeListener { _, i ->
                val selectDietType =
                    when (i) {
                        R.id.rb_lifecheck_diet_select_dialog_breakfast -> getString(R.string.lifecheck_morning)
                        R.id.rb_lifecheck_diet_select_dialog_lunch -> getString(R.string.lifecheck_lunch)
                        R.id.rb_lifecheck_diet_select_dialog_dinner -> getString(R.string.lifecheck_dinner)
                        R.id.rb_lifecheck_diet_select_dialog_morning_snack -> getString(R.string.lifecheck_morning_snack)
                        R.id.rb_lifecheck_diet_select_dialog_afternoon_snack -> getString(R.string.lifecheck_afternoon_snack)
                        R.id.rb_lifecheck_diet_select_dialog_dinner_snack -> getString(R.string.lifecheck_dinner_snack)
                        else -> ""
                    }
                viewModel.setSelectedDietType(selectDietType)
                findNavController().navigate(R.id.action_lifeCheckHomeFragment_to_lifeCheckMenuSearchFragment)
                dismiss()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}