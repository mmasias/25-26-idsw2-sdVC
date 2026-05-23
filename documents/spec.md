# Arquitectura de Software: Patrón MVC (myUniverse)

Este documento detalla la implementación del patrón **Modelo-Vista-Controlador (MVC)** para la aplicación CLI myUniverse en Java. La arquitectura se centra en la separación estricta de responsabilidades para facilitar el mantenimiento y la escalabilidad.

---

## Objetivos

- Construir una aplicación de terminal en Java para explorar un edificio por plantas.
- Mostrar planos ASCII o textuales de forma clara y navegable.
- Consultar espacios, puntos de interés y detalles contextuales.
- Incluir un dashboard de administración por consola.
- Permitir ejecutar el sistema en modo 2D o modo 3D desde terminal.
- Mantener una arquitectura limpia, modular y testeable.
- Garantizar cobertura funcional de todos los casos de uso del proyecto.

## 1. Definición de Capas

### 1.1 Modelo (Model)
- **Responsabilidad:** Gestiona el estado de la aplicación, las entidades de negocio y la persistencia de datos.
- **Componentes:** Entidades (POJOs) y Repositorios (Acceso a JSON).
- **Entidades Clave:** `Universidad`, `Edificio`, `Planta`, `Espacio`, `Recorrido`, `Administrador`.

### 1.2 Vista (View)
- **Responsabilidad:** Interfaz de usuario en consola. Captura la entrada del usuario (`Scanner`) y muestra la salida (`System.out`).
- **Restricción:** No contiene lógica de negocio ni accede directamente a los repositorios. Se comunica únicamente con el Controlador.
- **Componentes:** `LoginView`, `EspaciosView`, `RecorridosView`, `BienvenidaView`, `MapaView`.

### 1.3 Controlador (Controller)
- **Responsabilidad:** Actúa como mediador. Procesa las solicitudes de la Vista, manipula el Modelo y devuelve la respuesta a la Vista.
- **Componentes:** `AuthController`, `GestionController` (CRUDs), `VisitaController` (Navegación).

---

## 2. Estructura de Paquetes (Organización por Capas)

Para respetar la arquitectura MVC, el proyecto se organiza por capas técnicas:

```text
src/main/java/com/myuniverse/
├── controllers/              # Lógica de orquestación
│   ├── AuthController.java
│   ├── UniversidadController.java
│   ├── EdificioController.java
│   ├── PlantaController.java
│   ├── EspacioController.java
│   ├── RecorridoController.java
│   └── VisitaController.java
├── views/                    # Interfaces de consola
│   ├── LoginView.java
│   ├── admin/                # Vistas de gestión
│   │   ├── UniversidadView.java
│   │   ├── EdificioView.java
│   │   ├── PlantaView.java
│   │   ├── EspacioView.java
│   │   └── RecorridoAdminView.java
│   └── visitor/              # Vistas de navegación
│       ├── BienvenidaView.java
│       ├── ListarRecorridosView.java
│       └── EspacioView.java
├── models/                   # Entidades y Repositorios
│   ├── entities/             # Clases de datos (POJOs)
│   │   ├── Universidad.java
│   │   ├── Edificio.java
│   │   ├── Planta.java
│   │   ├── Espacio.java
│   │   └── Recorrido.java
│   └── repositories/         # Persistencia JSON
│       ├── EspacioRepository.java
│       └── RecorridoRepository.java
└── Main.java                 # Bootstrap de la aplicación
```

---

## 3. Flujo de Control MVC

1.  **Entrada:** El usuario interactúa con una `View` (ej. pulsa 'N' para nuevo espacio).
2.  **Solicitud:** La `View` llama a un método del `Controller` (ej. `espacioController.crear()`).
3.  **Procesamiento:** El `Controller` valida la solicitud y pide al `Repository` (Model) que guarde los datos.
4.  **Actualización:** El `Model` notifica el éxito o devuelve los nuevos datos al `Controller`.
5.  **Respuesta:** El `Controller` ordena a la `View` refrescar la pantalla con la nueva información.

---

## 4. Trazabilidad con Análisis (BCE)

| Componente MVC (Java) | Estereotipo RUP (Análisis) |
|-----------------------|----------------------------|
| `com.myuniverse.views` | `boundary` |
| `com.myuniverse.controllers` | `control` |
| `com.myuniverse.models.entities` | `entity` |
