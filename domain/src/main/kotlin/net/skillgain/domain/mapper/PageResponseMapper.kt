package net.skillgain.domain.mapper

import net.skillgain.domain.model.PagedResponse
import org.springframework.data.domain.Page

fun <T> Page<T>.toPagedResponse() = PagedResponse(
    content = content,
    page = number,
    size = size,
    totalElements = totalElements,
    totalPages = totalPages
)
