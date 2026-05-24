# Especificación Técnica: Interfaz de Línea de Comandos (spec-cli)

Este documento define la arquitectura técnica, el stack tecnológico y el protocolo de interacción de la interfaz de terminal (TUI) para el sistema **myUniverse**, integrando el plan de actualización visual.

## 1. Objetivos Técnicos
- **Interfaz Basada en Rejilla (Grid-Based):** Transicionar de una salida de texto plana a una interfaz visual dirigida por datos donde cada `Espacio` ocupa dimensiones reales en un mapa ASCII.
- **Interactividad en Tiempo Real:** Implementar un bucle de eventos que permita la manipulación directa de objetos (mover, redimensionar) con retroalimentación visual inmediata.
- **Gestión de Diálogos Modales:** Utilizar ventanas superpuestas para la entrada de datos, evitando la ruptura del flujo visual del mapa.

## 2. Stack Tecnológico
- **Runtime:** Java 11 o superior.
- **Motor de Terminal (Lanterna 3.1.x):**
    - `TerminalScreen`: Gestión de buffers y control de parpadeo.
    - `WindowBasedTextGUI`: Entorno para la gestión de ventanas lógicas y diálogos.
    - `TextGraphics`: Dibujo de primitivas (bordes, fondos y caracteres).
- **Persistencia:** Google Gson para la sincronización del modelo de datos jerárquico.

## 3. Arquitectura de la Interfaz (View Layer)

### 3.1 Componentes de Visualización
- **GridMapaRenderer:** Motor de renderizado que transforma la lista de `Espacio` en una rejilla 2D. 
    - Representa espacios mediante sus iniciales coloreadas.
    - Renderiza celdas vacías con puntos tenues (`·`).
- **MapaView:** Componente principal (State Machine) que gestiona los modos de operación (`NAVIGATE`, `MOVE`, `RESIZE`, `RECORRIDO_EDIT`).
- **Panel de Detalles:** Sección lateral que deserializa los atributos del objeto bajo el cursor en tiempo real.

### 3.2 Gestión de Diálogos
- **EspacioEditorDialog / EspacioCreatorDialog:** Diálogos modales para la captura de `nombre`, `tipo` (vía dropdown) y `descripcion`.
- **PlantaManagerDialog:** Interfaz dedicada a la gestión de la jerarquía del edificio (añadir/eliminar/renombrar plantas).

## 4. Protocolo de Comandos y Teclado

| Tecla | Acción en Modo Admin | Acción en Modo Visitante |
| :--- | :--- | :--- |
| `↑ ↓ ← →` | Desplazar cursor / Mover objeto (en modo Move) | Desplazar cursor / Navegación |
| `Enter` | Confirmar Acción / Abrir Editor | Ver Detalles del Espacio |
| `N` | Crear nuevo `Espacio` en posición actual | — |
| `E` | Editar `Espacio` seleccionado | — |
| `D` | Eliminar `Espacio` seleccionado (Confirmación) | — |
| `M` | Iniciar Modo Movimiento (Drag) | — |
| `R` | Iniciar Modo Redimensión | — |
| `+ / -` | Ajustar Ancho/Alto (solo en modo Resize) | — |
| `Tab` | Alternar entre Plantas | Alternar entre Plantas |
| `A / D` | Paso Anterior / Siguiente en Recorrido | Paso Anterior / Siguiente en Recorrido |
| `Q / Esc` | Salir / Cancelar | Salir / Cancelar |

## 5. Modelo de Datos Extendido
Para soportar la renderización espacial, el modelo `Espacio` debe integrar:
- **Atributos Geométricos:**
    - `coordenadaX`: Posición en el eje horizontal.
    - `coordenadaY`: Posición en el eje vertical.
    - `ancho`: Extensión horizontal (mínimo 1).
    - `alto`: Extensión vertical (mínimo 1).
- **Jerarquía:** Todo espacio debe pertenecer a una `Planta`, que a su vez reside en un `Edificio` dentro de la `Universidad`.

## 6. Lógica de Negocio y Validación Visual

### 6.1 Detección de Colisiones (Collision Detection)
El sistema impedirá la creación, movimiento o redimensión de un espacio si este solapa el área de otro espacio existente en la misma planta. La validación se realiza mediante la intersección de rectángulos:
`A.x < B.x + B.w && A.x + A.w > B.x && A.y < B.y + B.h && A.y + A.h > B.y`

### 6.2 Esquema de Colores por Tipo
Para facilitar la identificación visual, se establece el siguiente mapeo ANSI:
- **AULA:** Azul (Blue)
- **LABORATORIO:** Cian (Cyan)
- **BIBLIOTECA:** Verde (Green)
- **CAFETERIA:** Amarillo (Yellow)
- **AUDITORIO:** Magenta (Magenta)
- **OFICINA:** Blanco (White)
- **BAÑO:** Gris (Gray)
- **OTRO:** Blanco tenue (Dim White)

## 7. Flujo de Usuario (Admin)
1. **Navegación:** El admin explora el mapa con las flechas.
2. **Creación:** Al pulsar `N`, se hereda la posición del cursor y se abre el diálogo de creación.
3. **Ajuste Espacial:** 
    - Con `M`, el espacio se "ancla" al cursor para reposicionarlo.
    - Con `R`, el admin ajusta los límites con `+/-` viendo una previsualización dinámica.
4. **Sincronización:** Cada cambio exitoso actualiza el buffer de la terminal y dispara una escritura atómica en el archivo JSON.
