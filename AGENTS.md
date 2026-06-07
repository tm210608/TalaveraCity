# EboraAzule - Guía para Agentes de IA

## Descripción General del Proyecto

**EboraAzule** es una aplicación Android en Compose completamente en español que muestra eventos culturales y patrimonio (tradiciones de cerámica de Talavera). Utiliza arquitectura limpia con inyección de dependencias manual, Room para persistencia, Retrofit para APIs, y Navigation3 para enrutamiento.

**SDK Objetivo**: 37 | **SDK Mínimo**: 24 | **Kotlin**: 2.2.10

---

## Arquitectura y Flujo de Datos

### Estructura en Capas
```
Capa de UI (pantallas Compose)
    ↓
ViewModel (gestión de estado con StateFlow)
    ↓
Repositorio (fuente única de verdad)
    ↓
Capa de Datos (BD local Room + API remota Retrofit)
```

### Archivos Clave y Responsabilidades
- **`AppContainer.kt`**: Inyección de dependencias manual—inicializa Retrofit, BD Room, y repositorio
- **`CulturalRepository.kt`**: Orquesta datos locales/remotos; sincroniza con el feed RSS oficial del Ayuntamiento de Talavera.
- **`NavGraph.kt`**: Toda la lógica de navegación; crea ViewModels con factory y gestiona pila de atrás
- **`Routes.kt`**: Interfaz sellada que define 5 destinos de navegación (Welcome→LandscapeCeramic→CeramicAccess→CulturalEvents→Exploration)

### Modelos de Datos y Conversiones
- **Modelo de dominio**: `CulturalEvent` (serializable, id/título/fecha/ubicación/imagenUrl/descripción)
- **Modelo de entidad**: `SavedEventEntity` (persistida en Room, incluye marca de tiempo `guardadoEn`)
- **Modelo DTO**: `CulturalEventDto` (mapeo remoto, definido pero sin usar aún)
- **Convertidores de extensión**: `toDomain()` y `toEntity()` en archivos a nivel de paquete

---

## Flujos de Trabajo de Desarrollo

### Construcción y Ejecución
```bash
./gradlew build                # Construcción completa con generación de código KSP
./gradlew installDebug         # Instala APK debug en dispositivo/emulador
./gradlew compileDebugKotlin   # Compilación rápida de Kotlin
```

### Generación de Código (KSP)
La construcción depende de KSP para:
- **Room**: `androidx.room.compiler` genera DAOs
- **Moshi**: `moshi-kotlin-codegen` genera adaptadores JSON

Al agregar nuevas entidades Room o modelos Moshi, KSP regenerará automáticamente en la siguiente construcción.

### Pruebas
- **Pruebas unitarias**: `src/test/java/` (ejecutar con `./gradlew test`)
- **Pruebas instrumentales**: `src/androidTest/java/` (ejecutar con `./gradlew connectedAndroidTest`)
- **Pruebas UI de Compose**: Usar `androidx.compose.ui.test.junit4`

---

## Convenciones Específicas del Proyecto

### Nombres e Idioma
- **Todo en español**: Strings de UI, nombres de variables, documentación (ej: "Eventos Culturales", "Guardar")
- **Estructura de paquetes**: `com.example.eboraazule` con subpaquetes por capa
- **Nombres de archivos**: Clases Kotlin en PascalCase, pantallas con sufijo "Screen"
- **Material3 + Iconos de Compose**: Usar `androidx.compose.material.icons.rounded.*` no iconos deprecados

### Patrón de Gestión de Estado
```kotlin
sealed class [Característica]UiState {
    object Cargando : [Característica]UiState()
    data class Exito(val datos: T) : [Característica]UiState()
    data class Error(val mensaje: String) : [Característica]UiState()
}

class [Característica]ViewModel(repo: Repositorio) : ViewModel() {
    private val _uiState = MutableStateFlow<[Característica]UiState>(Cargando)
    val uiState = _uiState.asStateFlow()
    
    fun obtenerDatos() {
        viewModelScope.launch {
            repositorio.obtenerDatos().fold(
                onSuccess = { _uiState.value = Exito(it) },
                onFailure = { _uiState.value = Error(it.message ?: "Desconocido") }
            )
        }
    }
}
```
**Siempre usar tipos de retorno `Result<T>`** del repositorio y manejar con `.fold()` en ViewModels.

### Patrón de Navegación
- Rutas definidas como interfaz sellada `Route : NavKey` con `@Serializable` en cada destino
- Pasar ViewModelFactory a todas las creaciones de ViewModel en NavGraph
- Gestión manual de pila: `backStack.add(Route.Siguiente)` y `backStack.removeAt(size-1)` para atrás

### Patrón de UI en Compose
- Las **pantallas** son funciones `@Composable` que reciben callbacks de navegación y ViewModel
- Usar `collectAsState()` para suscribirse a StateFlow del ViewModel
- Factorizar componentes reutilizables en funciones `@Composable` separadas en el mismo archivo o extraídas
- Material3 `Scaffold` con `topBar`/`bottomBar` para estructura
- Usar `ElevatedCard` con `RoundedCornerShape(20.dp)` para estilos de tarjeta consistentes

### Inyección de Dependencias (Manual, No Hilt)
```kotlin
val contenedor = (context.applicationContext as EboraApplication).contenedor
val viewModel = viewModel(factory = ViewModelFactory(contenedor.repositorio))
```
Todas las cadenas de dependencia fluyen a través del singleton `EboraApplication.contenedor`.

---

## Dependencias Críticas y Librerías

### Android Core y Compose
- `androidx.activity:activity-compose` (1.10.1): Punto de entrada para Compose
- `androidx.compose.bom` (2024.09.00): Gestor de versiones para Compose consistente
- `androidx.compose.material3`: Sistema de diseño
- `androidx.compose.material.icons.rounded`: Biblioteca de iconos
- `androidx.lifecycle:lifecycle-runtime-compose` (2.8.7): Integración de ciclo de vida en Compose

### Datos y Redes
- `androidx.room`: Base de datos local (versión 2.7.0)
- `com.squareup.retrofit2`: Cliente HTTP (2.12.0) con convertidor Moshi
- `com.squareup.moshi`: Serialización JSON (1.15.2, usa codegen KSP)
- `kotlinx.serialization`: Marco de serialización (1.9.0)

### Navegación y Ciclo de Vida
- `androidx.navigation3`: Navegación de próxima generación (1.1.1)
- `androidx.compose.material3.adaptive`: Diseños responsivos (1.2.0)
- `androidx.lifecycle:lifecycle-viewmodel-compose`: ViewModel en Compose (2.8.7)

### Utilidades
- `coil-compose` (2.7.0): Carga de imágenes (reemplaza Glide)
- `play-services-location` (21.3.0): APIs de GPS/ubicación
- `androidx.camera`: Integración Camera2 (1.5.0)
- `accompanist-permissions` (0.37.3): Manejo de permisos

### Construcción y Generación de Código
- **KSP** (2.3.5): Procesador de anotaciones
- **Plugin de Serialización Kotlin** (2.2.21)
- **AGP** (9.2.0): Android Gradle Plugin

---

## Tareas Comunes y Patrones de Implementación

### Agregar una Nueva Pantalla
1. Crear `[NombreCaracterística]Screen.kt` en `ui/screens/`
2. Definir ruta en la interfaz sellada `Routes.kt`
3. Crear `[NombreCaracterística]UiState` y `[NombreCaracterística]ViewModel` asociados en `ui/viewmodel/`
4. Agregar ViewModel a la sentencia `create()` en `ViewModelFactory`
5. Agregar entrada en `NavGraph.kt` con inyección de factory
6. Agregar callback de navegación en pantalla existente

### Obtener Datos Remotos
1. Agregar método a interfaz `CulturalApiService` con `@GET`/`@POST`
2. Agregar implementación mock/real en `CulturalRepository.obtenerEventos()`
3. Envolver con `Result.success()` o `Result.failure()`
4. ViewModel recopila con `.fold()` → actualización StateFlow
5. UI observa con `collectAsState()`

### Persistir Datos Localmente
1. Definir clase `@Entity` en `data/local/`
2. Crear interfaz `@Dao` con consultas en `EventDao`
3. Agregar entidad a declaración `@Database(entities = [...])` e incrementar `version`
4. Agregar convertidores `toDomain()`/`toEntity()`
5. Llamar métodos DAO desde Repositorio

### Manejar Permisos
Usar `accompanist-permissions`:
```kotlin
val estado = rememberPermissionState(Manifest.permission.CAMERA)
if (!estado.status.isGranted) {
    estado.launchPermissionRequest()
}
```

---

## Manejo de Errores y Mejores Prácticas

### Flujo de Errores Basado en Result
```kotlin
repositorio.obtenerEventos().fold(
    onSuccess = { eventos -> _uiState.value = Exito(eventos) },
    onFailure = { error -> _uiState.value = Error(error.message ?: "Desconocido") }
)
```

### Notas de Depuración
- **Datos simulados**: Actualmente devuelve eventos codificados con retraso de 1500ms en `obtenerEventosSimulados()`
- **URL base de Retrofit**: Establecida en placeholder `"https://api.example.com/"` en `AppContainer` — actualizar cuando API real esté disponible
- **Filtrado de Logcat**: Usar `adb logcat | grep -i eboraazule`

---

## Referencia de Organización de Archivos
```
app/src/
├── main/java/com/example/eboraazule/
│   ├── data/
│   │   ├── local/         # Entidades Room, DAOs, convertidores
│   │   ├── remote/        # Interfaces de servicio API, DTOs
│   │   ├── model/         # Modelos de dominio (clases de datos inmutables)
│   │   └── repository/    # Lógica de fuente única de verdad
│   ├── di/                # Configuración de inyección manual AppContainer
│   ├── ui/
│   │   ├── screens/       # Funciones pantalla Composable
│   │   ├── viewmodel/     # ViewModels y clases de estado de UI
│   │   └── theme/         # Definiciones de tema Material3
│   ├── navigation/        # Rutas, NavGraph
│   ├── EboraApplication.kt
│   └── MainActivity.kt
├── res/                   # Recursos XML (colores, strings, estilos)
└── AndroidManifest.xml
```

---

## Comandos Útiles
```bash
# Limpiar y reconstruir
./gradlew clean build

# Ejecutar linter
./gradlew lint

# Verificar dependencias
./gradlew dependencies

# Sincronizar con API de red (cuando API real esté lista)
./gradlew build --refresh-dependencies

# Formatear código Kotlin
./gradlew ktlintFormat
```

---

## Puntos Clave para Agentes de IA

### Antes de Hacer Cambios
1. Leer `AppContainer.kt` para entender cómo fluyen las dependencias
2. Revisar `Routes.kt` y `NavGraph.kt` para entender toda la estructura de navegación
3. Estudiar el patrón UiState/ViewModel en `EventsViewModel.kt`
4. Notar que TODO está en español (UI, código, comentarios)

### Al Agregar Características
1. Mantener consistencia con patrón Result<T> en repositorio
2. Siempre usar ViewModelFactory al crear ViewModels
3. Respetar la estructura de capas: UI → ViewModel → Repository → Data
4. Escribir textos en español siguiendo el estilo existente

### Evitar
- No usar Hilt (proyecto usa inyección manual)
- No cambiar nombres de rutas (están definidas en interfaz sellada)
- No mezclar lógica de negocio en Composables (usar ViewModels)
- No ignorar los convertidores `toDomain()`/`toEntity()`

---

**Última Actualización**: 7 de Mayo de 2026 | **ID de App**: com.example.eboraazule
