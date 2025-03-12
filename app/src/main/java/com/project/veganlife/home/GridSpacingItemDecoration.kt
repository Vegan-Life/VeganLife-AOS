package com.project.veganlife.home

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int = 2, // 그리드의 열(column) 수
    private val spacing: Int, // 아이템 간의 간격 (픽셀 단위)
    private val includeEdge: Boolean // 가장자리 간격 포함 여부
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // 아이템의 위치
        val column = position % spanCount // 아이템이 속한 열(column)

        if (includeEdge) {
            // 가장자리 간격 포함
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position < spanCount) { // 첫 번째 행인 경우 위쪽 간격 추가
                outRect.top = spacing
            }
            outRect.bottom = spacing // 아래쪽 간격 추가
        } else {
            // 가장자리 간격 제외 (가운데 간격만 적용)
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing // 첫 번째 행이 아닌 경우 위쪽 간격 추가
            }
        }
    }
}

// dp를 픽셀로 변환하는 확장 함수
fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}