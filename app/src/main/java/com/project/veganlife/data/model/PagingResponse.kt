package com.project.veganlife.data.model

data class PagingResponse<T>(
    val content: List<T>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: PagingPageable,
    val size: Int,
    val sort: PagingSort,
    val totalElements: Int,
    val totalPages: Int
)
