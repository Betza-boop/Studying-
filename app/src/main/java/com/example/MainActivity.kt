package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.*
import com.example.ui.StudyViewModel
import com.example.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: StudyViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }

    val notes by viewModel.personalNotes.collectAsStateWithLifecycle()
    val progress by viewModel.studyProgress.collectAsStateWithLifecycle()
    val attempts by viewModel.examAttempts.collectAsStateWithLifecycle()

    val totalThemes = HighYieldContent.blocks.flatMap { it.themes }.size
    val completedThemes = progress.count { it.isCompleted }
    val progressPercent = if (totalThemes > 0) (completedThemes.toFloat() / totalThemes) else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab Layout
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("PLAN 48H", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                icon = { Icon(Icons.Default.Timer, contentDescription = "Plan de estudio") },
                modifier = Modifier.testTag("tab_plan_de_choque")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("MÓDULO ESTUDIO", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                icon = { Icon(Icons.Default.Book, contentDescription = "Contenidos de estudio") },
                modifier = Modifier.testTag("tab_modulo_de_estudio")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("SIMULADOR", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                icon = { Icon(Icons.Default.Assignment, contentDescription = "Simulador de examen") },
                modifier = Modifier.testTag("tab_simulador_examen")
            )
        }

        // Main content depending on the selected tab
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (selectedTab) {
                0 -> PlanDeChoqueTab(
                    viewModel = viewModel,
                    progress = progress,
                    completedThemes = completedThemes,
                    totalThemes = totalThemes,
                    progressPercent = progressPercent
                )
                1 -> ModuloEstudioTab(
                    viewModel = viewModel,
                    progress = progress,
                    notes = notes
                )
                2 -> SimuladorExamenTab(
                    viewModel = viewModel,
                    attempts = attempts
                )
            }
        }
    }
}

@Composable
fun PlanDeChoqueTab(
    viewModel: StudyViewModel,
    progress: List<StudyProgress>,
    completedThemes: Int,
    totalThemes: Int,
    progressPercent: Float
) {
    var weakAreasText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val aiDiagnostic by viewModel.aiDiagnostic.collectAsStateWithLifecycle()
    val isGeneratingDiagnostic by viewModel.isGeneratingDiagnostic.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.MedicalServices,
                        contentDescription = "Medical",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Plan de Choque UCS 48H",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "Programa intensivo para dominar los 12 documentos clave de Morfofisiopatología Humana I, ajustado al modelo evaluativo de la Universidad de las Ciencias de la Salud (UCS) en Venezuela.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Progress Bar
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progreso del Plan: $completedThemes de $totalThemes Temas",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${(progressPercent * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    LinearProgressIndicator(
                        progress = { progressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }

        // Diagnostic generator section
        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "🩺 Diagnóstico Inicial con IA",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Escribe los temas que más te cuestan y el Tutor Clínico generará un cronograma personalizado de 2 días basado en Atención Primaria de Salud (APS):",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = weakAreasText,
                    onValueChange = { weakAreasText = it },
                    label = { Text("Ej: Necrosis caseosa, Hardy-Weinberg, Diabetes") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.generateAiDiagnostic(weakAreasText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("diagnosticar_ia_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = weakAreasText.isNotBlank() && !isGeneratingDiagnostic
                ) {
                    if (isGeneratingDiagnostic) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Generar")
                            Text("Diagnosticar y Estructurar Plan 48H", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Show AI diagnostic schedule if generated
                AnimatedVisibility(
                    visible = aiDiagnostic != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    aiDiagnostic?.let { plan ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📋 Plan Cronograma de Choque IA:",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                IconButton(onClick = { viewModel.clearAiDiagnostic() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Cerrar", modifier = Modifier.size(16.dp))
                                }
                            }
                            Text(
                                text = plan,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Roadmaps Section (The 4 high-yield blocks)
        Text(
            text = "Estructura Evaluativa del Plan (4 Bloques)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        HighYieldContent.blocks.forEach { block ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = block.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Badge(
                            containerColor = if (block.id == 1 || block.id == 2) {
                                MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                            } else {
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                            },
                            contentColor = if (block.id == 1 || block.id == 2) {
                                Color.Red
                            } else {
                                MaterialTheme.colorScheme.tertiary
                            }
                        ) {
                            Text(
                                text = block.priority.substringBefore(" ("),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = block.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)

                    // Display themes in this block with checkboxes
                    block.themes.forEach { theme ->
                        val themeProgress = progress.firstOrNull { it.themeId == theme.id }
                        val isCompleted = themeProgress?.isCompleted ?: false
                        val plannedDay = themeProgress?.plannedDay ?: 1

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.toggleThemeCompletion(theme.id, !isCompleted)
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = "Estado",
                                    tint = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = theme.title,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Asignado a: Día $plannedDay (Siguiente paso)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Day Selector
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                FilterChip(
                                    selected = plannedDay == 1,
                                    onClick = { viewModel.updateThemePlannedDay(theme.id, 1) },
                                    label = { Text("Día 1", fontSize = 10.sp) }
                                )
                                FilterChip(
                                    selected = plannedDay == 2,
                                    onClick = { viewModel.updateThemePlannedDay(theme.id, 2) },
                                    label = { Text("Día 2", fontSize = 10.sp) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModuloEstudioTab(
    viewModel: StudyViewModel,
    progress: List<StudyProgress>,
    notes: List<PersonalNote>
) {
    var selectedBlockId by remember { mutableStateOf(1) }
    var selectedThemeId by remember { mutableStateOf("t1") }

    val currentBlock = HighYieldContent.blocks.first { it.id == selectedBlockId }
    val currentTheme = currentBlock.themes.firstOrNull { it.id == selectedThemeId }
        ?: currentBlock.themes.first()

    // Notes state
    val currentNote = notes.firstOrNull { it.themeId == currentTheme.id }
    var noteInputText by remember(currentTheme.id) { mutableStateOf(currentNote?.noteText ?: "") }
    var isNoteSavedTextVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Block Selector Header
        Text(
            text = "Seleccionar Bloque de Contenido:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(1, 2, 3, 4).forEach { blockId ->
                val isSelected = selectedBlockId == blockId
                Button(
                    onClick = {
                        selectedBlockId = blockId
                        // Select first theme of the new block
                        selectedThemeId = HighYieldContent.blocks.first { it.id == blockId }.themes.first().id
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "B$blockId",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Theme Selector
        Text(
            text = "Seleccionar Tema Clínico:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        currentBlock.themes.forEach { theme ->
            val isSelected = selectedThemeId == theme.id
            Card(
                shape = RoundedCornerShape(12.dp),
                border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedThemeId = theme.id }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.MenuBook else Icons.Default.Book,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = theme.title,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Content Display
        Text(
            text = "📚 Contenido de Repaso Intensivo",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = currentTheme.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Synthesis
                Text(
                    text = "Fisiopatología, Clínica y Abordaje APS:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = currentTheme.synthesis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)

                // Focus points
                Text(
                    text = "🎯 Puntos Focalizados UCS (Lo que van a preguntar):",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                currentTheme.focusPoints.forEach { point ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text("•", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(
                            text = point,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Interactive Flashcards
        Text(
            text = "⚡ Flashcards de Memorización Rápida",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        currentTheme.flashcards.forEachIndexed { index, card ->
            var isRevealed by remember(currentTheme.id, index) { mutableStateOf(false) }
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isRevealed) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surfaceVariant
                ),
                border = if (isRevealed) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isRevealed = !isRevealed }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Flashcard #${index + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (isRevealed) "Tap para Ocultar" else "Tap para Revelar",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = card.question,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    AnimatedVisibility(
                        visible = isRevealed,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Divider(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            Text(
                                text = card.answer,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Mis Notas Personales (Room Persistent)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Notes,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "✍️ Mis Notas Clínicas (Persistente)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Anota tus nemotecnias o resúmenes clínicos de APS sobre este tema. Se guardarán de manera local y permanente.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = noteInputText,
                    onValueChange = { noteInputText = it },
                    placeholder = { Text("Escribe tus observaciones de estudio aquí...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.savePersonalNote(currentTheme.id, noteInputText)
                            isNoteSavedTextVisible = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("guardar_nota_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Guardar Notas", fontWeight = FontWeight.Bold)
                    }

                    if (currentNote != null) {
                        IconButton(
                            onClick = {
                                viewModel.deletePersonalNote(currentTheme.id)
                                noteInputText = ""
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }

                // Temporary checkmark anim
                AnimatedVisibility(visible = isNoteSavedTextVisible) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Green)
                        Text(
                            text = " Notas guardadas en Room con éxito!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Green,
                            fontWeight = FontWeight.Bold
                        )
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(2000)
                            isNoteSavedTextVisible = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimuladorExamenTab(
    viewModel: StudyViewModel,
    attempts: List<ExamAttempt>
) {
    val questions by viewModel.currentQuestions.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()
    val answers by viewModel.studentAnswers.collectAsStateWithLifecycle()
    val evals by viewModel.currentEvaluations.collectAsStateWithLifecycle()
    val isEvaluating by viewModel.isEvaluating.collectAsStateWithLifecycle()

    val currentQuestion = questions.getOrNull(currentIndex)
    val currentAnswer = answers[currentQuestion?.id] ?: ""
    val currentEvaluation = evals[currentQuestion?.id]

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Quiz,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Simulador Examen UCS",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "Demuestra tu razonamiento clínico en Atención Primaria de Salud (APS) con preguntas de selección, desarrollo y casos clínico-comunitarios basados en exámenes anteriores de la UCS.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Active Question Block
        if (currentQuestion != null) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Question index & Type indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pregunta ${currentIndex + 1} de ${questions.size}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Badge(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) {
                            Text(
                                text = currentQuestion.type,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Question Text
                    Text(
                        text = currentQuestion.questionText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Answer Input based on Type
                    if (currentQuestion.type == "SELECCION") {
                        currentQuestion.options?.forEachIndexed { index, option ->
                            val isSelected = currentAnswer == index.toString()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        else Color.Transparent
                                    )
                                    .clickable { viewModel.setStudentAnswer(currentQuestion.id, index.toString()) }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { viewModel.setStudentAnswer(currentQuestion.id, index.toString()) }
                                )
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = currentAnswer,
                            onValueChange = { viewModel.setStudentAnswer(currentQuestion.id, it) },
                            placeholder = { Text("Escribe tu respuesta detallada aquí...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Action buttons (Previous, Next, Evaluate)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.selectQuestion(currentIndex - 1) },
                            enabled = currentIndex > 0
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Anterior")
                        }

                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.evaluateAnswer(currentQuestion, currentAnswer)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentEvaluation != null) Color.Gray else MaterialTheme.colorScheme.primary
                            ),
                            enabled = currentAnswer.isNotBlank() && !isEvaluating,
                            modifier = Modifier.testTag("evaluar_respuesta_button")
                        ) {
                            if (isEvaluating) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                            } else {
                                Text(if (currentEvaluation != null) "Re-Evaluar con IA" else "Evaluar Respuesta", fontWeight = FontWeight.Bold)
                            }
                        }

                        IconButton(
                            onClick = { viewModel.selectQuestion(currentIndex + 1) },
                            enabled = currentIndex < questions.size - 1
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Siguiente")
                        }
                    }

                    // Display evaluation results
                    AnimatedVisibility(
                        visible = currentEvaluation != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        currentEvaluation?.let { evaluation ->
                            val isCorrect = evaluation.isCorrect
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isCorrect) Color.Green.copy(alpha = 0.05f) else Color.Red.copy(alpha = 0.05f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isCorrect) Color.Green.copy(alpha = 0.3f) else Color.Red.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = if (isCorrect) Color.Green else Color.Red
                                    )
                                    Text(
                                        text = if (isCorrect) "Respuesta Evaluada Correcta" else "Evaluada con Observaciones",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isCorrect) Color.Green else Color.Red
                                    )
                                }

                                Text(
                                    text = evaluation.feedback,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Save & Reset Exam Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.submitFullExam() },
                modifier = Modifier
                    .weight(1f)
                    .testTag("guardar_examen_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = evals.isNotEmpty()
            ) {
                Text("Finalizar y Guardar Simulacro", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = { viewModel.resetExam() },
                modifier = Modifier.testTag("reiniciar_examen_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Reiniciar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // History of past attempts
        Text(
            text = "⏳ Historial de Simulacros Guardados (Room)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        if (attempts.isEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No has guardado simulacros todavía. Completa algunas evaluaciones arriba y guárdalas.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            IconButton(
                onClick = { viewModel.clearAttempts() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.DeleteSweep, contentDescription = "Borrar historial", tint = MaterialTheme.colorScheme.error)
            }

            attempts.forEach { attempt ->
                var isExpanded by remember { mutableStateOf(false) }
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(attempt.timestamp))
                                Text(
                                    text = "Simulacro - $date",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = attempt.overallFeedback,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "${attempt.score} / 20 Puntos",
                                fontWeight = FontWeight.Black,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (attempt.score >= 16) Color.Green else if (attempt.score >= 10) MaterialTheme.colorScheme.primary else Color.Red
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Detalle del Simulacro:",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Este simulacro incluyó ${attempt.totalQuestions} evaluaciones personalizadas del Tutor Clínico de la UCS.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
