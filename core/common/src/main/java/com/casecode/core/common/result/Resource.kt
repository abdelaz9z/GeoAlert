package com.casecode.core.common.result

sealed interface Resource<out T> {

    data class Success<T>(val data: T) : Resource<T>

    data class Error<T>(val message: Any?) : Resource<T>

    data object Loading : Resource<Nothing>

    data class Empty<T>(val emptyType: EmptyType? = null, val message: Any? = null) : Resource<T> {
        val data: T? = null
    }

    companion object {
        inline fun <reified T> success(data: T): Resource<T> = Success(data)

        inline fun <reified T> error(message: Any?): Resource<T> = Error(message)

        fun <T> loading(): Resource<T> = Loading

        inline fun <reified T> empty(
            emptyType: EmptyType? = null,
            message: Any? = null,
        ): Resource<T> = Empty(emptyType, message)
    }
}

enum class EmptyType {
    DATA,
    Validation
}