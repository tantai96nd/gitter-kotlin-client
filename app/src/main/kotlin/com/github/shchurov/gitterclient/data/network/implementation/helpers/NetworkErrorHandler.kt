package com.github.shchurov.gitterclient.data.network.implementation.helpers

import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.data.network.responses.ErrorResponse
import com.github.shchurov.gitterclient.utils.getString
import com.github.shchurov.gitterclient.utils.showToast
import com.squareup.okhttp.ResponseBody
import retrofit.Converter
import retrofit.HttpException
import java.net.UnknownHostException

object NetworkErrorHandler {

    private lateinit var converter: Converter<ResponseBody, ErrorResponse>

    @Suppress("UNCHECKED_CAST")
    fun setConverterFactory(factory: Converter.Factory) {
        converter = factory.fromResponseBody(ErrorResponse::class.java, null) as Converter<ResponseBody, ErrorResponse>
    }

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

    private fun extractApiError(exception: HttpException): ErrorResponse {
        return try {
            converter.convert(exception.response().errorBody())
        } catch (e: Exception) {
            ErrorResponse()
        }
    }

}