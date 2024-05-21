package com.project.veganlife.home

import android.content.Context
import android.util.AttributeSet
import android.util.Log
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

    var kcalMax = 100
        private set
    var currentkcal = 0
        private set

    var currentKcalLength = 0
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
        currentkcal = kcal
        binding.pbHomeProgressbar.progress = currentkcal
        var leftMargine = 0
        // 1 자리 까지 105 이지만 20%  -> 남자 4~500 , 여자 3~400
        // 2 자리 120
        // 3 자리 140
        // 4 자리 160
        when(currentKcalLength) {
            in 0..1 -> {
                leftMargine = getSizeProgress() * currentkcal  / kcalMax - 105
            }
            2 -> {
                leftMargine = getSizeProgress() * currentkcal  / kcalMax - 120
            }
            3 -> {
                leftMargine = getSizeProgress() * currentkcal  / kcalMax - 140
            }
            4 -> {
                leftMargine = getSizeProgress() * currentkcal  / kcalMax - 160
            }
        }

        Log.d("length",currentKcalLength.toString())
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

    fun setCurrentKcalLength(kcal: Int) {
        currentKcalLength = kcal.toString().length
    }

    fun setTextView(progress: Int, textview: TextView) {
        textview.setText("${progress} kcal")
    }
}