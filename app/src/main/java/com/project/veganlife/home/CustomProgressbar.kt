package com.project.veganlife.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import com.project.veganlife.databinding.LayoutHomeGredientProgressbarBinding

class CustomProgressbar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attributeSet, defStyleAttr) {

    // 임시 max값이고, 나중에 setMax 에서 값을 변경해준다
    var kcalMax = 100
    var currentkcal = 0
        private set

    val binding: LayoutHomeGredientProgressbarBinding =
        LayoutHomeGredientProgressbarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        // db에서 받아온 후 max 값 설정해야함
        binding.pbHomeProgressbar.max = kcalMax
        setTextView(0, binding.tvHomeNowKcal)
        setTextView(kcalMax, binding.tvHomeTotalKcal)
//        setUiOfProgressBar()
    }

    fun setCurrentKcal(kcal: Int) {
        // kcal가 20% 이상일 때 visible로 변경
        currentkcal = kcal
        setTextView(currentkcal, binding.tvHomeNowKcal)
        binding.pbHomeProgressbar.progress = currentkcal

        val leftMargine = getSizeProgress() * currentkcal / kcalMax

        binding.tvHomeNowKcal.updateLayoutParams<MarginLayoutParams> {
            this.marginStart = leftMargine
        }
    }

    fun setUiOfProgressBar() {
        binding.tvHomeNowKcal.doOnLayout {
            it.width
            binding.pbHomeProgressbar.updateLayoutParams<MarginLayoutParams> {
                this.marginStart = it.width
            }
        }
    }

    fun getSizeProgress(): Int {
        return binding.pbHomeProgressbar.width - binding.pbHomeProgressbar.paddingLeft - binding.pbHomeProgressbar.paddingRight
    }

    // db에서 데이터 가져온 후 max 값 계산
    fun setMaxKcal(kcal: Int) {
        kcalMax = kcal
    }

    fun setTextView(progress: Int, textview: TextView) {
        textview.setText("${progress} kcal")
    }
}