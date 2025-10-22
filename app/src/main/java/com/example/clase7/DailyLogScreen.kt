package com.example.clase7

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clase7.R
import com.example.clase7.AchievementManager
import com.example.clase7.models.Achievement
import com.example.clase7.ui.theme.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DailyLogScreen(navController: NavController, userId: String?, skipDailyRedirect: MutableState<Boolean>) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val today = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
    
    val formattedDate = remember {
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
    }

    var loading by remember { mutableStateOf(true) }
    var habits by remember { mutableStateOf<List<String>>(emptyList()) }
    val checked = remember { mutableStateMapOf<String, Boolean>() }
    val durations = remember { mutableStateMapOf<String, String>() }
    var notes by remember { mutableStateOf("") }
    var showSavedToast by remember { mutableStateOf(false) }
    var showErrorToast by remember { mutableStateOf(false) }
    var showSuccessAnimation by remember { mutableStateOf(false) }

    // Animación de entrada
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = tween(600),
        label = "scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(800),
        label = "alpha"
    )

    // Cargar hábitos del usuario
    LaunchedEffect(userId) {
        if (userId != null) {
            try {
                val doc = db.collection("users").document(userId).get().await()
                if (doc.exists()) {
                    val userHabits = doc.get("habits") as? List<String> ?: emptyList()
                    habits = userHabits
                    
                    // Cargar datos del día si existen
                    val todayDoc = db.collection("users")
                        .document(userId)
                        .collection("habitLogs")
                        .document(today)
                        .get().await()
                    
                    if (todayDoc.exists()) {
                        val data = todayDoc.data ?: emptyMap()
                        userHabits.forEach { habit ->
                            checked[habit] = data[habit] as? Boolean ?: false
                            durations[habit] = (data["${habit}_duration"] as? Long)?.toString() ?: ""
                        }
                        notes = data["notes"] as? String ?: ""
                    }
                }
            } catch (e: Exception) {
                showErrorToast = true
                            }
                            loading = false
                        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MindBoostBackground,
                        Color(0xFFE3EEFA)
                    )
                )
            )
    ) {
        val scrollState = rememberScrollState()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .scale(scale)
                .alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Encabezado con fecha
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Fecha",
                        tint = MindBoostPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = "Registro diario — $formattedDate",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MindBoostPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (loading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MindBoostPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else if (habits.isEmpty()) {
                // Mensaje cuando no hay hábitos
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar hábitos",
                            tint = MindBoostSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "No tienes hábitos registrados",
                            style = MaterialTheme.typography.titleLarge,
                            color = MindBoostText,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Crea algunos hábitos para comenzar tu registro diario",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MindBoostText.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(
                            onClick = { navController.navigate("createhabit") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MindBoostSecondary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Crear mi primer hábito",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                // Lista de hábitos
                Text(
                    text = "Mis hábitos de hoy",
                    style = MaterialTheme.typography.titleLarge,
                    color = MindBoostText,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                habits.forEach { habit ->
                    HabitCard(
                        habit = habit,
                        isChecked = checked[habit] ?: false,
                        duration = durations[habit] ?: "",
                        onCheckedChange = { checked[habit] = it },
                        onDurationChange = { durations[habit] = it }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Campo de notas
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = "Notas",
                                tint = MindBoostAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "Notas del día",
                                style = MaterialTheme.typography.titleMedium,
                                color = MindBoostText,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
                            placeholder = {
                                Text(
                                    text = "Escribe tus pensamientos o progresos del día...",
                                    color = MindBoostText.copy(alpha = 0.5f)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MindBoostAccent,
                                unfocusedBorderColor = MindBoostText.copy(alpha = 0.3f),
                                focusedTextColor = MindBoostText,
                                unfocusedTextColor = MindBoostText
                            ),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 3,
                            maxLines = 5
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botones de acción
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Botón Guardar
        Button(
            onClick = {
                if (checked.values.none { it }) {
                    showErrorToast = true
                    return@Button
                }
                            
                userId?.let { uid ->
                                coroutineScope.launch {
                                    try {
                    val dataToSave = mutableMapOf<String, Any>()
                    checked.forEach { (habit, done) ->
                        dataToSave[habit] = done
                                            durations[habit]?.let { duration ->
                                                dataToSave["${habit}_duration"] = duration.toLongOrNull() ?: 0L
                                            }
                    }
                    dataToSave["notes"] = notes
                    dataToSave["timestamp"] = FieldValue.serverTimestamp()

                    val userRef = db.collection("users").document(uid)
                    userRef.collection("habitLogs").document(today)
                        .set(dataToSave)
                                            .await()
                                        
                                        // Actualizar streak count
                                        val userDoc = userRef.get().await()
                                        val currentStreak = userDoc.getLong("streakCount") ?: 0L
                                        val lastLogDate = userDoc.getString("lastLogDate")
                                        
                                        val yesterday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                            .format(Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000))
                                        
                                        val newStreak = if (lastLogDate == yesterday) {
                                            currentStreak + 1
                                        } else if (lastLogDate == today) {
                                            currentStreak
                                        } else {
                                            1L
                                        }
                                        
                                        userRef.update(
                                            mapOf(
                                                "streakCount" to newStreak,
                                                "lastLogDate" to today
                                            )
                                        ).await()
                                        
                                        showSuccessAnimation = true
                            skipDailyRedirect.value = true

                                        // Navegar a stats después de un breve delay
                                        kotlinx.coroutines.delay(1500)
                            navController.navigate("stats") {
                                popUpTo("dailylog") { inclusive = true }
                            }
                                        
                                    } catch (e: Exception) {
                                        showErrorToast = true
                                        android.util.Log.e("DailyLogScreen", "Error saving log: ${e.message}")
                        }
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
                    ) {
                        Text(
                            text = "Guardar registro",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón Ir al Menú Principal
        Button(
            onClick = {
                            navController.navigate("home") {
                                popUpTo("dailylog") { inclusive = false }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MindBoostPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Ir al Menú Principal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón Omitir día
                    Button(
                        onClick = {
                skipDailyRedirect.value = true
                navController.navigate("home") {
                    popUpTo("dailylog") { inclusive = true }
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
                    ) {
                        Text(
                            text = "Omitir día",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Frase motivacional
                Text(
                    text = "Un día a la vez, construyes tu mejor versión 🌱",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MindBoostText.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Toasts y animaciones
    if (showSavedToast) {
        LaunchedEffect(showSavedToast) {
            Toast.makeText(context, "¡Progreso guardado! 🎉", Toast.LENGTH_SHORT).show()
            showSavedToast = false
        }
    }

    if (showErrorToast) {
        LaunchedEffect(showErrorToast) {
            Toast.makeText(context, "Selecciona al menos un hábito", Toast.LENGTH_SHORT).show()
            showErrorToast = false
        }
    }

    // Animación de éxito
    if (showSuccessAnimation) {
        LaunchedEffect(showSuccessAnimation) {
            Toast.makeText(context, "¡Progreso guardado! 🎉", Toast.LENGTH_SHORT).show()
            showSuccessAnimation = false
        }
    }

    // Diálogo de logro desbloqueado
    AchievementManager.lastUnlockedAchievement?.let { ach ->
        AlertDialog(
            onDismissRequest = { AchievementManager.clearLastUnlocked() },
            title = { Text(text = stringResource(R.string.achievement_unlocked_title), fontWeight = FontWeight.Bold) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = stringResource(ach.titleRes),
                        tint = MindBoostAccent,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = stringResource(ach.titleRes), fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(R.string.achievement_unlocked_msg, stringResource(ach.titleRes)))
                }
            },
            confirmButton = {
                Button(
                    onClick = { AchievementManager.clearLastUnlocked() },
                    colors = ButtonDefaults.buttonColors(containerColor = MindBoostPrimary)
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }
    }

@Composable
fun HabitCard(
    habit: String,
    isChecked: Boolean,
    duration: String,
    onCheckedChange: (Boolean) -> Unit,
    onDurationChange: (String) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100),
        label = "press_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                onCheckedChange(!isChecked)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono del hábito
            Icon(
                imageVector = getHabitIcon(habit),
                contentDescription = habit,
                tint = getHabitColor(habit),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Texto del hábito
            Text(
                text = habit,
                style = MaterialTheme.typography.titleMedium,
                color = MindBoostText,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            // Checkbox
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MindBoostPrimary,
                    uncheckedColor = MindBoostText.copy(alpha = 0.5f)
                )
            )
        }
        
        // Campo de duración (solo si está marcado)
        if (isChecked) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Duración",
                    tint = MindBoostSecondary,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OutlinedTextField(
                    value = duration,
                    onValueChange = onDurationChange,
                    placeholder = {
                        Text(
                            text = "Duración (min)",
                            color = MindBoostText.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MindBoostSecondary,
                        unfocusedBorderColor = MindBoostText.copy(alpha = 0.3f),
                        focusedTextColor = MindBoostText,
                        unfocusedTextColor = MindBoostText
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(120.dp),
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun getHabitIcon(habit: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (habit.lowercase()) {
        "sueño", "dormir", "sleep" -> Icons.Default.Nightlight
        "estudiar", "estudio", "study" -> Icons.Default.School
        "leer", "lectura", "reading" -> Icons.Default.MenuBook
        "ejercicio", "correr", "running", "gym" -> Icons.Default.DirectionsRun
        "meditar", "meditación", "meditation" -> Icons.Default.Favorite
        "agua", "hidratación", "water" -> Icons.Default.LocalDrink
        "trabajo", "work" -> Icons.Default.Work
        else -> Icons.Default.CheckCircle
    }
}

fun getHabitColor(habit: String): Color {
    return when (habit.lowercase()) {
        "sueño", "dormir", "sleep" -> Color(0xFF9C27B0)
        "estudiar", "estudio", "study" -> MindBoostPrimary
        "leer", "lectura", "reading" -> Color(0xFF4CAF50)
        "ejercicio", "correr", "running", "gym" -> Color(0xFFFF9800)
        "meditar", "meditación", "meditation" -> Color(0xFF00BCD4)
        "agua", "hidratación", "water" -> Color(0xFF2196F3)
        "trabajo", "work" -> Color(0xFF607D8B)
        else -> MindBoostAccent
    }
}