package com.project.veganlife.community.data.model

import android.net.Uri

data class ImageItem(
    val uri: Uri? = null,  // 새로 추가된 이미지
    val url: String? = null // 기존 등록된 이미지
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageItem) return false

        return uri == other.uri && url == other.url
    }

    override fun hashCode(): Int {
        return uri.hashCode() * 31 + (url?.hashCode() ?: 0)
    }
}
