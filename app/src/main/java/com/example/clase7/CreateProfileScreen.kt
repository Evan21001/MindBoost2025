package com.example.clase7

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.clase7.ui.theme.USERS_COLLECTION

import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import com.example.clase7.models.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val activity = LocalView.current.context as Activity
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid


    // Strings
    val titleText = stringResource(R.string.create_profile_screen_title)
    val continueButtonText = stringResource(R.string.create_profile_screen_continue)
    val dataSavedMsg = stringResource(R.string.create_profile_screen_saved)

    // Campos del usuario
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var expandedGender by remember { mutableStateOf(false) }
    val genderOptions = listOf(
        stringResource(R.string.gender_male),
        stringResource(R.string.gender_female),
        stringResource(R.string.gender_other)
    )
    var availableHours by remember { mutableStateOf("") }

    // Errores
    var nameError: Int? by remember { mutableStateOf(null) }
    var ageError: Int? by remember { mutableStateOf(null) }
    var habitsError: Int? by remember { mutableStateOf(null) }
    var hoursError: Int? by remember { mutableStateOf(null) }

    // Selección de hábitos
    val habitsList = listOf(
        stringResource(R.string.habit_sleep),
        stringResource(R.string.habit_exercise),
        stringResource(R.string.habit_study),
        stringResource(R.string.habit_reading)
    )
    val selectedHabits = remember { mutableStateListOf<String>() }
    var showMaxHabitsToast by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = titleText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.icon_register_back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E88E5))
            )
        }
    ) { paddingValues ->

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color(0xFF43A047))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Guardando perfil...", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                        )
                    )
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = titleText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0D47A1)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.fields_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None),
                    isError = nameError != null,
                    supportingText = {
                        nameError?.let {
                            Text(
                                text = stringResource(it),
                                color = Color.Red
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Edad
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text(stringResource(R.string.fields_age)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None),
                    isError = ageError != null,
                    supportingText = {
                        ageError?.let {
                            Text(
                                text = stringResource(it),
                                color = Color.Red
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Género
                ExposedDropdownMenuBox(
                    expanded = expandedGender,
                    onExpandedChange = { expandedGender = !expandedGender }
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.fields_gender)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGender,
                        onDismissRequest = { expandedGender = false }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    expandedGender = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Horas disponibles
                OutlinedTextField(
                    value = availableHours,
                    onValueChange = { availableHours = it },
                    label = { Text(stringResource(R.string.fields_available_hours)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = hoursError != null,
                    supportingText = {
                        hoursError?.let {
                            Text(
                                text = stringResource(it),
                                color = Color.Red
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Selección de hábitos
                Text(
                    text = stringResource(R.string.create_profile_screen_select_habits),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0D47A1)
                )
                Spacer(modifier = Modifier.height(8.dp))

                habitsList.forEach { habit ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .toggleable(
                                value = selectedHabits.contains(habit),
                                onValueChange = {
                                    if (it) {
                                        if (selectedHabits.size < 2) {
                                            selectedHabits.add(habit)
                                        } else {
                                            showMaxHabitsToast = true
                                        }
                                    } else {
                                        selectedHabits.remove(habit)
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedHabits.contains(habit),
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF43A047),
                                uncheckedColor = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = habit, fontSize = 16.sp)
                    }
                }

                if (showMaxHabitsToast) {
                    Toast.makeText(
                        context,
                        stringResource(R.string.create_profile_screen_habits_limit),
                        Toast.LENGTH_SHORT
                    ).show()
                    showMaxHabitsToast = false
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botón Continuar
                Button(
                    onClick = {
                        val nameValid = validateName(name)
                        val ageValid = validateAge(age)
                        val habitsValid = validateHabits(selectedHabits)
                        val hoursValid = validateHours(availableHours)
                        val genderValid = gender.isNotEmpty()

                        nameError = nameValid.second
                        ageError = ageValid.second
                        habitsError = habitsValid.second
                        hoursError = hoursValid.second

                        if (nameValid.first && ageValid.first && habitsValid.first && genderValid && hoursValid.first) {
                            // Activar carga
                            isLoading = true

                            // Guardar datos en Firestore
                            userId?.let { uid ->
                                val userData = User(
                                    name = name,
                                    age = age.toInt(),
                                    gender = gender,
                                    availableHours = availableHours.toInt(),
                                    habits = selectedHabits
                                )
                                db.collection(USERS_COLLECTION)
                                    .document(uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        db.collection(USERS_COLLECTION).document(uid).get()
                                            .addOnSuccessListener { document ->
                                                val user = document.toObject(User::class.java)
                                                Toast.makeText(
                                                    context,
                                                    dataSavedMsg,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate("home") {
                                                    popUpTo("createprofile") { inclusive = true }
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                isLoading = false
                                                Toast.makeText(
                                                    context,
                                                    e.message ?: "Error",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            e.message ?: "Error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            // Mostrar mensaje combinado de errores
                            val combinedError = listOfNotNull(
                                nameValid.second?.let { context.getString(it) },
                                ageValid.second?.let { context.getString(it) },
                                habitsValid.second?.let { context.getString(it) },
                                if (!genderValid) context.getString(R.string.create_profile_screen_gender_required) else null,
                                hoursValid.second?.let { context.getString(it) }
                            ).joinToString(" ")
                            Toast.makeText(context, combinedError, Toast.LENGTH_SHORT).show()
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF43A047),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text(
                        text = continueButtonText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
