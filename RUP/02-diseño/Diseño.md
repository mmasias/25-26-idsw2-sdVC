# Diseño de Software: myUniverse

Este documento describe la arquitectura de diseño y la interacción de componentes de myUniverse, siguiendo la especificación técnica definida en `spec.md`.

## 1. Arquitectura de Referencia
Se utiliza una arquitectura de **N-Capas** basada en el patrón **MVC**, con una clara separación entre lógica de negocio y persistencia.

### Capas Técnicas:
1.  **Vista (View):** Gestión de la CLI (Entrada/Salida).
2.  **Controlador (Controller):** Orquestador de peticiones.
3.  **Servicio (Service):** Lógica de negocio y validaciones.
4.  **Repositorio (Repository):** Persistencia de datos (JSON).
5.  **Entidad (Entity):** Modelos de dominio.

---

## 2. Diagramas de Secuencia por Caso de Uso

### 2.1 Gestión (Administrador)
- **Iniciar Sesión** ()
- **Configurar Universidad** ()
- **Abrir Espacios** ()
- **Abrir Recorridos** ()
- **Crear Espacio** ()
- **Crear Recorrido** ()
- **Editar Espacio** ()
- **Editar Recorrido** ()
- **Eliminar Espacio** ()
- **Eliminar Recorrido** ()

### 2.2 Consultas y Navegación (Visitante)
- **Iniciar Visita** ()
- **Listar Recorridos** ()
- **Seleccionar Recorrido** ()
- **Ver Espacio** ()
- **Ver Detalles** ()
- **Ver Espacios Cercanos** ()
- **Ver Espacios por Planta** ()
- **Buscar Espacio** ()
- **Cambiar de Espacio** ()

---

## 3. Decisiones de Diseño
- **Inyección de Dependencias:** Los controladores reciben sus servicios y los servicios sus repositorios a través del constructor.
- **Manejo de Errores:** Las excepciones se capturan en el controlador para delegar el mensaje de error adecuado a la vista.
- **Persistencia:** Se utiliza la interfaz `IRepository` para permitir el intercambio de sistemas de persistencia sin afectar la lógica de negocio.
