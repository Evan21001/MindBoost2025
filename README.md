# 🧠 MindBoost 2025

<div align="center">

![MindBoost Logo](https://img.shields.io/badge/MindBoost-2025-blue?style=for-the-badge&logo=android)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

**Transforma tu mente, transforma tu vida**

Una aplicación Android profesional para el seguimiento de hábitos con recomendaciones inteligentes impulsadas por IA.

[![GitHub stars](https://img.shields.io/github/stars/Evan21001/MindBoost2025?style=social)](https://github.com/Evan21001/MindBoost2025)
[![GitHub forks](https://img.shields.io/github/forks/Evan21001/MindBoost2025?style=social)](https://github.com/Evan21001/MindBoost2025)

</div>

---

## 📱 Capturas de Pantalla

<div align="center">

| Pantalla Principal | Estadísticas | Medallas |
|:---:|:---:|:---:|
| ![Home Screen](docs/screenshots/home.png) | ![Stats Screen](docs/screenshots/stats.png) | ![Medals Screen](docs/screenshots/medals.png) |

| Crear Hábitos | Perfil | Registro Diario |
|:---:|:---:|:---:|
| ![Create Habits](docs/screenshots/create_habits.png) | ![Profile](docs/screenshots/profile.png) | ![Daily Log](docs/screenshots/daily_log.png) |

</div>

---

## ✨ Características Principales

### 🎨 **Diseño Profesional**
- **Interfaz moderna** con Material Design 3
- **Paleta de colores consistente** MindBoost
- **Animaciones suaves** y transiciones profesionales
- **Diseño responsive** para diferentes tamaños de pantalla

### 🏠 **Pantalla Principal Inteligente**
- **Menú agrupado** por secciones lógicas
- **Navegación intuitiva** con iconos descriptivos
- **Acceso rápido** a todas las funcionalidades
- **Diseño limpio** y organizado

### 📊 **Estadísticas Avanzadas**
- **Gráfico de barras profesional** con MPAndroidChart
- **Sistema de colores inteligente** basado en progreso
- **Leyenda de colores** explicativa
- **Métricas detalladas** de rendimiento

### 🏆 **Sistema de Logros**
- **Medallas desbloqueables** por rachas y progreso
- **Diseño visual atractivo** con estados claros
- **Sistema de motivación** gamificado
- **Progreso visual** hacia objetivos

### 🌱 **Gestión de Hábitos**
- **Creación inteligente** con iconos únicos
- **Selección visual** con colores distintivos
- **Límites configurables** (máximo 4 hábitos)
- **Hábitos predeterminados** optimizados

### 🤖 **Recomendaciones IA** *(Próximamente)*
- **Motor de recomendaciones** con inteligencia artificial
- **Configuración de acceso anticipado**
- **Notificaciones inteligentes**
- **Análisis predictivo** de comportamiento

### 👤 **Perfil Completo**
- **Edición de información** personal
- **Sistema de rachas** visual
- **Estadísticas personales**
- **Gestión de hábitos** registrados

### 📝 **Registro Diario**
- **Interfaz intuitiva** para marcar hábitos
- **Notas personales** del día
- **Duración de actividades**
- **Validación inteligente**

### 🔐 **Autenticación Robusta**
- **Login/Registro** con Firebase
- **Validaciones completas** de formularios
- **Manejo de errores** profesional
- **Seguridad implementada**

---

## 🛠️ Tecnologías Utilizadas

### **Frontend**
- **Jetpack Compose** - UI moderna y declarativa
- **Material Design 3** - Sistema de diseño consistente
- **Kotlin** - Lenguaje de programación principal
- **Android Architecture Components** - Arquitectura robusta

### **Backend & Servicios**
- **Firebase Authentication** - Autenticación de usuarios
- **Firebase Firestore** - Base de datos NoSQL
- **Firebase Cloud Messaging** - Notificaciones push
- **Firebase Analytics** - Análisis de uso

### **Librerías Principales**
- **MPAndroidChart** - Gráficos profesionales
- **Kotlin Coroutines** - Programación asíncrona
- **Navigation Compose** - Navegación entre pantallas
- **ViewModel & LiveData** - Gestión de estado

### **Herramientas de Desarrollo**
- **Android Studio** - IDE principal
- **Gradle** - Sistema de build
- **Git** - Control de versiones
- **GitHub** - Repositorio y colaboración

---

## 🏗️ Arquitectura del Proyecto

```
app/
├── src/main/java/com/example/clase7/
│   ├── ui/theme/           # Sistema de diseño y colores
│   │   ├── Color.kt        # Paleta MindBoost
│   │   ├── Theme.kt        # Configuración de temas
│   │   └── Type.kt         # Tipografía
│   ├── models/             # Modelos de datos
│   │   ├── User.kt         # Modelo de usuario
│   │   └── Achievement.kt   # Modelo de logros
│   ├── screens/            # Pantallas principales
│   │   ├── HomeScreen.kt
│   │   ├── StatsScreen.kt
│   │   ├── MedalsScreen.kt
│   │   ├── CreateHabitScreen.kt
│   │   ├── ProfileScreen.kt
│   │   ├── DailyLogScreen.kt
│   │   ├── LoginScreen.kt
│   │   ├── RegisterScreen.kt
│   │   └── RecommendationsScreen.kt
│   ├── MainActivity.kt     # Actividad principal
│   ├── AchievementManager.kt # Gestión de logros
│   └── Validations.kt      # Validaciones de formularios
└── src/main/res/           # Recursos de la aplicación
    ├── drawable/           # Iconos y imágenes
    ├── values/             # Strings, colores, temas
    └── xml/                # Configuraciones XML
```

---

## 🚀 Instalación y Configuración

### **Prerrequisitos**
- Android Studio Arctic Fox o superior
- Android SDK API 24+
- Kotlin 1.8.0+
- Firebase project configurado

### **Pasos de Instalación**

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Evan21001/MindBoost2025.git
   cd MindBoost2025
   ```

2. **Configurar Firebase**
   - Crear proyecto en [Firebase Console](https://console.firebase.google.com)
   - Descargar `google-services.json`
   - Colocar en `app/` directory

3. **Configurar Android SDK**
   ```bash
   # Configurar ANDROID_HOME en tu sistema
   export ANDROID_HOME=$HOME/Android/Sdk
   export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
   ```

4. **Compilar y ejecutar**
   ```bash
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

### **Configuración de Firebase**

1. **Authentication**
   - Habilitar Email/Password
   - Configurar reglas de seguridad

2. **Firestore Database**
   - Crear colección `users`
   - Configurar reglas de acceso

3. **Cloud Messaging**
   - Configurar notificaciones push
   - Generar clave de servidor

---

## 📊 Funcionalidades Detalladas

### **🏠 Pantalla Principal**
- **Secciones organizadas**: Tu Progreso, Hábitos, Configuración
- **Navegación intuitiva**: Iconos descriptivos y colores temáticos
- **Acceso directo**: A todas las funcionalidades principales
- **Diseño moderno**: Gradientes y animaciones suaves

### **📈 Estadísticas**
- **Gráfico de barras profesional**: Con colores basados en rendimiento
- **Leyenda explicativa**: Verde (≥80%), Amarillo (≥40%), Rojo (<20%)
- **Métricas detalladas**: Racha actual, promedio diario, progreso total
- **Interactividad**: Zoom, pan y tooltips informativos

### **🏅 Sistema de Medallas**
- **3 tipos de logros**: Racha de 3 días, 7 días, promedio 80%
- **Estados visuales**: Desbloqueadas vs bloqueadas
- **Progreso visual**: Barra de progreso hacia objetivos
- **Motivación**: Sistema gamificado para mantener engagement

### **🌱 Gestión de Hábitos**
- **9 hábitos predeterminados**: Sueño, estudio, ejercicio, etc.
- **Iconos únicos**: Cada hábito con su color distintivo
- **Selección inteligente**: Máximo 4 hábitos, mínimo 1
- **Estados claros**: Seleccionado, disponible, original del perfil

### **🤖 Recomendaciones IA**
- **Configuración avanzada**: Acceso anticipado y notificaciones
- **Preview de funcionalidades**: Mock de recomendaciones futuras
- **Engagement**: Switches interactivos para preferencias
- **Roadmap claro**: Explicación de funcionalidades próximas

---

## 🎯 Casos de Uso

### **👨‍💼 Usuario Profesional**
- Seguimiento de hábitos de productividad
- Análisis de patrones de trabajo
- Optimización de rutinas diarias

### **🎓 Estudiante**
- Gestión de hábitos de estudio
- Seguimiento de progreso académico
- Motivación a través de logros

### **🏃‍♂️ Persona Fitness**
- Registro de ejercicio diario
- Seguimiento de hidratación
- Monitoreo de descanso

### **🧘‍♀️ Bienestar Mental**
- Práctica de meditación
- Registro de estado emocional
- Desarrollo de mindfulness

---

## 🔮 Roadmap Futuro

### **Fase 1 - IA Avanzada** *(Q2 2025)*
- [ ] Motor de recomendaciones con ML
- [ ] Análisis predictivo de comportamiento
- [ ] Sugerencias personalizadas en tiempo real
- [ ] Chatbot de motivación

### **Fase 2 - Social Features** *(Q3 2025)*
- [ ] Compartir logros en redes sociales
- [ ] Desafíos grupales
- [ ] Sistema de amigos y competencia
- [ ] Comunidad de usuarios

### **Fase 3 - Integración** *(Q4 2025)*
- [ ] Integración con wearables
- [ ] Sincronización con calendarios
- [ ] Integración con apps de salud
- [ ] API pública para desarrolladores

### **Fase 4 - Plataforma** *(2026)*
- [ ] Versión web
- [ ] Versión iOS
- [ ] Dashboard empresarial
- [ ] Analytics avanzados

---

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Por favor:

1. **Fork** el proyecto
2. **Crea** una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. **Abre** un Pull Request

### **Guidelines de Contribución**
- Sigue las convenciones de código Kotlin
- Añade tests para nuevas funcionalidades
- Actualiza la documentación según sea necesario
- Mantén el diseño consistente con MindBoost

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

---

## 👨‍💻 Autor

**Evan Tejada Duarte**
- GitHub: [@Evan21001](https://github.com/Evan21001)
- Email: evantejadaduarte@gmail.com
- LinkedIn: [Evan Tejada](https://linkedin.com/in/evan-tejada)

---

## 🙏 Agradecimientos

- **Jetpack Compose Team** - Por el increíble framework de UI
- **Firebase Team** - Por los servicios backend robustos
- **Material Design** - Por el sistema de diseño consistente
- **Android Community** - Por el apoyo y recursos compartidos

---

<div align="center">

**⭐ Si te gusta este proyecto, ¡dale una estrella! ⭐**

[![GitHub stars](https://img.shields.io/github/stars/Evan21001/MindBoost2025?style=social)](https://github.com/Evan21001/MindBoost2025)
[![GitHub forks](https://img.shields.io/github/forks/Evan21001/MindBoost2025?style=social)](https://github.com/Evan21001/MindBoost2025)

---

*Transforma tu mente, transforma tu vida* 🌱

</div>
