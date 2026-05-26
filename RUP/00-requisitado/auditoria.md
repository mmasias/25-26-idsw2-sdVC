# Informe de Auditoría y Evolución: myUniverse

<div align=right>

| [![](https://img.shields.io/badge/-Inicio-FFF?style=flat&logo=Emlakjet&logoColor=black)](/README.md) [![](https://img.shields.io/badge/-Especificación_Técnica-282c34?style=flat&logo=markdown&logoColor=white)](/documents/spec.md) [![](https://img.shields.io/badge/-myUniverse_IDSW1-FFF?style=flat&logo=github&logoColor=black)](https://github.com/Camila-Lesly/25-26-idsw1-sdr) [![](https://img.shields.io/badge/-Conversation_Log-FFF?style=flat&logo=LiveChat&logoColor=black)](/conversation-log.md) [![](https://img.shields.io/badge/-Requisitado-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/00-requisitado//auditoria.md) [![](https://img.shields.io/badge/-Análisis-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/01-analisis/Análisis.md) [![](https://img.shields.io/badge/-Diseño-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/02-diseño/Diseño.md) [![](https://img.shields.io/badge/-Desarrollo_&_Ejecución-FFF?style=flat&logo=Proton&logoColor=black)](/RUP/03-desarrollo/Desarrollo.md)
|:-:|

</div>

Este documento justifica los cambios estructurales realizados en el diseño del sistema y analiza la evolución desde la especificación inicial (detallada en el repositorio `prompt`) hasta la implementación actual.

## 1. Justificación: Cambio en el Diagrama de Contexto (Visitante)

El diagrama de contexto original trataba las acciones del visitante como estados aislados y secuenciales. Se ha rediseñado hacia un modelo **operativo y circular** por las siguientes razones:

- **Centralidad del Mapa:** En la implementación real, el estado `ESPACIO_ABIERTO` (el Mapa) es el núcleo de la experiencia. Casi todas las acciones (`verDetalles`, `verCercanos`, `cambiarEspacio`) ocurren sin salir de este estado visual.
- **Navegación Orgánica:** El nuevo diagrama reconoce la recursividad (bucle Espacio -> Espacio). Esto refleja fielmente que el visitante "descubre" el edificio moviéndose celda a celda, convirtiendo el movimiento en una transición de estado continua.
- **Flujo de Escape:** Se añadió la transición explícita hacia `RECORRIDOS_ABIERTOS` desde el espacio, permitiendo documentar la funcionalidad de la tecla 'T' (volver a tours) que antes era un "punto ciego" en el diseño.

## 2. Evolución de los Casos de Uso (Análisis vs. Diseño)

Comparando el detalle inicial en el repositorio `prompt` con el diseño final en este repositorio, se observan los siguientes cambios clave:

### 2.1 Ver Espacios Cercanos
- **Antes (Análisis/Prompt):** Se planteaba como una consulta a base de datos basada en distancias euclidianas complejas, asumiendo una lógica de "búsqueda" tradicional.
- **Ahora (Diseño/Código):** Se ha simplificado a un modelo de **Detección por Adyacencia**. El sistema escanea las celdas Norte, Sur, Este y Oeste de la posición actual del usuario. Esto es más eficiente para una terminal y reconoce que los espacios son cuadros de tamaño flexible (size-agnostic).

### 2.2 Ver Detalles
- **Antes (Análisis/Prompt):** Se detallaba como un caso de uso manual que requería una acción explícita (ej: pulsar Enter o un botón).
- **Ahora (Diseño/Código):** Es un proceso **automático y reactivo**. Al estar la aplicación dirigida por estados, el panel de detalles se refresca en cada ciclo de renderizado al detectar qué espacio hay bajo la posición actual, eliminando fricción para el usuario.

### 2.3 Buscar Espacio
- **Antes (Análisis/Prompt):** Se consideraba una funcionalidad core con una interfaz de entrada de texto.
- **Ahora (Diseño/Código):** Se ha marcado como **"No Implementado"** en el diseño final para ser honestos con el código entregado, donde la navegación visual por el mapa ha sustituido temporalmente a la búsqueda por comandos.

## 3. Conclusión de la Auditoría
La evolución del proyecto muestra una transición desde un **pensamiento orientado a formularios** (típico de aplicaciones web genéricas) hacia un **diseño orientado a estados y rejilla** (específico para una TUI - Terminal User Interface). Los cambios realizados garantizan que la documentación RUP no sea solo un ejercicio teórico, sino un mapa exacto de la arquitectura técnica construida.
