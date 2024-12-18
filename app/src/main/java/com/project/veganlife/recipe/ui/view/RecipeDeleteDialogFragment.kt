package com.project.veganlife.recipe.ui.view

import android.content.Context
import android.graphics.Insets
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.project.veganlife.R
import com.project.veganlife.databinding.DialogRecipeDeleteBinding
import com.project.veganlife.recipe.ui.viewmodel.RecipeViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeDeleteDialogFragment(id: Long) : DialogFragment() {
    private var _binding: DialogRecipeDeleteBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: RecipeViewmodel by viewModels()
    private val recipeId = id

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogRecipeDeleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawableResource(R.color.black_30)

        binding.apply {
            btnRecipeCancel.setOnClickListener { dismiss() }
            btnRecipeConfirm.setOnClickListener {
                viewmodel.deleteRecipe(recipeId)
            }
        }

        lifecycleScope.launch {
            viewmodel.recipeDeleteResponse.observe(viewLifecycleOwner) { response ->
                if (response == "레시피 삭제") {
                    dismiss()
                    findNavController().apply {
                        previousBackStackEntry?.savedStateHandle?.set("recipe_updated", true)
                        navigate(
                            R.id.action_recipeDetailInfoFragment_to_recipeHomeFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.recipeDetailInfoFragment, true)
                                .build()
                        )
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
        _binding = null
    }
}
