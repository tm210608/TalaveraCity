# Workflow Optimizer (Meta-Habilidad de Gestión de Contexto)

## Misión
Optimizar el consumo de tokens y asegurar la persistencia del razonamiento entre sesiones de IA mediante la gestión estructurada de "Snapshots" de contexto y tickets de seguimiento.

## Estrategias de Optimización
1. **Snapshots de Sesión**: Al final de cada hito, generar un resumen comprimido del estado actual, decisiones clave y siguiente paso inmediato.
2. **Modularización de Prompts**: Guardar prompts específicos para tareas recurrentes (ej: "Refactorizar pantalla", "Crear Test Unitario").
3. **Gestión de Tickets**: Mantener un archivo `TICKETS.md` con estados binarios (Hecho/Pendiente) para evitar re-análisis del progreso.

## Formato de Snapshot (Context Saver)
```markdown
### SNAPSHOT: [FECHA_ID]
- **Objetivo**: [Breve]
- **Estado Técnico**: [Ultimo Commit/Archivo modificado]
- **Contexto Crítico**: [Decisión técnica que no debe olvidarse]
- **Siguiente Prompt Sugerido**: [Instrucción para retomar]
```
