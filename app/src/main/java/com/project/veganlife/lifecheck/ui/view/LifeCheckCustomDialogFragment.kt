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
import androidx.navigation.fragment.findNavController
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentLifeCheckCustomDialogBinding


class LifeCheckCustomDialogFragment : DialogFragment() {

    private var _binding: FragmentLifeCheckCustomDialogBinding? = null
    private val binding get() = _binding!!

    private var dialogResultListener: DialogResultListener? = null

    interface DialogResultListener {
        fun onConfirm()
    }

    fun setDialogResultListener(listener: DialogResultListener) {
        this.dialogResultListener = listener
    }

    companion object {
        private const val ARG_MEAL_ID = "meal_id"
        private const val ARG_ACTION_MODE = "action_mode"
        const val MODE_DELETE = "delete"
        const val MODE_MODIFY = "modify"

        fun newInstance(mealId: Long, actionMode: String): LifeCheckCustomDialogFragment {
            val fragment = LifeCheckCustomDialogFragment()
            val args = Bundle()
            args.putLong(ARG_MEAL_ID, mealId)
            args.putString(ARG_ACTION_MODE, actionMode)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckCustomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mealId = arguments?.getLong(ARG_MEAL_ID) ?: return
        val actionMode = arguments?.getString(ARG_ACTION_MODE) ?: MODE_MODIFY

        when (actionMode) {
            MODE_DELETE -> setupDeleteMode(mealId)
            MODE_MODIFY -> setupModifyMode(mealId)
        }
    }

    private fun setupDeleteMode(mealId: Long) {
        binding.run {
            tvLifecheckTitle.text = getString(R.string.lifecheck_menu_delete)
            tvLifecheckContent.text = getString(R.string.lifecheck_dialog_menu_delete_content)

            btnLifecheckCancel.setOnClickListener {
                dismiss()
            }

            btnLifecheckConfirm.setOnClickListener {
                dialogResultListener?.onConfirm()
                dismiss()
            }
        }
    }

    private fun setupModifyMode(mealId: Long) {
        binding.run {
            tvLifecheckTitle.text = getString(R.string.lifecheck_menu_modify_toolbar_title)
            tvLifecheckContent.text = getString(R.string.lifecheck_dialog_menu_modify_content)

            btnLifecheckCancel.setOnClickListener {
                dismiss()
            }

            btnLifecheckConfirm.setOnClickListener {
                val bundle = Bundle().apply {
                    putLong("mealId", mealId)
                }
                findNavController().navigate(
                    R.id.action_lifeCheckMenuSearchFragment_to_lifeCheckMenuModifyFragment,
                    bundle
                )
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