# Especificación Técnica 01: Infraestructura Android 15 (Artesano de Android)

Para cumplir con los estándares de **Android 15 (API 35/37)** y preparar el terreno para las funcionalidades de IA, establezco la siguiente hoja de ruta técnica inmediata.

## 1. Edge-to-Edge por Defecto (Requisito Android 15)
Android 15 obliga a que las apps sean "Edge-to-Edge". Debemos asegurar que todos los Composables manejen `WindowInsets`.
- **Acción**: Actualizar `MainActivity.kt` para usar `enableEdgeToEdge()`.
- **Acción**: Revisar `Scaffold` en todas las pantallas para usar `innerPadding` correctamente.

## 2. Preparación de IA Local (Gemini Nano & ML Kit)
- **ML Kit Object Detection**: Añadiremos la dependencia de `com.google.mlkit:object-detection-custom` para procesar el escaneo de cerámica localmente.
- **Gemini Nano**: Investigar el uso de `AICore` para generar resúmenes históricos de las piezas detectadas sin salir de la app.

## 3. Transiciones de Elementos Compartidos (Shared Elements)
Con Compose 1.7+, usaremos `Modifier.sharedElement()` para que, al seleccionar un evento o pieza escaneada, la imagen "vuele" a su posición en la pantalla de detalles.

## 4. Estabilidad y Rendimiento
- **Baseline Profiles**: Configurar la generación de perfiles para reducir el tiempo de primer inicio (Jank-free).
- **ADPF (Android Dynamic Performance Framework)**: Implementar monitoreo térmico para ajustar la calidad de las animaciones del escáner si el dispositivo se calienta.

---

## Próximos Pasos Coordinados
1. **Artesano**: Aplicar `enableEdgeToEdge()` en `MainActivity.kt`.
2. **Curador**: Definir el flujo de navegación de la "Colección de Azulejos".
3. **Vigía**: Buscar socios locales o bases de datos abiertas de cerámica para alimentar el escáner.
