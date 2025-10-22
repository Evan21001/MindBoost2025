# 🏗️ MindBoost 2025 - Documentación Técnica

## 📋 Arquitectura del Sistema

### **Patrón de Arquitectura**
- **MVVM (Model-View-ViewModel)** con Jetpack Compose
- **Repository Pattern** para gestión de datos
- **Single Source of Truth** con Firebase Firestore
- **Reactive Programming** con Kotlin Coroutines

### **Componentes Principales**

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
├─────────────────────────────────────────────────────────────┤
│  Jetpack Compose UI  │  ViewModels  │  Navigation  │ State │
├─────────────────────────────────────────────────────────────┤
│                    Business Logic Layer                     │
├─────────────────────────────────────────────────────────────┤
│  Use Cases  │  Repositories  │  Data Models  │ Validation │
├─────────────────────────────────────────────────────────────┤
│                      Data Layer                             │
├─────────────────────────────────────────────────────────────┤
│  Firebase Auth  │  Firestore  │  Local Storage  │ Cache   │
└─────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Estructura de Base de Datos

### **Firestore Collections**

#### **Users Collection**
```json
{
  "users": {
    "{userId}": {
      "name": "string",
      "email": "string", 
      "age": "number",
      "gender": "string",
      "availableHours": "number",
      "habits": ["string"],
      "profileHabits": ["string"],
      "streakCount": "number",
      "lastLogDate": "string",
      "createdAt": "timestamp",
      "updatedAt": "timestamp"
    }
  }
}
```

#### **Habit Logs Subcollection**
```json
{
  "users/{userId}/habitLogs": {
    "{date}": {
      "habit1": "boolean",
      "habit2": "boolean",
      "duration_habit1": "string",
      "duration_habit2": "string", 
      "notes": "string",
      "timestamp": "timestamp"
    }
  }
}
```

#### **Achievements Collection**
```json
{
  "achievements": {
    "{userId}": {
      "streak_3": "boolean",
      "streak_7": "boolean", 
      "avg_80": "boolean",
      "unlockedAt": {
        "streak_3": "timestamp",
        "streak_7": "timestamp",
        "avg_80": "timestamp"
      }
    }
  }
}
```

---

## 🎨 Sistema de Diseño

### **Paleta de Colores MindBoost**
```kotlin
// Colores principales
val MindBoostBackground = Color(0xFFF4F6FB)    // Gris azulado claro
val MindBoostPrimary = Color(0xFF4A90E2)        // Azul suave
val MindBoostSecondary = Color(0xFF50E3C2)      // Verde agua
val MindBoostAccent = Color(0xFF7B61FF)         // Violeta
val MindBoostText = Color(0xFF1A1A1A)           // Gris oscuro

// Variaciones
val MindBoostPrimaryLight = Color(0xFF6BA3E8)
val MindBoostSecondaryLight = Color(0xFF6FE6C8)
val MindBoostAccentLight = Color(0xFF8B7AFF)
```

### **Tipografía**
```kotlin
val Typography = Typography(
    displayLarge = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
    headlineLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
    titleLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal)
)
```

### **Espaciado**
```kotlin
// Espaciado consistente
val spacing = 24.dp    // Horizontal padding
val spacingSmall = 8.dp
val spacingMedium = 16.dp
val spacingLarge = 32.dp
```

---

## 🔧 Configuración de Firebase

### **Authentication Rules**
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only access their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      // Habit logs subcollection
      match /habitLogs/{logId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }
    
    // Achievements are readable by authenticated users
    match /achievements/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

### **Firebase Configuration**
```kotlin
// build.gradle (app level)
implementation platform('com.google.firebase:firebase-bom:32.7.0')
implementation 'com.google.firebase:firebase-auth-ktx'
implementation 'com.google.firebase:firebase-firestore-ktx'
implementation 'com.google.firebase:firebase-messaging-ktx'
```

---

## 📊 Gráficos y Visualización

### **MPAndroidChart Configuration**
```kotlin
// Configuración del gráfico de barras
BarChart(context).apply {
    data = barData
    description.isEnabled = false
    axisRight.isEnabled = false
    
    // Eje Y izquierdo
    axisLeft.apply {
        axisMinimum = 0f
        granularity = 1f
        setDrawGridLines(true)
        gridLineWidth = 1f
        gridColor = Color(0xFFE0E0E0).hashCode()
        textSize = 11f
        textColor = MindBoostText.hashCode()
    }
    
    // Eje X
    xAxis.apply {
        granularity = 1f
        setDrawGridLines(false)
        textSize = 11f
        textColor = MindBoostText.hashCode()
        labelRotationAngle = -30f
    }
}
```

### **Sistema de Colores del Gráfico**
```kotlin
val colors = entries.map { entry ->
    val pct = if (habitLogs.isNotEmpty()) entry.y / habitLogs.size else 0f
    when {
        pct >= 0.8f -> Color(0xFF4CAF50).hashCode() // Verde éxito
        pct >= 0.6f -> Color(0xFF8BC34A).hashCode() // Verde claro
        pct >= 0.4f -> Color(0xFFFF9800).hashCode() // Naranja progreso
        pct >= 0.2f -> Color(0xFFFFC107).hashCode() // Amarillo mejora
        else -> Color(0xFFE57373).hashCode()        // Rojo claro
    }
}
```

---

## 🔔 Sistema de Notificaciones

### **NotificationUtils**
```kotlin
class NotificationUtils {
    companion object {
        fun sendImmediateNotification(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("MindBoost")
                .setContentText("¡Es hora de registrar tus hábitos!")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
                
            notificationManager.notify(1, notification)
        }
    }
}
```

### **ReminderReceiver**
```kotlin
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtils.sendImmediateNotification(context)
    }
}
```

---

## 🏆 Sistema de Logros

### **AchievementManager**
```kotlin
object AchievementManager {
    private var unlocked = mutableMapOf<String, Boolean>()
    private var lastUnlockedAchievement: Achievement? = null
    
    fun checkAchievements(userId: String, db: FirebaseFirestore, context: Context) {
        // Verificar racha de 3 días
        if (streakCount >= 3 && !unlocked["streak_3"]!!) {
            unlockAchievement("streak_3", context)
        }
        
        // Verificar racha de 7 días
        if (streakCount >= 7 && !unlocked["streak_7"]!!) {
            unlockAchievement("streak_7", context)
        }
        
        // Verificar promedio 80%
        if (averageCompletion >= 0.8f && !unlocked["avg_80"]!!) {
            unlockAchievement("avg_80", context)
        }
    }
}
```

---

## 🧪 Testing

### **Unit Tests**
```kotlin
@Test
fun `validateEmail should return true for valid email`() {
    val result = validateEmail("test@example.com")
    assertTrue(result.first)
    assertNull(result.second)
}

@Test
fun `validatePassword should return false for short password`() {
    val result = validatePassword("123")
    assertFalse(result.first)
    assertEquals(R.string.password_too_short, result.second)
}
```

### **Integration Tests**
```kotlin
@Test
fun `user registration should create user document in Firestore`() {
    runBlocking {
        val user = User(name = "Test", email = "test@example.com")
        val result = userRepository.createUser(user)
        assertTrue(result.isSuccess)
    }
}
```

---

## 🚀 Deployment

### **Build Configuration**
```kotlin
// build.gradle (app level)
android {
    compileSdk 34
    
    defaultConfig {
        applicationId "com.example.clase7"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0.0"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
```

### **ProGuard Rules**
```proguard
# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Kotlin Coroutines
-keep class kotlinx.coroutines.** { *; }
```

---

## 📈 Performance Optimization

### **Image Optimization**
- Usar WebP para imágenes
- Implementar lazy loading
- Optimizar tamaños de iconos

### **Memory Management**
- Usar `remember` para estado local
- Implementar `LaunchedEffect` para operaciones async
- Limpiar listeners en `DisposableEffect`

### **Network Optimization**
- Implementar cache local
- Usar `Flow` para datos reactivos
- Optimizar queries de Firestore

---

## 🔒 Security Considerations

### **Data Protection**
- Validación de entrada en cliente y servidor
- Encriptación de datos sensibles
- Reglas de seguridad de Firestore

### **Authentication Security**
- Validación de tokens JWT
- Rate limiting en endpoints
- Logout automático por inactividad

---

*Documentación técnica actualizada para MindBoost 2025 v1.0.0*
