# MindBoost 2025

Aplicación Android profesional para el seguimiento de hábitos con recomendaciones de inteligencia artificial.

## Descripción

MindBoost 2025 es una aplicación móvil desarrollada en Android que ayuda a los usuarios a formar y mantener hábitos positivos a través de un sistema de seguimiento intuitivo, estadísticas visuales y gamificación con medallas.

## Características Principales

### 🎯 Gestión de Hábitos
- Selección y personalización de hábitos predefinidos
- Seguimiento diario del progreso
- Máximo 4 hábitos activos simultáneamente
- 9 hábitos disponibles: Sueño, Estudio, Ejercicio, Hidratación, Meditación, Lectura, Hogar, Social, Otros

### 📊 Estadísticas y Análisis
- Gráficos de barras profesionales con colores basados en rendimiento
- Métricas detalladas: racha actual, promedio diario, progreso total
- Visualización interactiva con leyenda explicativa
- Codificación por colores: Verde (≥80%), Amarillo (≥40%), Rojo (<20%)

### 🏅 Sistema de Gamificación
- Sistema de medallas con 3 tipos de logros
- Desbloqueo progresivo de logros
- Barras de progreso hacia objetivos
- Racha de 3 días, 7 días, y promedio 80%

### 🔐 Autenticación
- Sistema de registro y login con Firebase
- Validaciones completas de formularios
- Manejo profesional de errores
- Gestión de perfil de usuario

### 🤖 Inteligencia Artificial
- Motor de recomendaciones con DeepSeek API
- Análisis predictivo de comportamiento
- Sugerencias personalizadas
- Funcionalidad de recomendaciones anticipadas

### 🔔 Notificaciones
- Recordatorios diarios personalizados
- Sistema de alarmas programadas
- Manejo de permisos de notificaciones
- Configuración flexible de recordatorios

## Tecnologías Utilizadas

### Frontend
- **Jetpack Compose** - UI moderna y declarativa
- **Material Design 3** - Sistema de diseño consistente
- **Kotlin** - Lenguaje de programación principal
- **Android Architecture Components** - Arquitectura robusta

### Backend & Servicios
- **Firebase Authentication** - Autenticación de usuarios
- **Firebase Firestore** - Base de datos NoSQL
- **Firebase Cloud Messaging** - Notificaciones push
- **DeepSeek API** - Motor de recomendaciones IA

### Librerías Principales
- **MPAndroidChart** - Gráficos profesionales
- **Kotlin Coroutines** - Programación asíncrona
- **Navigation Compose** - Navegación entre pantallas
- **ViewModel & LiveData** - Gestión de estado
- **Ktor** - Cliente HTTP para APIs

## Requisitos del Sistema

- **Android Studio** Arctic Fox o superior
- **Android SDK** API 29+
- **Kotlin** 1.9.0+
- **Gradle** 8.0+
- Proyecto Firebase configurado

## Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Evan21001/MindBoost2025.git
   cd MindBoost2025
   ```

2. **Configurar Firebase**
   - Crear proyecto en [Firebase Console](https://console.firebase.google.com/)
   - Descargar `google-services.json`
   - Colocar en el directorio `app/`

3. **Compilar el proyecto**
   ```bash
   ./gradlew build
   ```

4. **Ejecutar en dispositivo/emulador**
   ```bash
   ./gradlew installDebug
   ```

## Configuración de Firebase

### Authentication
- Habilitar Email/Password provider
- Configurar reglas de seguridad

### Firestore Database
- Crear colección `users`
- Configurar reglas de acceso:
  ```javascript
  match /users/{userId} {
    allow read, write: if request.auth != null && request.auth.uid == userId;
  }
  ```

### Cloud Messaging
- Configurar notificaciones push
- Generar clave de servidor FCM

## Arquitectura del Proyecto

```
app/
├── src/main/java/com/example/clase7/
│   ├── ui/theme/              # Sistema de diseño
│   │   ├── Color.kt           # Paleta de colores
│   │   ├── Theme.kt           # Configuración de temas
│   │   └── Type.kt            # Tipografía
│   ├── models/                # Modelos de datos
│   │   ├── User.kt            # Modelo de usuario
│   │   └── Achievement.kt     # Modelo de logros
│   ├── screens/               # Pantallas principales
│   │   ├── HomeScreen.kt
│   │   ├── StatsScreen.kt
│   │   ├── MedalsScreen.kt
│   │   ├── CreateHabitScreen.kt
│   │   ├── ProfileScreen.kt
│   │   ├── DailyLogScreen.kt
│   │   ├── LoginScreen.kt
│   │   ├── RegisterScreen.kt
│   │   └── RecommendationsScreen.kt
│   ├── MainActivity.kt        # Actividad principal
│   ├── AchievementManager.kt  # Gestión de logros
│   └── Validations.kt         # Validaciones
│   ├── data/
│   │   └── DeepSeekService.kt # Servicio IA
│   └── notifications/
│       ├── NotificationUtils.kt
│       └── ReminderReceiver.kt
└── src/main/res/              # Recursos
    ├── drawable/              # Iconos y imágenes
    ├── values/                # Strings, colores
    └── xml/                   # Configuraciones
```

## Funcionalidades Detalladas

### Pantalla de Inicio
- Vista consolidada del progreso
- Acceso rápido a todas las funcionalidades
- Diseño moderno con gradientes
- Navegación intuitiva con iconos descriptivos

### Estadísticas
- Visualización de datos con gráficos interactivos
- Análisis de tendencias de hábitos
- Comparación de rendimiento semanal
- Filtros por tipo de hábito

### Perfil de Usuario
- Gestión de información personal
- Selección de hábitos
- Configuración de preferencias
- Edición de datos del usuario

### Registro Diario
- Registro rápido de hábitos completados
- Interfaz intuitiva con iconos visuales
- Confirmación de acciones
- Manejo de días consecutivos

## Características Técnicas

- Arquitectura MVVM
- Programación reactiva con Kotlin Coroutines
- Manejo asíncrono de datos
- Persistencia local y en la nube
- UI moderna con Jetpack Compose
- Material Design 3

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Documentación Adicional

Para más información sobre el proyecto, consulta:
- [Lista de Cotejo](Lista_de_Cotejo_Actualizada].pdf)
- `TESTING_GUIDE.md` - Guía de pruebas
- `CONTRIBUTING.md` - Guía de contribución

