# Arquitectura de Software: Patrón MVC + Service/Repository (myUniverse)

Este documento detalla la implementación del patrón **Modelo-Vista-Controlador (MVC)**, extendido con capas de **Servicio** y **Repositorio**, para la aplicación CLI myUniverse en Java. Esta arquitectura asegura una separación estricta entre la interfaz, la lógica de negocio y la persistencia.

---

## 1. Definición de Capas

### 1.1 Vista (View)
- **Responsabilidad:** Interfaz de consola. Captura la entrada (`Scanner`) y muestra la salida (`System.out`).
- **Restricción:** Solo se comunica con el **Controlador**.

### 1.2 Controlador (Controller)
- **Responsabilidad:** Orquestador de la UI. Recibe órdenes de la Vista, invoca al Servicio correspondiente y actualiza la Vista con el resultado.
- **Restricción:** No contiene lógica de negocio pesada, solo flujo de navegación y manejo de peticiones.

### 1.3 Servicio (Service)
- **Responsabilidad:** Contiene la **Lógica de Negocio**. Validaciones, cálculos, y coordinación de múltiples operaciones.
- **Componentes:** `GestionEspacioService`, `VisitaService`, `AuthService`.

### 1.4 Repositorio (Repository)
- **Responsabilidad:** Abstracción de la persistencia. Gestiona la lectura/escritura de los datos en archivos JSON.
- **Componentes:** `EspacioRepository`, `RecorridoRepository`.

### 1.5 Modelo/Entidad (Entity)
- **Responsabilidad:** Representación de los datos del dominio (POJOs).

---

## 2. Estructura de Paquetes (Organización por Capas)

```text
src/main/java/com/myuniverse/
├── controllers/              # Orquestadores de UI
│   ├── AuthController.java
│   ├── EspacioController.java
│   └── ...
├── services/                 # Lógica de negocio y validaciones
│   ├── AuthService.java
│   ├── EspacioService.java
│   └── ...
├── repositories/             # Persistencia de datos (JSON)
│   ├── IRepository.java      # Interfaz base
│   ├── EspacioRepository.java
│   └── ...
├── views/                    # Interfaces de consola
│   ├── LoginView.java
│   ├── admin/
│   └── visitor/
├── models/                   # Entidades de dominio (POJOs)
│   ├── Universidad.java
│   ├── Edificio.java
│   ├── Planta.java
│   ├── Espacio.java
│   └── Recorrido.java
└── Main.java                 # Bootstrap
```

---

## 3. Flujo de Control Extendido

1.  **Vista:** El usuario solicita una acción (ej. "Crear Espacio").
2.  **Controlador:** Recibe los datos y los envía al **Servicio**.
3.  **Servicio:** Valida que el nombre no esté duplicado y que la planta exista. Si todo es correcto, llama al **Repositorio**.
4.  **Repositorio:** Serializa el objeto y lo guarda en el archivo JSON.
5.  **Retorno:** El Repositorio confirma al Servicio, el Servicio al Controlador, y el Controlador ordena a la Vista mostrar un mensaje de éxito.

---

## 4. Trazabilidad con Análisis (BCE)

| Componente Java           | Capa Arquitectónica | Estereotipo RUP |
|--------------------------|---------------------|-----------------|
| `com.myuniverse.views`    | Vista               | `boundary`      |
| `com.myuniverse.controllers`| Controlador         | `control`       |
| `com.myuniverse.services` | Servicio (Lógica)   | `control`       |
| `com.myuniverse.repositories`| Repositorio (Datos) | `control`       |
| `com.myuniverse.models`   | Entidad             | `entity`        |
