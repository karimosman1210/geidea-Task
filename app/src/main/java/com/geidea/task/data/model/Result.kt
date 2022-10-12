package com.geidea.task.data.model

data class Result<out T>(
    val status: Status,
    val data: T?,
    val statusCode: Int?,
    val message: String,
) {

    companion object {

        fun <T> loading(message: String): Result<T> =
            Result(
                status = Status.LOADING,
                data = null,
                statusCode = null,
                message = message,
            )

        fun <T> complete(data: T, statusCode: Int, message: String): Result<T> =
            Result(
                status = Status.COMPLETE,
                data = data,
                statusCode = statusCode,
                message = message,
            )

        fun <T> error(
            statusCode: Int?,
            message: String
        ): Result<T> =
            Result(
                status = Status.ERROR,
                data = null,
                statusCode = statusCode,
                message = message,
            )

    }
}