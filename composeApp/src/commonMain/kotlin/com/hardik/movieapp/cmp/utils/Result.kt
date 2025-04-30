package com.hardik.movieapp.cmp.utils

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    /**
     * Returns true if this instance represents a successful outcome. In this case isFailure returns false.
     */
    val isSuccess get() = this is Success

    /**
     * Returns true if this instance represents a failed outcome. In this case isSuccess returns false.
     */
    val isFailure get() = !isSuccess

    companion object {
        fun <T> success(value: T): Result<T> = Success(value)

        fun <T> failure(exception: Throwable): Result<T> = Error(exception)

        inline fun <T> runCatching(block: () -> T) = try {
            success(block())
        } catch (t: Throwable) {
            failure(t)
        }
    }
}

/**
 * Returns the encapsulated value if this instance represents success or null if it is failure.
 * This function is a shorthand for getOrElse { null }
 * (see getOrElse) or fold(onSuccess = { it }, onFailure = { null })
 */
fun <T> Result<T>.getOrNull(): T? = when (this) {
    is Result.Success -> data
    is Result.Error -> null
}

/**
 * Returns the encapsulated Throwable exception if this instance represents failure or null if it
 * is success.
 * This function is a shorthand for fold(onSuccess = { null }, onFailure = { it }) (see fold)
 */
fun <T> Result<T>.exceptionOrNull(): Throwable? = when (this) {
    is Result.Success -> null
    is Result.Error -> exception
}

/**
 * Returns the encapsulated value if this instance represents success or throws the encapsulated
 * Throwable exception if it is failure.
 * This function is a shorthand for getOrElse { throw it } (see getOrElse).
 */
fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error -> throw exception
}

/**
 * Returns the encapsulated value if this instance represents success or the result of onFailure
 * function for the encapsulated Throwable exception if it is failure.
 */
inline fun <R, T : R> Result<T>.getOrElse(onFailure: (exception: Throwable) -> R) = when (this) {
    is Result.Success -> data
    is Result.Error -> onFailure(exception)
}

/**
 * Returns the result of onSuccess for the encapsulated value if this instance represents success
 * or the result of onFailure function for the encapsulated Throwable exception if it is failure.
 */
inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R,
) = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onFailure(exception)
}

/**
 * Returns the encapsulated result of the given transform function applied to the encapsulated
 * value if this instance represents success or the original encapsulated Throwable exception
 * if it is failure.
 */
inline fun <R, T> Result<T>.map(transform: (value: T) -> R) = when (this) {
    is Result.Success -> Result.success(transform(data))
    is Result.Error -> Result.failure(exception)
}

/**
 * Returns the encapsulated value if this instance represents success or the defaultValue
 * if it is failure.
 * This function is a shorthand for getOrElse { defaultValue } (see getOrElse).
 */
fun <R, T : R> Result<T>.getOrDefault(defaultValue: R) = when (this) {
    is Result.Success -> data
    is Result.Error -> defaultValue
}

/**
 * Returns the encapsulated result of the given transform function applied to the encapsulated
 * value if this instance represents success or the original encapsulated Throwable exception
 * if it is failure.
 *
 * This function catches any Throwable exception thrown by transform function and encapsulates
 * it as a failure. See map for an alternative that rethrows exceptions from transform function.
 */
inline fun <R, T> Result<T>.mapCatching(transform: (value: T) -> R) = when (this) {
    is Result.Success -> try {
        Result.success(transform(data))
    } catch (t: Throwable) {
        Result.Error(t)
    }
    is Result.Error -> Result.failure(exception)
}

inline fun <R, T : R> Result<T>.recover(transform: (exception: Throwable) -> R) = when (this) {
    is Result.Success -> this
    is Result.Error -> Result.success(transform(exception))
}

/**
 * Returns the encapsulated result of the given transform function applied to the encapsulated
 * Throwable exception if this instance represents failure or the original encapsulated value
 * if it is success.
 *
 * Note, that this function rethrows any Throwable exception thrown by transform function.
 * See recoverCatching for an alternative that encapsulates exceptions.
 */
inline fun <R, T : R> Result<T>.recoverCatching(transform: (exception: Throwable) -> R) =
    when (this) {
        is Result.Success -> this
        is Result.Error -> try {
            Result.success(transform(exception))
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

/**
 * Returns the encapsulated result of the given transform function applied to the encapsulated
 * Throwable exception if this instance represents failure or the original encapsulated value
 * if it is success.
 *
 * Note, that this function rethrows any Throwable exception thrown by transform function.
 * See recoverCatching for an alternative that encapsulates exceptions.
 */
inline infix fun <T> Result<T>.onFailure(action: (exception: Throwable) -> Unit): Result<T> {
    exceptionOrNull()?.let(action)
    return this
}

/**
 * Performs the given action on the encapsulated value if this instance represents success.
 * Returns the original Result unchanged.
 */
inline infix fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
    getOrNull()?.let(action)
    return this
}

inline infix fun <R, T> Result<T>.flatMap(transform: (value: T) -> Result<R>) = when (this) {
    is Result.Success -> transform(data)
    is Result.Error -> this
}
