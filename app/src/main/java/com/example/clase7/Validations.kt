package com.example.clase7

import android.util.Patterns

// Valida email y retorna Pair<Boolean, Int?> donde Int? es el ID del string de error
fun validateEmail(value: String): Pair<Boolean, Int?> {
    return when {
        value.isEmpty() -> Pair(false, R.string.login_screen_validation_email_empty)
        !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> Pair(false, R.string.login_screen_validation_email_invalid)
        else -> Pair(true, null)
    }
}

// Valida contraseña y retorna Pair<Boolean, Int?> donde Int? es el ID del string de error
fun validatePassword(value: String): Pair<Boolean, Int?> {
    return when {
        value.isEmpty() -> Pair(false, R.string.login_screen_validation_password_empty)
        value.length < 6 -> Pair(false, R.string.login_screen_validation_password_short)
        else -> Pair(true, null)
    }
}

// Función combinada opcional para validar email y contraseña juntos
fun validateCredentials(email: String, password: String): Pair<Boolean, List<Int>> {
    val errors = mutableListOf<Int>()
    val emailValidation = validateEmail(email)
    val passwordValidation = validatePassword(password)

    if (!emailValidation.first) emailValidation.second?.let { errors.add(it) }
    if (!passwordValidation.first) passwordValidation.second?.let { errors.add(it) }

    return Pair(errors.isEmpty(), errors)
}

fun validateName(name: String): Pair<Boolean, Int?> {
    return if (name.isBlank()) Pair(false, R.string.create_profile_screen_habits_error) else Pair(true, null)
}

fun validateAge(age: String): Pair<Boolean, Int?> {
    val ageInt = age.toIntOrNull()
    return if (age.isBlank() || ageInt == null || ageInt <= 0) Pair(false, R.string.create_profile_screen_habits_error) else Pair(true, null)
}

fun validateHabits(selectedHabits: List<String>): Pair<Boolean, Int?> {
    return if (selectedHabits.isEmpty()) Pair(false, R.string.create_profile_screen_habits_error) else Pair(true, null)
}

fun validateHours(hours: String): Pair<Boolean, Int?> {
    return if (hours.isBlank()) {
        Pair(false, R.string.create_profile_screen_hours_required) // String que debes agregar
    } else {
        val value = hours.toIntOrNull()
        if (value == null || value <= 0) Pair(false, R.string.create_profile_screen_hours_invalid)
        else Pair(true, null)
    }
}

fun validateHabitName(name: String): Pair<Boolean, Int?> {
    return if (name.isBlank()) false to R.string.error_habit_name else true to null
}

fun validateHabitHours(hours: String): Pair<Boolean, Int?> {
    val h = hours.toIntOrNull()
    return if (h == null || h <= 0) false to R.string.error_habit_hours else true to null
}

