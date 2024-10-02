package com.project.veganlife.utils

import android.util.Log
import com.project.veganlife.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.*
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import javax.inject.Inject

// SseClient.kt
class SseClient @Inject constructor(
    private val client: OkHttpClient,
) {
    private var eventSource: EventSource? = null

    // Notification 처리(SSE Response data class)는 v.1.1
    fun subscribeSse(onEvent: (String) -> Unit) {
        val request = Request.Builder().url("${BuildConfig.BASEURL}sse/subscribe").build()

        val eventSourceListener = object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                Log.d("SSE", "Connection opened")
            }

            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                Log.d("SSE", "New SSE event: $data")
                onEvent(data) // 이벤트 처리 콜백
            }

            override fun onClosed(eventSource: EventSource) {
                Log.d("SSE", "Connection closed")
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?
            ) {
                Log.e("SSE", "Error: ${t?.message}")
            }
        }

        eventSource =
            EventSources.createFactory(client).newEventSource(request, eventSourceListener)
    }

    fun close() {
        eventSource?.cancel()
    }
}
