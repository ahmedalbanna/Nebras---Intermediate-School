package com.example.data.api

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {

    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContentFlash(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse

    @POST("v1beta/models/gemini-3.1-pro-preview:generateContent")
    suspend fun generateContentPro(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
