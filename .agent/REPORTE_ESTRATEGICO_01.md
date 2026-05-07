# Reporte Estratégico 01: Innovación en Patrimonio (Vigía del Mercado)

Tras analizar los líderes del mercado (*Smartify*, *Google Arts & Culture*), propongo tres funcionalidades "Estrella" para **EboraAzule** que le darán ese toque senior y diferencial.

## 1. Escáner de Cerámica (Inspirado en Smartify)
- **Concepto**: Permitir al usuario apuntar con la cámara a una pieza de cerámica y obtener su historia, artesano y época.
- **Implementación Técnica**:
    - Uso de `CameraX` para la captura.
    - Integración de `ML Kit` (Image Labelling) o un modelo de `TensorFlow Lite` personalizado para reconocer patrones de Talavera (Serie Azul, Serie Mariposa, etc.).
- **Valor**: Transforma la app de un catálogo pasivo a una herramienta interactiva.

## 2. Colección "Mi Azulejo" (Gamificación)
- **Concepto**: Un álbum digital donde los usuarios coleccionan piezas escaneadas o visitadas.
- **Detalle**:
    - Al escanear una pieza real, se desbloquea un "Azulejo Digital" en el perfil.
    - Recompensas: Desbloquear contenido exclusivo o "insignias de maestro artesano" tras completar series.
- **Valor**: Fomenta la retención y la exploración física de la ciudad.

## 3. Guía "Susurros del Tajo" (Audio-Storytelling)
- **Concepto**: Audioguías inmersivas que se activan por proximidad (Geofencing).
- **Detalle**:
    - Narrativa en primera persona (ej. un maestro alfarero del siglo XVIII explicando su técnica).
    - Uso de `FusedLocationProvider` para activar el audio cuando el usuario está cerca de un punto de interés.
- **Valor**: Crea una conexión emocional con el patrimonio.

---

## Recomendación para el Curador de Experiencia
Diseñar una interfaz de cámara limpia que evoque precisión artesanal, con un marco que recuerde a las cenefas tradicionales de Talavera.

## Recomendación para el Artesano de Android
Investigar la viabilidad de un modelo local de ML Kit para reconocimiento de patrones básicos sin necesidad de servidor, maximizando la privacidad y velocidad.
