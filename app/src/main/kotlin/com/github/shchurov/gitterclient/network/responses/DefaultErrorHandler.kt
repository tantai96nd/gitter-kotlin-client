package com.github.shchurov.gitterclient.network.responses

import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.network.GitterApi
import com.github.shchurov.gitterclient.utils.getString
import com.github.shchurov.gitterclient.utils.showToast
import retrofit.HttpException
import java.io.IOException

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
        } catch (e: IOException) {
            ApiError()
        }
        showToast(apiError.description ?: getString(R.string.unexpected_error_from_web))
    }

}