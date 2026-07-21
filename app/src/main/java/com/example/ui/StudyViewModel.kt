package com.example.ui

import android.app.Application
import com.example.BuildConfig
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val noteDao = db.personalNoteDao()
    private val progressDao = db.studyProgressDao()
    private val attemptDao = db.examAttemptDao()

    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val listMyTypes = Types.newParameterizedType(List::class.java, EvaluatedQuestion::class.java)
    private val moshiAdapter = moshi.adapter<List<EvaluatedQuestion>>(listMyTypes)

    // State flows representing database contents
    val personalNotes: StateFlow<List<PersonalNote>> = noteDao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studyProgress: StateFlow<List<StudyProgress>> = progressDao.getAllProgress()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val examAttempts: StateFlow<List<ExamAttempt>> = attemptDao.getAllAttempts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI state for the current exam simulator
    private val _currentQuestions = MutableStateFlow<List<SimQuestion>>(PresetQuestions.questions)
    val currentQuestions: StateFlow<List<SimQuestion>> = _currentQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _studentAnswers = MutableStateFlow<Map<Int, String>>(emptyMap())
    val studentAnswers: StateFlow<Map<Int, String>> = _studentAnswers.asStateFlow()

    // Evaluations of the current exam: Maps question ID to EvaluatedQuestion
    private val _currentEvaluations = MutableStateFlow<Map<Int, EvaluatedQuestion>>(emptyMap())
    val currentEvaluations: StateFlow<Map<Int, EvaluatedQuestion>> = _currentEvaluations.asStateFlow()

    private val _isEvaluating = MutableStateFlow(false)
    val isEvaluating: StateFlow<Boolean> = _isEvaluating.asStateFlow()

    // AI diagnostic flow
    private val _aiDiagnostic = MutableStateFlow<String?>(null)
    val aiDiagnostic: StateFlow<String?> = _aiDiagnostic.asStateFlow()

    private val _isGeneratingDiagnostic = MutableStateFlow(false)
    val isGeneratingDiagnostic: StateFlow<Boolean> = _isGeneratingDiagnostic.asStateFlow()

    init {
        // Pre-populate progress if empty
        viewModelScope.launch(Dispatchers.IO) {
            studyProgress.collectLatest { list ->
                if (list.isEmpty()) {
                    // Prepopulate defaults based on priority clinical targets
                    HighYieldContent.blocks.flatMap { it.themes }.forEachIndexed { idx, theme ->
                        val day = if (idx < 3) 1 else 2 // Split themes across the 48 hours (Day 1 / Day 2)
                        progressDao.insertProgress(StudyProgress(themeId = theme.id, isCompleted = false, plannedDay = day))
                    }
                }
            }
        }
    }

    // --- Study Progress Actions ---

    fun toggleThemeCompletion(themeId: String, completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            progressDao.updateCompletion(themeId, completed)
        }
    }

    fun updateThemePlannedDay(themeId: String, day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            progressDao.updatePlannedDay(themeId, day)
        }
    }

    fun savePersonalNote(themeId: String, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insertNote(PersonalNote(themeId = themeId, noteText = text))
        }
    }

    fun deletePersonalNote(themeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteNote(themeId)
        }
    }

    // --- AI Diagnostic Schedule Generator ---

    fun generateAiDiagnostic(weakAreas: String, totalAvailableHours: String = "48 Horas") {
        _isGeneratingDiagnostic.value = true
        _aiDiagnostic.value = "Consultando al Tutor Clínico UCS..."
        viewModelScope.launch {
            val prompt = """
Genera un plan de estudio intensivo ultra optimizado de $totalAvailableHours para un estudiante de medicina que se prepara para el examen de Morfofisiopatología Humana I de la UCS.
El estudiante tiene dificultades especiales o debilidades en: "$weakAreas".
Organiza el plan detalladamente en:
1. DIAGNÓSTICO CLÍNICO: Breve análisis de qué temas priorizar de acuerdo a su debilidad (máximo 3 párrafos).
2. DISTRIBUCIÓN HORARIA (DÍA 1 - Primeras 24 Horas): Horas específicas y qué bloques estudiar.
3. DISTRIBUCIÓN HORARIA (DÍA 2 - Siguientes 24 Horas): Horas específicas y qué bloques de Genética o Inflamación estudiar.
4. ABORDAJE COMUNITARIO (APS): Una breve directriz de cómo aplicar esto en el Consultorio Popular de Barrio Adentro (Atención Primaria).
Evita rodeos, ve directo al grano científico, fisiopatológico y metodológico. Excluye texto de relleno.
""".trimIndent()
            val result = withContext(Dispatchers.IO) {
                GeminiClient.callGemini(prompt)
            }
            _aiDiagnostic.value = result
            _isGeneratingDiagnostic.value = false
        }
    }

    fun clearAiDiagnostic() {
        _aiDiagnostic.value = null
    }

    // --- Exam Simulator Actions ---

    fun setStudentAnswer(questionId: Int, answer: String) {
        val current = _studentAnswers.value.toMutableMap()
        current[questionId] = answer
        _studentAnswers.value = current
    }

    fun selectQuestion(index: Int) {
        if (index in 0 until _currentQuestions.value.size) {
            _currentQuestionIndex.value = index
        }
    }

    fun evaluateAnswer(question: SimQuestion, answer: String) {
        _isEvaluating.value = true
        viewModelScope.launch {
            val isOptionSelect = question.type == "SELECCION"
            val prompt = if (isOptionSelect) {
                """
El estudiante ha respondido una pregunta de selección simple de Morfofisiopatología Humana I de la UCS.
Pregunta: "${question.questionText}"
Opciones: ${question.options?.joinToString()}
Respuesta seleccionada por el estudiante: "$answer" (Índice de opción o texto)
Respuesta correcta esperada: "${question.options?.getOrNull(question.correctAnswer.toIntOrNull() ?: -1) ?: question.correctAnswer}"

Evalúa si su selección es correcta.
Proporciona la retroalimentación breve como el Tutor Clínico UCS, explicando:
1. El fundamento fisiopatológico o morfológico de la necrosis, inflamación, cicatrización o genética.
2. Su aplicación o implicación clínica en la Atención Primaria de Salud (APS) o Consultorio Popular.
Ve directo al grano, sin textos introductorios ni despedidas de relleno.
""".trimIndent()
            } else {
                """
El estudiante ha respondido una pregunta clínica de desarrollo corto en Morfofisiopatología Humana I de la UCS.
Pregunta: "${question.questionText}"
Respuesta redactada por el estudiante: "$answer"
Respuesta modelo esperada: "${question.correctAnswer}"
Justificación académica: "${question.justification}"

Evalúa si la respuesta es CORRECTA, PARCIALMENTE CORRECTA o INCORRECTA.
Proporciona la retroalimentación breve como el Tutor Clínico UCS, detallando:
1. Qué puntos clave acertó o le faltaron (ej. entrada de sodio/agua, tipo de necrosis, corpúsculos de Barr, fórmulas de Hardy-Weinberg).
2. El fundamento fisiopatológico y morfológico real.
3. Su relevancia en la Atención Primaria de Salud (APS) en Venezuela.
Ve directo al grano, sin textos introductorios de relleno.
""".trimIndent()
            }

            val result = withContext(Dispatchers.IO) {
                // If the key is not set, we do a high-quality local keyword match fallback to prevent dead ends!
                val apiKey = BuildConfig.GEMINI_API_KEY
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    val isCorrectLocal = evaluateLocalFallback(question, answer)
                    val feedbackLocal = """
[EVALUACIÓN LOCAL - OFFLINE]
Resultado: ${if (isCorrectLocal) "CORRECTA" else "PARCIALMENTE CORRECTA"}
Explicación del Tutor Clínico UCS: 
${question.justification}

Relevancia en APS (Barrio Adentro): El dominio de esta fisiopatología es indispensable para tomar decisiones de derivación oportuna desde el Consultorio Popular en casos de emergencias clínicas (como infartos o pancreatitis agudas).

(Para una evaluación interactiva dinámica por IA, configura tu GEMINI_API_KEY en los Secrets de AI Studio).
""".trimIndent()
                    EvaluatedQuestion(
                        id = question.id,
                        type = question.type,
                        questionText = question.questionText,
                        studentAnswer = answer,
                        correctAnswer = question.correctAnswer,
                        isCorrect = isCorrectLocal,
                        feedback = feedbackLocal
                    )
                } else {
                    val aiResponse = GeminiClient.callGemini(prompt)
                    val isCorrect = aiResponse.contains("CORRECTA", ignoreCase = true) && !aiResponse.contains("INCORRECTA", ignoreCase = true)
                    EvaluatedQuestion(
                        id = question.id,
                        type = question.type,
                        questionText = question.questionText,
                        studentAnswer = answer,
                        correctAnswer = question.correctAnswer,
                        isCorrect = isCorrect || aiResponse.contains("Sí", ignoreCase = true) || aiResponse.contains("es correcta", ignoreCase = true),
                        feedback = aiResponse
                    )
                }
            }

            val currentEvals = _currentEvaluations.value.toMutableMap()
            currentEvals[question.id] = result
            _currentEvaluations.value = currentEvals
            _isEvaluating.value = false
        }
    }

    private fun evaluateLocalFallback(question: SimQuestion, answer: String): Boolean {
        if (question.type == "SELECCION") {
            return answer.trim() == question.correctAnswer.trim()
        }
        // Simple keyword match for development questions
        val keywords = when (question.id) {
            1 -> listOf("tumefacción", "hidrópica", "bomba", "sodio", "potasio", "agua")
            3 -> listOf("tuberculosis", "caseosa", "epitelioide", "gigante", "Langhans", "granuloma")
            5 -> listOf("45", "XO", "0", "47", "XXY", "1")
            6 -> listOf("0.3", "0.5")
            else -> listOf()
        }
        val matchCount = keywords.count { answer.contains(it, ignoreCase = true) }
        return matchCount >= 2
    }

    fun submitFullExam() {
        viewModelScope.launch(Dispatchers.IO) {
            val evals = _currentEvaluations.value.values.toList()
            if (evals.isEmpty()) return@launch

            val correctCount = evals.count { it.isCorrect }
            val score = ((correctCount.toFloat() / evals.size) * 20).toInt() // UCS grading scale is out of 20 points!
            
            // Generate overall feedback
            val overallFeedback = if (score >= 16) {
                "Excelente rendimiento clínico. Demuestra sólida comprensión de la morfofisiopatología y APS."
            } else if (score >= 10) {
                "Rendimiento aprobado. Se recomienda repasar los puntos débiles de genética y necrosis."
            } else {
                "Plan de choque requerido de urgencia. Repasa las flashcards de memorización inmediata."
            }

            val questionsSerialized = moshiAdapter.toJson(evals) ?: "[]"
            
            val attempt = ExamAttempt(
                score = score,
                totalQuestions = evals.size,
                overallFeedback = overallFeedback,
                questionsJson = questionsSerialized
            )
            attemptDao.insertAttempt(attempt)
        }
    }

    fun resetExam() {
        _studentAnswers.value = emptyMap()
        _currentEvaluations.value = emptyMap()
        _currentQuestionIndex.value = 0
    }

    fun clearAttempts() {
        viewModelScope.launch(Dispatchers.IO) {
            attemptDao.clearAllAttempts()
        }
    }
}
