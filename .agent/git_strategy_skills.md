# Estrategia de Ramas (Git Flow Senior)

## Misión
Garantizar la estabilidad de la rama `master` mediante un flujo de trabajo basado en ramas de características (`feature branches`), permitiendo experimentación segura y reversión rápida de errores.

## Estructura de Ramas
1. **`master` / `main`**: Código estable y verificado. Solo se actualiza mediante merges exitosos.
2. **`feature/[ID-Ticket]-[Nombre]`**: Ramas temporales para desarrollar tareas específicas de `TICKETS.md`.
    - Ejemplo: `feature/EA-201-vision-infra`
3. **`fix/[ID-Ticket]-[Nombre]`**: Ramas urgentes para corregir errores encontrados en `master`.

## Protocolo de Trabajo
1. **Crear Rama**: Antes de cada ticket, crear rama desde `master`.
   ```bash
   git checkout -b feature/EA-XXX-nombre-tarea
   ```
2. **Desarrollo y Pruebas**: Trabajar en la rama y ejecutar `./gradlew compileDebugKotlin`.
3. **Merge Seguro**: Una vez verificado, volver a `master` y fusionar.
   ```bash
   git checkout master
   git merge feature/EA-XXX-nombre-tarea
   ```
4. **Limpieza**: Borrar la rama local tras el merge exitoso.

## Recuperación ante Fallos
Si una rama corrompe la lógica, simplemente se descarta y se vuelve a `master` (rama limpia), protegiendo la integridad del proyecto.
