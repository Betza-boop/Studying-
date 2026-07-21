package com.example.data

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val apiService: GeminiApiService = retrofit.create(GeminiApiService::class.java)

    const val SYSTEM_INSTRUCTION_UCS = """
Eres un Tutor Clínico de la Universidad de las Ciencias de la Salud (UCS) de Venezuela, especialista en Morfofisiopatología Humana I.
Tu tarea es guiar al estudiante de medicina en su plan de choque de estudio intensivo de 48 horas.
Debes exigir rigurosidad académica, razonamiento clínico basado en la Atención Primaria de Salud (APS) y la medicina integral comunitaria de Venezuela.
Evita explicaciones redundantes o textos de relleno. Ve directo al grano fisiopatológico, morfológico o clínico.
Toda retroalimentación debe basarse estrictamente en la literatura oficial de la asignatura Morfofisiopatología Humana I del PNF-MIC (Medicina Integral Comunitaria).
Si el estudiante responde una pregunta de examen:
1. Evalúa si la respuesta es CORRECTA, PARCIALMENTE CORRECTA o INCORRECTA.
2. Explica brevemente el porqué fisiopatológico o anatomopatológico basándote en los datos clínicos.
3. Relaciónalo con la labor comunitaria de prevención y promoción de salud en el Consultorio Popular de Barrio Adentro (APS).
"""

    suspend fun callGemini(prompt: String, systemInstruction: String = SYSTEM_INSTRUCTION_UCS): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return "Error: Gemini API Key no configurada. Por favor, configúrala en el panel de Secrets de AI Studio."
        }

        val request = GenerateContentRequest(
            contents = listOf(
                Content(parts = listOf(Part(text = prompt)))
            ),
            systemInstruction = Content(parts = listOf(Part(text = systemInstruction))),
            generationConfig = GenerationConfig(temperature = 0.3f)
        )

        return try {
            val response = apiService.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "No se obtuvo respuesta del Tutor Clínico UCS."
        } catch (e: Exception) {
            "Error al consultar al Tutor Clínico: ${e.localizedMessage}"
        }
    }
}
