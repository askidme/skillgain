package net.skillgain.exception.domain

import org.springframework.http.HttpStatus

interface ErrorCode {

    fun title(): String
    fun detail(): String
    fun status(): HttpStatus
}