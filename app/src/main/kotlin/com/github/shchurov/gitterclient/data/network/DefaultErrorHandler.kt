package com.github.shchurov.gitterclient.data.network

import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.data.network.responses.ApiError
import com.github.shchurov.gitterclient.data.network.retrofit.RetrofitManager
import com.github.shchurov.gitterclient.utils.getString
import com.github.shchurov.gitterclient.utils.showToast
import retrofit.HttpException
import java.net.UnknownHostException

object DefaultErrorHandler {

    fun handleError(exception: Throwable) {
        when (exception) {
            is HttpException -> handleHttpException(exception)
            is UnknownHostException -> showToast(R.string.check_your_connection)
        }
    }

    private fun handleHttpException(exception: HttpException) {
        val apiError = extractApiError(exception)
        showToast(apiError.description ?: getString(R.string.unexpected_error_from_internet))
    }

    private fun extractApiError(exception: HttpException): ApiError {
        val converter = RetrofitManager.getConverter(ApiError::class.java)
        return try {
            converter.convert(exception.response().errorBody())
        } catch (e: Exception) {
            ApiError()
        }
    }

}