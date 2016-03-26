package com.github.shchurov.gitterclient.data.subscribers

import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.data.network.model.ErrorResponse
import com.github.shchurov.gitterclient.utils.getString
import com.google.gson.Gson
import retrofit.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@Suppress("UNCHECKED_CAST")
object ErrorMessageGenerator {

    private val gson = Gson()

    fun generateErrorMessage(exception: Throwable): String {
        return when (exception) {
            is HttpException -> generateApiError(exception)
            is UnknownHostException, is SocketTimeoutException -> getConnectionError()
            else -> getUnexpectedError()
        }
    }

    private fun generateApiError(exception: HttpException) = try {
        val errorResponse = gson.fromJson(exception.response().errorBody().string(), ErrorResponse::class.java)
        getString(R.string.gitter_error, errorResponse.description)
    } catch (e: Exception) {
        getUnexpectedError()
    }

    private fun getConnectionError() = getString(R.string.check_your_connection)

    private fun getUnexpectedError() = getString(R.string.unexpected_error)

}