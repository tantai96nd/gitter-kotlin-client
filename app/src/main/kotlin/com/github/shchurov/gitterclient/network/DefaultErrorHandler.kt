package com.github.shchurov.gitterclient.network

import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.network.responses.ApiError
import com.github.shchurov.gitterclient.utils.getString
import com.github.shchurov.gitterclient.utils.showToast
import retrofit.HttpException

object DefaultErrorHandler {

    fun handleError(exception: Throwable) {
        when (exception) {
            is HttpException -> handleHttpException(exception)
        }
    }

    private fun handleHttpException(exception: HttpException) {
        val converter = GitterApi.getConverter(ApiError::class.java)
        val apiError: ApiError = try {
            converter.convert(exception.response().errorBody())
        } catch (e: Exception) {
            ApiError()
        }
        showToast(apiError.description ?: getString(R.string.unexpected_error_from_web))
    }

}