package com.hardik.movieapp.cmp.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

suspend fun <T> performNetworkFlow(networkCall: suspend () -> T): Flow<Result<T>> =
    flow {
        try {
            val responseStatus = networkCall.invoke()
            emit(Result.success(responseStatus))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.Default)