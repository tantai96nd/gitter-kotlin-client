package com.github.shchurov.gitterclient.network

import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.network.responses.ApiError
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
        val converter = GitterApi.getConverter(ApiError::class.java)
        val apiError: ApiError = try {
            converter.convert(exception.response().errorBody())
        } catch (e: Exception) {
            ApiError()
        }
        showToast(apiError.description ?: getString(R.string.unexpected_error_from_internet))
    }

}