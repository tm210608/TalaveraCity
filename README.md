# TalaveraCity 🏺

TalaveraCity es una aplicación Android moderna diseñada para explorar y poner en valor el patrimonio cultural y la cerámica de **Talavera de la Reina** (Patrimonio Inmaterial de la Humanidad).

## 🚀 Características Principales

- **Agenda Cultural:** Mantente al día con los eventos y noticias oficiales de la ciudad.
- **Mapa de Patrimonio:** Explora los puntos de interés histórico con rutas guiadas.
- **Escaneo de Cerámica (AI):** Identifica piezas de cerámica de Talavera usando IA y descubre su historia.
- **Colección Personal:** Guarda tus eventos favoritos y las piezas que hayas descubierto.
- **Experiencia Inmersiva:** Diseño contemporáneo inspirado en los colores de la cerámica tradicional (Azul Cobalto, Crema).

## 🛠️ Stack Tecnológico (Estándares 2025)

- **Arquitectura:** Clean Architecture (Domain, Data, Presentation).
- **UI:** Jetpack Compose con Material 3 y Dynamic Color.
- **DI:** Dagger Hilt.
- **Programación:** Kotlin 2.2 con el compilador K2.
- **Persistencia:** Room (con KSP) y DataStore.
- **Networking:** Retrofit con kotlinx-serialization.
- **IA:** Integración con modelos de IA para narración histórica.

## 🏗️ Estructura del Proyecto

- `:domain`: Contiene las entidades de negocio, interfaces de repositorio y Casos de Uso (Use Cases).
- `:data`: Implementación de repositorios, fuentes de datos (Room, Retrofit) y mappers.
- `:presentation`: (Dentro de `app`) ViewModels (UDF), Composables y Navegación segura.

## 📦 Instalación y Configuración

1. Clona el repositorio.
2. Abre el proyecto en **Android Studio Ladybug (o superior)**.
3. Sincroniza Gradle y ejecuta la aplicación.
4. *Nota:* Asegúrate de configurar una API Key válida de Google Maps en `strings.xml`.

## 🧪 Testing

El proyecto incluye una suite de pruebas base:
- **Unit Tests:** JUnit 5 + MockK para la lógica de negocio.
- **Integration Tests:** Turbine para flujos de coroutines.

---
Desarrollado con ❤️ para la ciudad de la cerámica.
