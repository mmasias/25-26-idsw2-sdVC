# Guía de Desarrollo: myUniverse

<div align=right>

| [![](https://img.shields.io/badge/-Inicio-FFF?style=flat&logo=Emlakjet&logoColor=black)](/README.md) [![](https://img.shields.io/badge/-Especificación_Técnica-282c34?style=flat&logo=markdown&logoColor=white)](/documents/spec.md) [![](https://img.shields.io/badge/-myUniverse_IDSW1-FFF?style=flat&logo=github&logoColor=black)](https://github.com/Camila-Lesly/25-26-idsw1-sdr) [![](https://img.shields.io/badge/-Conversation_Log-FFF?style=flat&logo=LiveChat&logoColor=black)](/conversation-log.md) [![](https://img.shields.io/badge/-Requisitado-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/00-requisitado//auditoria.md) [![](https://img.shields.io/badge/-Análisis-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/01-analisis/Análisis.md) [![](https://img.shields.io/badge/-Diseño-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/02-diseño/Diseño.md) [![](https://img.shields.io/badge/-Desarrollo_&_Ejecución-FFF?style=flat&logo=Proton&logoColor=black)](/RUP/03-desarrollo/Desarrollo.md)
|:-:|

</div>

Este documento proporciona la información técnica necesaria para comprender, instalar, ejecutar y extender el sistema **myUniverse**.

## 1. Descripción General del Proyecto

**myUniverse** es una aplicación de interfaz de línea de comandos (CLI) que permite la gestión y exploración de un campus universitario. El sistema utiliza una representación visual basada en una rejilla ASCII para mostrar plantas, espacios y realizar recorridos interactivos.

### Características principales:
- **Mapa 2D Dinámico:** Visualización en tiempo real de espacios sobre una rejilla.
- **Gestión Administrativa:** CRUD completo de Regiones, Plantas, Espacios y Recorridos.
- **Navegación de Visitante:** Sistema de tours guiados y búsqueda de puntos de interés.
- **Validación de Negocio:** Detección de colisiones geométricas y reglas de integridad referencial.

---

## 2. Requisitos Previos

Para trabajar con este proyecto, necesitas tener instalado:

1.  **Java Development Kit (JDK) 11 o superior:**
    - Se recomienda OpenJDK 11.
    - Verifica la instalación con: `java -version`.
2.  **Apache Maven:**
    - El proyecto utiliza Maven para la gestión de dependencias y construcción.
    - El repositorio incluye un "wrapper" (`./mvnw`), por lo que **no es estrictamente necesario instalar Maven globalmente**.

---

## 3. Instalación y Ejecución

### Clonar y Construir
Para descargar las dependencias y compilar el proyecto:
```bash
./mvnw clean compile
```

### Ejecutar la Aplicación
Existen dos modos de ejecución principales:

1.  **Modo Visitante (Por defecto):**
    ```bash
    ./mvnw exec:java -Dexec.mainClass="com.myuniverse.views.terminal.TerminalApp"
    ```
2.  **Modo Administrador:**
    ```bash
    ./mvnw exec:java -Dexec.mainClass="com.myuniverse.views.terminal.TerminalApp" -Dexec.args="--admin"
    ```

---

## 4. Estructura del Código (Arquitectura MVC)

El proyecto sigue un patrón **Model-View-Controller (MVC)** estricto organizado en paquetes:

### 4.1. Modelos (`com.myuniverse.models`)
Contiene las entidades del dominio con lógica de estado mínima.
- **Universidad:** Entidad raíz que contiene una lista de `Region`.
- **Region:** Agrupación geográfica (ej: Campus Principal) que contiene `Planta`.
- **Planta:** Nivel que contiene una colección de `Espacio`.
- **Espacio:** Punto de interés con coordenadas (X, Y) y dimensiones (ancho, alto).
- **Recorrido:** Secuencia ordenada de IDs de espacios para un tour.

### 4.2. Vistas (`com.myuniverse.views.terminal`)
Implementadas con la librería **Lanterna**.
- **TerminalApp:** Punto de entrada que inicializa la terminal y los controladores.
- **MapaView:** Vista principal que gestiona el renderizado de la rejilla.
- **Diálogos:** (`LoginDialog`, `PlantaDialog`, `RecorridoDialog`) Ventanas emergentes interactivas.
- **Estados:** (`ModoNavegarEstado`, `ModoMoverEstado`) Patrón State para cambiar el comportamiento del teclado.

### 4.3. Controladores (`com.myuniverse.controllers`)
Actúan como intermediarios entre la Vista y los Servicios.
- **EspacioController:** Coordina acciones sobre la jerarquía física.
- **VisitaController:** Gestiona la experiencia del usuario visitante.
- **AuthController:** Maneja el flujo de inicio de sesión.

### 4.4. Servicios (`com.myuniverse.services`)
Donde reside la **Lógica de Negocio Crítica**.
- **GestionEspacioService:** Calcula colisiones geométricas y valida unicidad de nombres.
- **VisitaService:** Implementa la lógica de navegación y cálculo de distancias.

### 4.5. Repositorios y Persistencia (`com.myuniverse.repositories`)
- **JsonUtil:** Wrapper sobre GSON para lectura/escritura atómica de archivos `.json`.
- **Data:** Los datos persisten en `src/main/resources/data/universidad.json`.

---

## 6. Justificación de Diseño y Tecnología

En este apartado se detallan las decisiones arquitectónicas y técnicas que fundamentan la implementación actual de **myUniverse**, alineadas con los principios de la asignatura **IDSW2**.

### 6.1 Elección de Tecnologías
- **Java 11:** Seleccionado por su robustez, tipado fuerte y ecosistema de bibliotecas estándar, ideal para implementar una lógica de negocio compleja con garantías de mantenibilidad.
- **Lanterna 3.1.x:** Se eligió para la interfaz de usuario (TUI) porque permite gestionar la terminal como una rejilla de coordenadas real, facilitando la creación de una interfaz visual sin la sobrecarga de una GUI pesada, manteniendo la aplicación ligera y portable.
- **Google Gson:** Permite una persistencia en archivos JSON de forma atómica y legible, lo que facilita la inspección manual de los datos y el intercambio de información.

### 6.2 Dinamismo del Mapa (Bucle de Renderizado)
El mapa de myUniverse **no es estático**, es una **interfaz dirigida por el estado**. 
- A diferencia de una secuencia lineal de comandos, el sistema utiliza un bucle de eventos (`Event Loop`) en la vista.
- Cada interacción (tecla pulsada) modifica el estado en el `MapContext`.
- Esta modificación dispara un nuevo ciclo de renderizado completo, lo que permite que el mapa se sienta "vivo" y responda instantáneamente al movimiento del cursor o a los cambios de dimensiones.

### 6.3 Aplicación de Principios IDSW2
El proyecto refleja los patrones de diseño y arquitecturas vistos en la asignatura:
- **Patrón State:** La lógica del teclado cambia dinámicamente (`ModoNavegar`, `ModoMover`, `ModoRedimensionar`). Cada modo es una clase independiente, evitando el uso de condicionales masivos (`if/else`) y facilitando la extensión de nuevas funciones.
- **Arquitectura N-Capas:** Separación estricta entre la lógica de visualización (Lanterna), la orquestación (Controllers) y el dominio (Services/Models).
- **Inyección de Dependencias:** Los servicios y controladores se pasan como dependencias, facilitando la testabilidad del sistema.

### 6.4 Memoria vs. Persistencia (Edición en Tiempo Real)
Una decisión de diseño clave es que, mientras el administrador está en "modo edición" (moviendo un espacio, redimensionándolo o llenando un formulario), **los cambios solo existen en la memoria de la sesión (la Vista)**.
- El sistema utiliza objetos "preview" temporales para mostrar el resultado visual antes de confirmarlo.
- La persistencia en disco (JSON) solo ocurre cuando el Administrador pulsa **Enter** para confirmar. Esto protege la integridad de la "base de datos" contra cambios accidentales o estados inconsistentes durante la manipulación manual.

### 6.5 Decisiones sobre la Estructura (Alcance del Diseño)
- **Orden de las Plantas:** Se decidió no implementar una numeración forzada o jerárquica para las plantas. La navegación mediante la tecla `Tab` es circular y equidistante, tratando cada nivel como un punto de interés independiente y fomentando la exploración orgánica del edificio.
- **Contexto Institucional Fijo:** Solo se permite editar el nombre de la Universidad, no crear nuevas instituciones. Esto delimita el alcance del proyecto como una **solución ad-hoc** para la Universidad Europea del Atlántico. Permitir un CRUD de universidades completas se consideró fuera de los requisitos de marca y propósito del sistema.

