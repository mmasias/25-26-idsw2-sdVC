# Guía de Desarrollo: myUniverse

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

### Ejecutar Pruebas
Es fundamental que todas las pruebas pasen antes de realizar cambios:
```bash
./mvnw test
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

## 5. Convenciones de Desarrollo

- **Inmutabilidad:** Se prefiere el uso de `Collections.unmodifiableList` al devolver listas desde modelos.
- **Excepciones:** Las violaciones de reglas de negocio deben lanzar una `ExcepcionReglaNegocio` con un ID de regla (ej: "BR-02").
- **Trazabilidad:** Cada cambio en el modelo debe verse reflejado en el archivo `documents/spec.md`.
