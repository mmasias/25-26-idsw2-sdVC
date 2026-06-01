# Especificación Técnica: myUniverse (spec-cli)

<div align=right>

| [![](https://img.shields.io/badge/-Inicio-FFF?style=flat&logo=Emlakjet&logoColor=black)](/README.md) [![](https://img.shields.io/badge/-Especificación_Técnica-282c34?style=flat&logo=markdown&logoColor=white)](/documents/spec.md) [![](https://img.shields.io/badge/-myUniverse_IDSW1-FFF?style=flat&logo=github&logoColor=black)](https://github.com/Camila-Lesly/25-26-idsw1-sdr) [![](https://img.shields.io/badge/-Conversation_Log-FFF?style=flat&logo=LiveChat&logoColor=black)](/conversation-log.md) [![](https://img.shields.io/badge/-Requisitado-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/00-requisitado//auditoria.md) [![](https://img.shields.io/badge/-Análisis-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/01-analisis/Análisis.md) [![](https://img.shields.io/badge/-Diseño-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/02-diseño/Diseño.md) [![](https://img.shields.io/badge/-Desarrollo_&_Ejecución-FFF?style=flat&logo=Proton&logoColor=black)](/RUP/03-desarrollo/Desarrollo.md)
|:-:|

</div>

Este documento define la arquitectura técnica, el stack tecnológico y el protocolo de interacción de la interfaz de terminal (TUI) para el sistema **myUniverse**, alineado con la implementación final en español.

## 1. Objetivos Técnicos
- **Interfaz Basada en Rejilla (Grid-Based):** Transición a una interfaz visual dirigida por datos donde cada `Espacio` ocupa dimensiones reales (coordenadas, ancho, alto) en un mapa ASCII.
- **Arquitectura N-Capas (MVC):** Separación estricta entre Vista (Terminal), Controlador, Servicio (Lógica de Negocio) y Repositorio (Persistencia).
- **Patrón de Estados (State Pattern):** Gestión de la interactividad del mapa mediante estados intercambiables para evitar lógica condicional compleja en la vista.

## 2. Stack Tecnológico
- **Lenguaje:** Java 11.
- **Motor de Terminal (Lanterna 3.1.x):** Gestión de buffers, capas de texto y captura de eventos de teclado.
- **Persistencia:** Google Gson para la serialización de objetos a archivos JSON con mapeo de nombres (`@SerializedName`).

## 3. Arquitectura del Sistema

### 3.1 Capa de Vista (Terminal)
- **MapaView:** Componente central que coordina el renderizado y la captura de entrada.
- **GridMapaRenderer:** Motor encargado de dibujar la rejilla 2D, los bordes de los espacios y las etiquetas de nombre.
- **MapaModoEstado (Interface):** Define el comportamiento para los diferentes modos:
    - `ModoNavegarEstado`: Navegación estándar del cursor.
    - `ModoMoverEstado`: Desplazamiento de un espacio seleccionado.
    - `ModoRedimensionarEstado`: Ajuste de ancho y alto de un espacio.
    - `ModoEditarRecorridoEstado`: Selección secuencial de espacios para un tour.
- **Componentes de Diálogo:** `DialogForm`, `PlantaDialog`, `RecorridoDialog` y `LoginDialog`.

### 3.2 Capa de Negocio (Servicios)
- **GestionEspacioService:** Centraliza la validación de solapamientos geométrica y la integridad de la jerarquía.
- **VisitaService:** Gestiona el estado de la visita actual y la navegación entre los puntos de un recorrido.
- **AuthService:** Maneja la autenticación y la creación del objeto `Sesion`.

### 3.3 Capa de Datos (Modelos y Repositorios)
- **Jerarquía de Datos:** `Universidad` > `Region` > `Planta` > `Espacio`.
- **Repositorios:** Implementan `IRepository<T>` utilizando `JsonUtil` para el acceso atómico a los archivos `.json`.

## 4. Lógica de Negocio Crítica

### 4.1 Detección de Solapamientos (Collision Detection)
El sistema valida que ningún espacio ocupe el mismo área que otro en la misma planta antes de confirmar una creación, movimiento o redimensión:
`A.x < B.x + B.ancho && A.x + A.ancho > B.x && A.y < B.y + B.alto && A.y + A.alto > B.y`

### 4.2 Integridad de Recorridos
No se permite la eliminación de un `Espacio` si este forma parte de un `Recorrido` activo. El servicio lanza una `ExcepcionReglaNegocio` en caso de violación.

### 4.3 Gestión de Sesión
Tras una autenticación exitosa, el sistema genera un objeto `Sesion` que contiene:
- `Administrador` autenticado.
- `fechaInicio` (timestamp).
- `activa` (boolean).

## 5. Protocolo de Comandos

| Tecla | Acción |
| :--- | :--- |
| `Flechas` | Movimiento del cursor o del objeto (según modo). |
| `Enter` | Confirmar cambios / Ver detalles. |
| `N` | Crear nuevo espacio. |
| `E` | Editar espacio seleccionado. |
| `D` | Eliminar espacio (con confirmación). |
| `M` | Activar modo mover. |
| `R` | Activar modo redimensionar. |
| `T` | Listar / Gestionar recorridos. |
| `P` | Abrir gestor de plantas. |
| `Esc` | Cancelar modo actual / Salir. |
