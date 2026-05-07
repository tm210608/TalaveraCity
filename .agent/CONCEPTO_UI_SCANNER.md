# Concepto Visual: Escáner de Cerámica (Curador de Experiencia)

Para diferenciar a **EboraAzule**, el escáner no debe parecer una app de cámara genérica, sino una ventana al arte de Talavera.

## Identidad Visual "Azul Cobalto"
- **Marco de Enfoque**: En lugar del cuadrado estándar, usaremos una cenefa animada inspirada en la "Serie de la Palma" (tradicional de Talavera).
- **Tipografía**: Títulos en una Serif elegante (ej. Playfair Display o similar) para evocar historia, y Sans Serif moderna para legibilidad.
- **Micro-interacciones**: Al reconocer una pieza, el marco debe "pintarse" de azul cobalto con un efecto de acuarela.

## UI Consciente del Contexto (Stage-Aware)
Propongo que la pantalla de inicio sea dinámica:
- **Mañana**: Destacar rutas de senderismo y museos (luz suave, tonos tierra).
- **Tarde**: Destacar eventos culturales y talleres (tonos vibrantes).
- **Cerca de un Punto de Interés**: Aparecerá una "Quick Action" para abrir el escáner inmediatamente.

## Ejemplo de Componente Compose (Pseudocódigo)
```kotlin
@Composable
fun CeramicScannerFrame(modifier: Modifier = Modifier) {
    Box(modifier) {
        // Marco con textura de cerámica
        Image(painterResource(R.drawable.cenefa_talavera), contentDescription = null)
        // Animación de escaneo con gradiente azul
        ScanningBeam(color = Color(0xFF0047AB)) 
    }
}
```

---

## Próximo Paso para el Artesano de Android
Implementar un prototipo de la pantalla de bienvenida con este enfoque dinámico usando `Material3 Adaptive`.
