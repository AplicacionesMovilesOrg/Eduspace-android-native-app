# 📚 Eduspace - Educational Space Management

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple.svg?style=flat&logo=kotlin)](http://kotlinlang.org)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg?style=flat&logo=android)](https://www.android.com/)
[![MinSDK](https://img.shields.io/badge/Min%20SDK-24-green.svg)](https://developer.android.com/about/versions/nougat)
[![TargetSDK](https://img.shields.io/badge/Target%20SDK-36-green.svg)](https://developer.android.com/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2025.10.00-blue.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)

Eduspace is a modern Android application built with **Jetpack Compose** and **Clean Architecture** for managing educational spaces, classrooms, meetings, and teachers efficiently.

## ✨ Features

### 🔐 Authentication
- Secure user login and registration
- Session persistence (stay logged in)
- Automatic session management

### 🏫 Classroom Management
- Full CRUD operations for classrooms
- Classroom details with resource tracking
- Teacher assignment to classrooms
- Real-time classroom information

### 📦 Resource Management
- Add, edit, and delete classroom resources
- Predefined resource types:
  - 💻 Computer
  - 📽️ Projector
  - 🪑 Desk/Chair
  - 📋 Whiteboard
  - 💻 Laptop/Tablet
  - 🖨️ Printer/Scanner
  - 🎤 Microphone
- Resource count tracking

### 👥 Meeting Management
- Create meetings for specific classrooms
- Add teachers as participants
- Filter available teachers (exclude already added)
- Edit and delete meetings
- View meeting details with participant list

### 👨‍🏫 Teacher Management
- View all registered teachers
- Create new teacher profiles
- Teacher information display
- Search and filter capabilities

### 🌍 Internationalization (i18n)
- **Full bilingual support**: English 🇺🇸 & Spanish 🇪🇸
- Language selector in sidebar
- Instant language switching
- Persistent language preference

### 🎨 Modern UI/UX
- **100% Jetpack Compose** - No XML layouts
- **Material 3 Design** system
- Beautiful gradient backgrounds
- Smooth animations and transitions
- Responsive layout
- Edge-to-edge display

## 🏗️ Architecture

Eduspace follows **Clean Architecture** principles with a modular, feature-based structure:

```
app/
├── core/                          # Core functionality
│   ├── data/                      # Data layer (SessionManager, LanguagePreferences)
│   ├── di/                        # Dependency injection (Hilt modules)
│   ├── navigation/                # App navigation
│   ├── ui/                        # Shared UI components & theme
│   └── utils/                     # Utility classes & helpers
│
└── features/                      # Feature modules
    ├── auth/                      # Authentication
    │   ├── data/                  # Data sources & repositories
    │   ├── domain/                # Business logic & models
    │   └── presentation/          # UI screens & ViewModels
    │
    ├── classrooms/                # Classroom management
    │   ├── data/
    │   ├── domain/
    │   └── presentation/
    │
    ├── meetings/                  # Meeting management
    ├── teachers/                  # Teacher management
    ├── shared_spaces/             # Shared spaces (in development)
    ├── home/                      # Home dashboard
    └── menu/                      # Navigation menu logic
```

### 📐 Architecture Layers

#### **Data Layer**
- Repository pattern implementation
- Retrofit for API communication
- DataStore for local persistence
- DTO to Domain model mapping

#### **Domain Layer**
- Business logic
- Use cases
- Domain models
- Repository interfaces

#### **Presentation Layer**
- Jetpack Compose UI
- ViewModels with StateFlow
- UiState sealed class for state management
- Hilt dependency injection

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material 3** - Design system
- **Coroutines & Flow** - Asynchronous programming
- **Hilt** - Dependency injection

### Jetpack Components
- **Navigation Compose** - In-app navigation
- **ViewModel** - Lifecycle-aware state management
- **DataStore** - Data persistence
- **Room** - Local database (configured, not actively used)

### Networking
- **Retrofit** - REST API client
- **Gson** - JSON serialization
- **OkHttp** - HTTP client

### Other Libraries
- **Coil** - Image loading
- **KSP** - Kotlin Symbol Processing

## 📱 Screenshots

> Add your app screenshots here

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 11 or higher
- Android SDK 24+
- Gradle 8.9+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/eduspace-android.git
   cd eduspace-android
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Build the project**
   ```bash
   gradle build
   ```

4. **Run on emulator or device**
   - Click the "Run" button in Android Studio
   - Or use: `gradle assembleDebug`

### Configuration

The app connects to the backend API at:
```
https://eduspace-platform-production-e783.up.railway.app/api/v1/
```

To change the API URL, modify `core/di/RemoteModule.kt`:
```kotlin
@Provides
@Singleton
fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("YOUR_API_URL_HERE")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
```

## 🔧 Build Commands

```bash
# Clean build
gradle clean

# Build debug APK
gradle assembleDebug

# Build release APK
gradle assembleRelease

# Run unit tests
gradle test

# Run instrumented tests (requires device/emulator)
gradle connectedAndroidTest
```

## 📂 Project Structure Highlights

### Key Files

**Core:**
- `MyApplication.kt` - Application class with Hilt setup
- `MainActivity.kt` - Single activity with language support
- `SessionManager.kt` - Session & auth state management
- `LanguagePreferences.kt` - i18n preferences with DataStore

**Navigation:**
- `AppNav.kt` - Root navigation (Auth → Main)
- `EduSpaceNavigation.kt` - Main app navigation with drawer
- `Screen.kt` - Navigation route definitions

**Features:**
- `ClassroomDetailViewModel.kt` - Classroom detail logic
- `ClassroomResourcesViewModel.kt` - Resource management (separated for SRP)
- `MeetingDetailViewModel.kt` - Meeting management with participant filtering

## 🌐 API Integration

The app communicates with a RESTful backend API. Key endpoints:

### Authentication
- `POST /auth/sign-in` - User login
- `POST /auth/sign-up` - User registration

### Classrooms
- `GET /teachers/{teacherId}/classrooms` - Get classrooms by teacher
- `POST /teachers/{teacherId}/classrooms` - Create classroom
- `PUT /classrooms/{id}` - Update classroom
- `DELETE /classrooms/{id}` - Delete classroom

### Resources (Nested)
- `GET /classrooms/{classroomId}/resources` - List resources
- `POST /classrooms/{classroomId}/resources` - Create resource
- `PUT /classrooms/{classroomId}/resources/{resourceId}` - Update resource
- `DELETE /classrooms/{classroomId}/resources/{resourceId}` - Delete resource

### Meetings
- `GET /administrators/{adminId}/meetings` - Get all meetings
- `POST /administrators/{adminId}/classrooms/{classroomId}/meetings` - Create meeting
- `PUT /meetings/{id}` - Update meeting
- `DELETE /meetings/{id}` - Delete meeting
- `POST /meetings/{meetingId}/teachers/{teacherId}` - Add teacher to meeting
- `DELETE /meetings/{meetingId}/teachers/{teacherId}` - Remove teacher from meeting

## 🎯 Key Features Implementation

### Clean Architecture Pattern
```kotlin
// Domain Layer - Business logic
interface ClassroomsRepository {
    suspend fun getClassroomById(id: Int): Classroom?
}

// Data Layer - Implementation
class ClassroomsRepositoryImpl @Inject constructor(
    private val service: ClassroomsService
) : ClassroomsRepository {
    override suspend fun getClassroomById(id: Int): Classroom? {
        // API call + error handling + mapping
    }
}

// Presentation Layer - UI State
@HiltViewModel
class ClassroomDetailViewModel @Inject constructor(
    private val repository: ClassroomsRepository
) : ViewModel() {
    private val _classroomState = MutableStateFlow<UiState<Classroom>>(UiState.Initial)
    val classroomState: StateFlow<UiState<Classroom>> = _classroomState.asStateFlow()
}
```

### State Management with UiState
```kotlin
sealed class UiState<out T> {
    object Initial : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

### Dependency Injection with Hilt
```kotlin
@HiltViewModel
class ClassroomDetailViewModel @Inject constructor(
    private val classroomsRepository: ClassroomsRepository,
    private val teachersRepository: TeachersRepository,
    private val meetingsRepository: MeetingsRepository,
    private val sessionManager: SessionManager
) : ViewModel()
```

## 🌍 Internationalization

String resources are organized in:
- `res/values/strings.xml` - English (default)
- `res/values-es/strings.xml` - Spanish

Usage:
```kotlin
@Composable
fun MyScreen() {
    Text(text = stringResource(R.string.classroom_title))
}
```

Language switching is handled automatically via:
- `LanguagePreferences` - Saves user preference
- `LocaleHelper` - Applies locale changes
- `MainActivity.attachBaseContext()` - Configures locale on startup

## 🧪 Testing

### Unit Tests
Located in `app/src/test/`
```bash
gradle test
```

### Instrumented Tests
Located in `app/src/androidTest/`
```bash
gradle connectedAndroidTest
```

## 📝 Code Style

This project follows:
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 🙏 Acknowledgments

- Built with ❤️ using Jetpack Compose
- Material Design 3 components
- Clean Architecture principles
- Backend API by [Your Team Name]

## 📞 Contact

For questions or support, please contact:
- Email: your.email@example.com
- GitHub Issues: [Project Issues](https://github.com/yourusername/eduspace-android/issues)

---

**Made with** ❤️ **and** ☕ **by [Your Team/Name]**
