package com.example.clase7

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clase7.ui.theme.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val loginButtonText = stringResource(R.string.login_screen_login_button)
    val registerButtonText = stringResource(R.string.login_screen_register_button)
    val loginSuccessMsg = stringResource(R.string.login_screen_success)
    val loginFailMsg = stringResource(R.string.login_screen_fail_message)

    val context = LocalContext.current
    val activity = LocalView.current.context as Activity
    val auth = Firebase.auth

    var stateEmail by remember { mutableStateOf("") }
    var statePassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var emailError: Int? by remember { mutableStateOf(null) }
    var passwordError: Int? by remember { mutableStateOf(null) }

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Logo y título principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo/Ícono principal
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
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "MindBoost",
                            tint = MindBoostPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Text(
                        text = "MindBoost",
                        style = MaterialTheme.typography.displayMedium,
                        color = MindBoostPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = stringResource(R.string.login_screen_text),
                        style = MaterialTheme.typography.titleLarge,
                        color = MindBoostText,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Transforma tu mente, transforma tu vida",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MindBoostText.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Formulario de login
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Credenciales",
                        style = MaterialTheme.typography.titleLarge,
                        color = MindBoostText,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Campo Email
                    OutlinedTextField(
                        value = stateEmail,
                        leadingIcon = { 
                            Icon(
                                Icons.Default.Email, 
                                contentDescription = null,
                                tint = MindBoostPrimary
                            ) 
                        },
                        onValueChange = { stateEmail = it },
                        label = { Text(stringResource(R.string.fields_email)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None),
                        isError = emailError != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MindBoostPrimary,
                            unfocusedBorderColor = MindBoostText.copy(alpha = 0.3f),
                            focusedTextColor = MindBoostText,
                            unfocusedTextColor = MindBoostText
                        ),
                        supportingText = {
                            emailError?.let { 
                                Text(
                                    text = stringResource(id = it), 
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall
                                ) 
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Password
                    OutlinedTextField(
                        value = statePassword,
                        leadingIcon = { 
                            Icon(
                                Icons.Default.Lock, 
                                contentDescription = null,
                                tint = MindBoostPrimary
                            ) 
                        },
                        onValueChange = { statePassword = it },
                        label = { Text(stringResource(R.string.fields_password)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None),
                        isError = passwordError != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MindBoostPrimary,
                            unfocusedBorderColor = MindBoostText.copy(alpha = 0.3f),
                            focusedTextColor = MindBoostText,
                            unfocusedTextColor = MindBoostText
                        ),
                        supportingText = {
                            passwordError?.let { 
                                Text(
                                    text = stringResource(id = it), 
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall
                                ) 
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Iniciar Sesión
            Button(
                onClick = {
                    val emailValid = validateEmail(stateEmail)
                    val passwordValid = validatePassword(statePassword)

                    emailError = emailValid.second
                    passwordError = passwordValid.second

                    if (emailValid.first && passwordValid.first) {
                        isLoading = true
                        auth.signInWithEmailAndPassword(stateEmail, statePassword)
                            .addOnCompleteListener(activity) { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    Toast.makeText(context, loginSuccessMsg, Toast.LENGTH_SHORT).show()

                                    // Verificar si el perfil existe en Firestore
                                    val db = Firebase.firestore
                                    val currentUser = Firebase.auth.currentUser
                                    currentUser?.reload()?.addOnSuccessListener {
                                        db.collection("users").document(currentUser.uid).get()
                                            .addOnSuccessListener { doc ->
                                                val nextScreen = if (doc.exists()) "home" else "createprofile"
                                                navController.navigate(nextScreen) {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_SHORT).show()
                                            }
                                    }?.addOnFailureListener { e ->
                                        Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_SHORT).show()
                                    }

                                } else {
                                    Toast.makeText(context, loginFailMsg, Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        val combinedError = listOfNotNull(
                            emailValid.second?.let { context.getString(it) },
                            passwordValid.second?.let { context.getString(it) }
                        ).joinToString(" ")
                        Toast.makeText(context, combinedError, Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MindBoostSecondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = loginButtonText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Registrar
            Button(
                onClick = { navController.navigate("register") },
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
                    text = registerButtonText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Frase motivacional
            Text(
                text = "Cada día es una nueva oportunidad para crecer 🌱",
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