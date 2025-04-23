package com.example.apphoctap.utils

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidPhone(sdt: String): Boolean {
        return sdt.matches(Regex("^0[0-9]{9}$"))
    }
}
