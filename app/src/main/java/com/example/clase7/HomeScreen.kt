package com.example.clase7

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clase7.notifications.NotificationUtils
import com.example.clase7.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, skipDailyRedirect: MutableState<Boolean>) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val alreadyCheckedDaily = rememberSaveable { mutableStateOf(false) }
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    LaunchedEffect(uid) {
        if (uid != null && !alreadyCheckedDaily.value && !skipDailyRedirect.value) {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            try {
                val doc = db.collection("users")
                    .document(uid)
                    .collection("habitLogs")
                    .document(today)
                    .get()
                    .await()

                if (!doc.exists()) {
                    alreadyCheckedDaily.value = true
                    navController.navigate("dailylog")
                } else {
                    alreadyCheckedDaily.value = true
                }
            } catch (e: Exception) {
                alreadyCheckedDaily.value = true
            }
        }
    }

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
            .background(MindBoostBackground)
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
            
            // Título principal
            Text(
                text = "MindBoost",
                style = MaterialTheme.typography.displayMedium,
                color = MindBoostPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Transforma tu mente, transforma tu vida",
                style = MaterialTheme.typography.bodyLarge,
                color = MindBoostText.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Sección: Tu Progreso
            SectionGroup(
                title = "🧠 Tu Progreso",
                items = listOf(
                    MenuItem(
                        icon = Icons.Default.BarChart,
                        title = stringResource(R.string.home_stats_button),
                        color = MindBoostPrimary,
                        onClick = { navController.navigate("stats") }
                    ),
                    MenuItem(
                        icon = Icons.Default.EmojiEvents,
                        title = stringResource(R.string.home_medals_button),
                        color = Color(0xFFFF9800),
                        onClick = { navController.navigate("medals") }
                    )
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección: Hábitos
            SectionGroup(
                title = "🌙 Hábitos",
                items = listOf(
                    MenuItem(
                        icon = Icons.Default.Add,
                        title = stringResource(R.string.home_create_habit_button),
                        color = Color(0xFF4CAF50),
                        onClick = { navController.navigate("createhabit") }
                    ),
                    MenuItem(
                        icon = Icons.Default.Lightbulb,
                        title = stringResource(R.string.home_recommendations_button),
                        color = Color(0xFFFFEB3B),
                        onClick = { navController.navigate("recommendations") }
                    )
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección: Configuración
            SectionGroup(
                title = "⚙️ Configuración",
                items = listOf(
                    MenuItem(
                        icon = Icons.Default.Person,
                        title = stringResource(R.string.home_profile_button),
                        color = MindBoostPrimary,
                        onClick = { navController.navigate("profile") }
                    ),
                    MenuItem(
                        icon = Icons.Default.Notifications,
                        title = stringResource(R.string.Test_notifications),
                        color = Color(0xFFFF9800),
                        onClick = {
                            NotificationUtils.sendImmediateNotification(context)
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Frase motivacional
            Text(
                text = "Pequeños hábitos, grandes resultados 🌱",
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

@Composable
fun SectionGroup(
    title: String,
    items: List<MenuItem>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Título de la sección
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MindBoostText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Elementos de la sección
        items.forEach { item ->
            MenuItemRow(item = item)
            if (item != items.last()) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MenuItemRow(item: MenuItem) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100),
        label = "press_scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                item.onClick()
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = item.color,
            modifier = Modifier.size(28.dp)
        )
        
        Spacer(modifier = Modifier.width(20.dp))
        
        // Texto
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
            color = MindBoostText,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        
        // Chevron para todos los elementos navegables
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Ir",
            tint = MindBoostText.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}

data class MenuItem(
    val icon: ImageVector,
    val title: String,
    val color: Color,
    val onClick: () -> Unit
)