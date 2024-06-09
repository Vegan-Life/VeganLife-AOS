package com.project.veganlife.data.model

data class PagingPageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: PagingSort,
    val unpaged: Boolean
)