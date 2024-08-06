package com.dixon.app.kmmsample.core.base

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

val ktorClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
    install(HttpCookies) {
        storage = AcceptAllCookiesStorage()
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.INFO
    }
    expectSuccess = true
}

private const val KTOR_TAG = "KtorSafeCall"

// append try-catch
suspend fun <T> safeCall(
    client: HttpClient = ktorClient,
    call: suspend HttpClient.() -> T
): Result<T> {
    return try {
        val result = call(client)
        com.dixon.app.kmmsample.core.base.Logger.i(KTOR_TAG) { "Request succeed: $result" }
        Result(data = result)
    } catch (e: ConnectTimeoutException) {
        com.dixon.app.kmmsample.core.base.Logger.i(KTOR_TAG) { "Connection timed out: ${e.message}" }
        Result(exception = e)
    } catch (e: HttpRequestTimeoutException) {
        com.dixon.app.kmmsample.core.base.Logger.i(KTOR_TAG) { "Request timed out: ${e.message}" }
        Result(exception = e)
    } catch (e: ClientRequestException) {
        // 4xx responses
        com.dixon.app.kmmsample.core.base.Logger.i(KTOR_TAG) { "Client request error: ${e.response.status.description}" }
        Result(exception = e)
    } catch (e: ServerResponseException) {
        // 5xx responses
        com.dixon.app.kmmsample.core.base.Logger.i(KTOR_TAG) { "Server response error: ${e.response.status.description}" }
        Result(exception = e)
    } catch (e: Exception) {
        com.dixon.app.kmmsample.core.base.Logger.i(KTOR_TAG) { "An unexpected error occurred: ${e.message}" }
        Result(exception = e)
    }
}

suspend inline fun <reified T> apiGet(
    source: String
): Result<T> =
    safeCall {
        get(source).body<T>()
    }

data class Result<T>(
    val data: T? = null,
    val exception: Throwable? = null
)

fun <T> Result<T>.isSucceed() = data != null

fun <T> Result<T>.isFailed() = exception != null

inline fun <T> Result<T>.dispose(
    onSucceed: (T) -> Unit,
    onFailed: (Throwable) -> Unit
) {
    if (data != null) {
        onSucceed.invoke(data)
    } else {
        onFailed.invoke(exception ?: RuntimeException("Unknown Exception"))
    }
}

inline fun <T> Result<T>.disposeSucceed(
    onSucceed: (T) -> Unit,
) = this.apply {
    if (data != null) {
        onSucceed.invoke(data)
    }
}

inline fun <T> Result<T>.disposeFailed(
    onFailed: (Throwable) -> Unit,
) = this.apply {
    if (exception != null) {
        onFailed.invoke(exception)
    }
}
