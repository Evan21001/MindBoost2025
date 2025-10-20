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
import com.example.clase7.models.User
import com.example.clase7.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.clase7.ui.theme.USERS_COLLECTION
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Variables de edición
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var availableHours by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<Int?>(null) }
    var ageError by remember { mutableStateOf<Int?>(null) }
    var hoursError by remember { mutableStateOf<Int?>(null) }

    val profile_update = stringResource(R.string.profile_updated)

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

    // Cargar datos del usuario desde Firestore
    LaunchedEffect(Unit) {
        userId?.let { uid ->
            try {
                val document = db.collection(USERS_COLLECTION).document(uid).get().await()
                user = document.toObject(User::class.java)
                user?.let { u ->
                    name = u.name
                    age = u.age.toString()
                    availableHours = u.availableHours.toString()
                }
                isLoading = false
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
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
        if (isLoading) {
            // Pantalla de carga moderna
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.scale(scale).alpha(alpha)
                ) {
                    CircularProgressIndicator(
                        color = MindBoostPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.profile_loading),
                        style = MaterialTheme.typography.titleMedium,
                        color = MindBoostText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        } else {
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
                
                // Encabezado con botón de regreso
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigate("home") { popUpTo("profile") { inclusive = true } } },
                        modifier = Modifier
                            .background(
                                Color.White.copy(alpha = 0.9f),
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_home),
                            tint = MindBoostPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Text(
                        text = stringResource(R.string.profile_screen_title),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MindBoostPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Avatar y información principal
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    MindBoostPrimary.copy(alpha = 0.1f),
                                    RoundedCornerShape(40.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                tint = MindBoostPrimary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Nombre del usuario
                        Text(
                            text = user?.name ?: "Usuario",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MindBoostText,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Email del usuario
                        Text(
                            text = auth.currentUser?.email ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MindBoostText.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 🔥 Racha actual mejorada
                user?.let { u ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFF9800).copy(alpha = 0.9f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = stringResource(R.string.profile_streak_label),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${u.streakCount} ${stringResource(R.string.profile_streak_days)}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Racha",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isEditing) {
                    // Modo de edición
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
                            Text(
                                text = "Editar Perfil",
                                style = MaterialTheme.typography.titleLarge,
                                color = MindBoostText,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            // Campo Nombre
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text(stringResource(R.string.fields_name)) },
                                isError = nameError != null,
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MindBoostPrimary,
                                    unfocusedBorderColor = MindBoostText.copy(alpha = 0.3f),
                                    focusedTextColor = MindBoostText,
                                    unfocusedTextColor = MindBoostText
                                ),
                                shape = RoundedCornerShape(12.dp),
                                supportingText = { 
                                    nameError?.let { 
                                        Text(
                                            text = stringResource(it), 
                                            color = Color.Red,
                                            style = MaterialTheme.typography.bodySmall
                                        ) 
                                    } 
                                }
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))

                            // Campo Edad
                            OutlinedTextField(
                                value = age,
                                onValueChange = { age = it },
                                label = { Text(stringResource(R.string.fields_age)) },
                                isError = ageError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MindBoostPrimary,
                                    unfocusedBorderColor = MindBoostText.copy(alpha = 0.3f),
                                    focusedTextColor = MindBoostText,
                                    unfocusedTextColor = MindBoostText
                                ),
                                shape = RoundedCornerShape(12.dp),
                                supportingText = { 
                                    ageError?.let { 
                                        Text(
                                            text = stringResource(it), 
                                            color = Color.Red,
                                            style = MaterialTheme.typography.bodySmall
                                        ) 
                                    } 
                                }
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))

                            // Campo Horas Disponibles
                            OutlinedTextField(
                                value = availableHours,
                                onValueChange = { availableHours = it },
                                label = { Text(stringResource(R.string.fields_available_hours)) },
                                isError = hoursError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MindBoostPrimary,
                                    unfocusedBorderColor = MindBoostText.copy(alpha = 0.3f),
                                    focusedTextColor = MindBoostText,
                                    unfocusedTextColor = MindBoostText
                                ),
                                shape = RoundedCornerShape(12.dp),
                                supportingText = { 
                                    hoursError?.let { 
                                        Text(
                                            text = stringResource(it), 
                                            color = Color.Red,
                                            style = MaterialTheme.typography.bodySmall
                                        ) 
                                    } 
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones de acción en modo edición
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Botón Guardar
                        Button(
                            onClick = {
                                // Validaciones
                                val nameValid = validateName(name)
                                val ageValid = validateAge(age)
                                val hoursValid = validateHours(availableHours)

                                nameError = nameValid.second
                                ageError = ageValid.second
                                hoursError = hoursValid.second

                                if (nameValid.first && ageValid.first && hoursValid.first) {
                                    userId?.let { uid ->
                                        val updatedUser = User(
                                            name = name,
                                            age = age.toInt(),
                                            gender = user!!.gender,
                                            availableHours = availableHours.toInt(),
                                            habits = user!!.habits,
                                            streakCount = user!!.streakCount
                                        )
                                        db.collection(USERS_COLLECTION).document(uid)
                                            .set(updatedUser)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, profile_update, Toast.LENGTH_SHORT).show()
                                                user = updatedUser
                                                isEditing = false
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_SHORT).show()
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
                                text = stringResource(R.string.profile_save_button),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón Cancelar
                        Button(
                            onClick = { isEditing = false },
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
                                text = stringResource(R.string.profile_cancel_button),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    // Modo de visualización
                    user?.let { u ->
                        // Información del perfil
                        ProfileInfoSection(
                            title = "Información Personal",
                            items = listOf(
                                ProfileInfoItem(
                                    icon = Icons.Default.Person,
                                    label = stringResource(R.string.profile_screen_name),
                                    value = u.name,
                                    color = MindBoostPrimary
                                ),
                                ProfileInfoItem(
                                    icon = Icons.Default.Cake,
                                    label = stringResource(R.string.profile_screen_age),
                                    value = u.age.toString(),
                                    color = Color(0xFFE91E63)
                                ),
                                ProfileInfoItem(
                                    icon = Icons.Default.Wc,
                                    label = stringResource(R.string.profile_screen_gender),
                                    value = u.gender,
                                    color = Color(0xFF9C27B0)
                                ),
                                ProfileInfoItem(
                                    icon = Icons.Default.Schedule,
                                    label = stringResource(R.string.profile_screen_hours),
                                    value = u.availableHours.toString(),
                                    color = Color(0xFF00BCD4)
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Hábitos del usuario
                        ProfileInfoSection(
                            title = "Mis Hábitos",
                            items = listOf(
                                ProfileInfoItem(
                                    icon = Icons.Default.CheckCircle,
                                    label = "Hábitos registrados",
                                    value = "${u.habits.size} hábitos",
                                    color = Color(0xFF4CAF50)
                                )
                            )
                        )

                        if (u.habits.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.9f)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Lista de hábitos:",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MindBoostText,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    
                                    u.habits.forEach { habit ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Circle,
                                                contentDescription = "Hábito",
                                                tint = MindBoostSecondary,
                                                modifier = Modifier.size(8.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = habit,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MindBoostText
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botones de acción
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Botón Editar
                        Button(
                            onClick = { isEditing = true },
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
                                text = stringResource(R.string.profile_edit_button),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón Cerrar Sesión
                        Button(
                            onClick = {
                                auth.signOut()
                                navController.navigate("login") { popUpTo("profile") { inclusive = true } }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD32F2F),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.profile_logout_button),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Frase motivacional
                Text(
                    text = "Tu perfil es el reflejo de tu compromiso 🌟",
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
}

@Composable
fun ProfileInfoSection(
    title: String,
    items: List<ProfileInfoItem>
) {
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MindBoostText,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            items.forEach { item ->
                ProfileInfoRow(item = item)
                if (item != items.last()) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(item: ProfileInfoItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = item.color,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Información
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MindBoostText.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.value,
                style = MaterialTheme.typography.titleMedium,
                color = MindBoostText,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

data class ProfileInfoItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val value: String,
    val color: Color
)