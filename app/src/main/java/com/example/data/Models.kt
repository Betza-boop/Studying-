package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// --- Room Database Entities ---

@Entity(tableName = "personal_notes")
data class PersonalNote(
    @PrimaryKey val themeId: String,
    val noteText: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_progress")
data class StudyProgress(
    @PrimaryKey val themeId: String,
    val isCompleted: Boolean = false,
    val plannedDay: Int = 1 // Day 1 or Day 2 of the 48h plan
)

@Entity(tableName = "exam_attempts")
data class ExamAttempt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val score: Int, // e.g. out of 100 or number of correct answers
    val totalQuestions: Int,
    val overallFeedback: String,
    val questionsJson: String // Serialized list of evaluated questions
)

// --- Gemini API Moshi Models ---

@JsonClass(generateAdapter = true)
data class Part(
    @Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "parts") val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    @Json(name = "responseMimeType") val responseMimeType: String? = null,
    @Json(name = "temperature") val temperature: Float? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "systemInstruction") val systemInstruction: Content? = null,
    @Json(name = "generationConfig") val generationConfig: GenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    @Json(name = "content") val content: Content? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    @Json(name = "candidates") val candidates: List<Candidate>? = null
)

// Helper model to represent exam simulator questions
data class SimQuestion(
    val id: Int,
    val type: String, // "SELECCION", "CASO_CLINICO", "DESARROLLO"
    val questionText: String,
    val options: List<String>? = null, // for multiple choice
    val correctAnswer: String, // correct option index or exact keyword answer
    val justification: String
)

data class EvaluatedQuestion(
    val id: Int,
    val type: String,
    val questionText: String,
    val studentAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val feedback: String // Detailed UCS tutor feedback
)
