package com.example.test.utils

object EmailValidator {
    private val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    fun isEmailValid(email: String) = EMAIL_REGEX.toRegex().matches(email)
}