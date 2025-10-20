package com.example.clase7

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clase7.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun CreateHabitScreen(navController: NavController, userId: String?) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var userProfileHabits by remember { mutableStateOf<List<String>>(emptyList()) }
    var userSavedHabits by remember { mutableStateOf<List<String>>(emptyList()) }
    val selectedHabits = remember { mutableStateListOf<String>() }
    var showLimitToast by remember { mutableStateOf(false) }
    var showMinToast by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val maxHabits = 4
    val create_max = stringResource(R.string.create_habit_max_limit)
    val create_saved = stringResource(R.string.create_habit_saved)
    val create_min = stringResource(R.string.create_habit_min_limit)

    // Animación de entrada
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 800), label = "alphaAnimation"
    )
    val animatedOffsetY by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 50.dp,
        animationSpec = tween(durationMillis = 800), label = "offsetYAnimation"
    )

    // Cargar hábitos del usuario
    LaunchedEffect(userId) {
        userId?.let { id ->
            try {
                val doc = db.collection("users").document(id).get().await()
                val habits = doc.get("habits") as? List<String> ?: emptyList()
                userSavedHabits = habits
                selectedHabits.clear()
                selectedHabits.addAll(habits)
                userProfileHabits = doc.get("profileHabits") as? List<String> ?: emptyList()
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Hábitos predeterminados con iconos
    val allHabits = listOf(
        HabitOption(stringResource(R.string.habit_sleep), Icons.Default.Nightlight, Color(0xFF7B61FF)),
        HabitOption(stringResource(R.string.habit_study), Icons.Default.School, MindBoostPrimary),
        HabitOption(stringResource(R.string.habit_reading), Icons.Default.MenuBook, MindBoostSecondary),
        HabitOption(stringResource(R.string.habit_exercise), Icons.Default.DirectionsRun, Color(0xFFFF9800)),
        HabitOption(stringResource(R.string.habit_meditation), Icons.Default.Favorite, Color(0xFFE91E63)),
        HabitOption(stringResource(R.string.habit_learning), Icons.Default.Lightbulb, Color(0xFFFFEB3B)),
        HabitOption(stringResource(R.string.habit_journaling), Icons.Default.EditNote, Color(0xFF00BCD4)),
        HabitOption(stringResource(R.string.habit_coding), Icons.Default.Code, Color(0xFF607D8B)),
        HabitOption(stringResource(R.string.habit_drinking_water), Icons.Default.LocalDrink, Color(0xFF4CAF50))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MindBoostBackground, Color(0xFFE3EEFA))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .alpha(animatedAlpha)
                .offset(y = animatedOffsetY),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    IconButton(
                        onClick = { navController.navigate("home") { popUpTo("createhabit") { inclusive = true } } },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MindBoostPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.create_habit_screen_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = MindBoostPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Información principal
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MindBoostPrimary.copy(alpha = 0.1f))
                            .border(2.dp, MindBoostPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Crear Hábito",
                            tint = MindBoostPrimary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Selecciona tus hábitos",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MindBoostText,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Elige hasta $maxHabits hábitos para comenzar tu transformación",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MindBoostText.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Contador de selección
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MindBoostPrimary.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Hábitos seleccionados:",
                                style = MaterialTheme.typography.titleMedium,
                                color = MindBoostText,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${selectedHabits.size}/$maxHabits",
                                style = MaterialTheme.typography.titleLarge,
                                color = MindBoostPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Lista de hábitos
            Text(
                text = "Hábitos disponibles:",
                style = MaterialTheme.typography.titleLarge,
                color = MindBoostText,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            allHabits.forEach { habitOption ->
                val isOriginal = userProfileHabits.contains(habitOption.name)
                val alreadySelected = selectedHabits.contains(habitOption.name)

                HabitSelectionCard(
                    habitOption = habitOption,
                    isOriginal = isOriginal,
                    isSelected = alreadySelected,
                    onClick = {
                        when {
                            alreadySelected && !isOriginal -> {
                                if (selectedHabits.size > 1) {
                                    selectedHabits.remove(habitOption.name)
                                } else {
                                    showMinToast = true
                                }
                            }
                            !alreadySelected -> {
                                if (selectedHabits.size < maxHabits) {
                                    selectedHabits.add(habitOption.name)
                                } else {
                                    showLimitToast = true
                                }
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones de acción
            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        try {
                            userId?.let { uid ->
                                val toSave = selectedHabits.filter { !userProfileHabits.contains(it) }
                                db.collection("users").document(uid)
                                    .update("habits", toSave)
                                    .await()
                                Toast.makeText(context, create_saved, Toast.LENGTH_SHORT).show()
                                navController.navigate("home") {
                                    popUpTo("createhabit") { inclusive = true }
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_SHORT).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MindBoostSecondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp), clip = false),
                enabled = !isLoading && selectedHabits.isNotEmpty()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = stringResource(R.string.create_habit_save_button),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("createhabit") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB0B0B0),
                    contentColor = MindBoostText
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp), clip = false),
                enabled = !isLoading
            ) {
                Text(
                    text = stringResource(R.string.create_habit_cancel_button),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Motivational Phrase
            Text(
                text = "Los pequeños hábitos crean grandes cambios 🌱",
                style = MaterialTheme.typography.bodyMedium,
                color = MindBoostText.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Toasts
        if (showLimitToast) {
            LaunchedEffect(showLimitToast) {
                Toast.makeText(context, create_max, Toast.LENGTH_SHORT).show()
                showLimitToast = false
            }
        }

        if (showMinToast) {
            LaunchedEffect(showMinToast) {
                Toast.makeText(context, create_min, Toast.LENGTH_SHORT).show()
                showMinToast = false
            }
        }
    }
}

@Composable
fun HabitSelectionCard(
    habitOption: HabitOption,
    isOriginal: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100), label = "pressScale"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> habitOption.color.copy(alpha = 0.1f)
                isOriginal -> Color(0xFFF5F5F5).copy(alpha = 0.9f)
                else -> Color.White.copy(alpha = 0.9f)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono del hábito
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isSelected) habitOption.color else habitOption.color.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = habitOption.icon,
                    contentDescription = habitOption.name,
                    tint = if (isSelected) Color.White else habitOption.color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Información del hábito
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habitOption.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isOriginal) MindBoostText.copy(alpha = 0.6f) else MindBoostText,
                    fontWeight = FontWeight.Medium
                )
                if (isOriginal) {
                    Text(
                        text = "Hábito original del perfil",
                        style = MaterialTheme.typography.bodySmall,
                        color = MindBoostText.copy(alpha = 0.5f),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            
            // Indicador de selección
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            habitOption.color,
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Seleccionado",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else if (!isOriginal) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(0xFFE0E0E0),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "No seleccionado",
                        tint = MindBoostText.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

data class HabitOption(
    val name: String,
    val icon: ImageVector,
    val color: Color
)