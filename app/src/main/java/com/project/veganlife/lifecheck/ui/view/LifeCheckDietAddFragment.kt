package com.project.veganlife.lifecheck.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.project.veganlife.R
import com.project.veganlife.databinding.FragmentLifeCheckDietAddBinding
import com.project.veganlife.databinding.LayoutLifecheckDietAddBinding

class LifeCheckDietAddFragment : Fragment() {

    private var _binding: FragmentLifeCheckDietAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeCheckDietAddBinding.inflate(inflater, container, false)

        setupClickListeners(inflater)
        setupDefaultButtonListeners()

        return binding.root
    }

    private fun setupClickListeners(inflater: LayoutInflater) {
        binding.clLifecheckDietAddDietPlus.setOnClickListener {
            addNewDietEntry(inflater)
        }
    }

    private fun addNewDietEntry(inflater: LayoutInflater) {
        val newDietBinding = LayoutLifecheckDietAddBinding.inflate(inflater, binding.root, false)
        var intakeValue = 1.0
        newDietBinding.tvLifecheckDietAddMenuIntake.text = "$intakeValue"

        newDietBinding.btnLifecheckDietAddMenuPlus.setOnClickListener {
            intakeValue = when (intakeValue) {
                0.25 -> {
                    newDietBinding.btnLifecheckDietAddMenuMinus.setBackgroundResource(R.drawable.lifecheck_minus)
                    0.5
                }
                0.5 -> 1.0
                else -> intakeValue + 1
            }
            newDietBinding.tvLifecheckDietAddMenuIntake.text = "$intakeValue"
        }

        newDietBinding.btnLifecheckDietAddMenuMinus.setOnClickListener {
            intakeValue = when (intakeValue) {
                0.25 -> {
                    // 뷰 제거
                    (binding.root.findViewById<LinearLayout>(R.id.root_layout)).removeView(newDietBinding.root)
                    return@setOnClickListener
                }
                0.5 -> {
                    newDietBinding.btnLifecheckDietAddMenuMinus.setImageDrawable(null)
                    newDietBinding.btnLifecheckDietAddMenuMinus.setBackgroundResource(R.drawable.lifecheck_delete)
                    0.25
                }
                1.0 -> {
                    newDietBinding.btnLifecheckDietAddMenuMinus.setBackgroundResource(R.drawable.lifecheck_minus)
                    0.5
                }
                else -> intakeValue - 1
            }
            newDietBinding.tvLifecheckDietAddMenuIntake.text = "$intakeValue"
        }

        val parentLayout = binding.root.findViewById<LinearLayout>(R.id.root_layout)
        val index = parentLayout.indexOfChild(binding.clLifecheckDietAddDietPlus)
        parentLayout.addView(newDietBinding.root, index)
    }

    private fun setupDefaultButtonListeners() {
        val parentLayout = binding.root.findViewById<LinearLayout>(R.id.root_layout)
        val defaultView = binding.includeLifecheckDietAdd
        val intakeTextView = defaultView.tvLifecheckDietAddMenuIntake
        val plusButton = defaultView.btnLifecheckDietAddMenuPlus
        val minusButton = defaultView.btnLifecheckDietAddMenuMinus

        var intakeValue = 1.0
        intakeTextView.text = "$intakeValue"

        plusButton.setOnClickListener {
            intakeValue = when (intakeValue) {
                0.25 -> {
                    minusButton.setBackgroundResource(R.drawable.lifecheck_minus)
                    0.5
                }
                0.5 -> 1.0
                else -> intakeValue + 1
            }
            intakeTextView.text = "$intakeValue"
        }
        minusButton.setOnClickListener {
            intakeValue = when (intakeValue) {
                0.25 -> {
                    // 값이 0.25일 때 기본 뷰 삭제
                    parentLayout.removeView(defaultView.root)
                    return@setOnClickListener
                }
                0.5 -> {
                    minusButton.setImageDrawable(null)
                    minusButton.setBackgroundResource(R.drawable.lifecheck_delete)
                    0.25
                }
                1.0 -> 0.5
                else -> intakeValue - 1
            }
            intakeTextView.text = "$intakeValue"
        }
    }

        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}