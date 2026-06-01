# Diseño de Software: myUniverse

<div align=right>

| [![](https://img.shields.io/badge/-Inicio-FFF?style=flat&logo=Emlakjet&logoColor=black)](/README.md) [![](https://img.shields.io/badge/-Especificación_Técnica-282c34?style=flat&logo=markdown&logoColor=white)](/documents/spec.md) [![](https://img.shields.io/badge/-myUniverse_IDSW1-FFF?style=flat&logo=github&logoColor=black)](https://github.com/Camila-Lesly/25-26-idsw1-sdr) [![](https://img.shields.io/badge/-Conversation_Log-FFF?style=flat&logo=LiveChat&logoColor=black)](/conversation-log.md) [![](https://img.shields.io/badge/-Requisitado-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/00-requisitado//auditoria.md) [![](https://img.shields.io/badge/-Análisis-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/01-analisis/Análisis.md) [![](https://img.shields.io/badge/-Diseño-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/02-diseño/Diseño.md) [![](https://img.shields.io/badge/-Desarrollo_&_Ejecución-FFF?style=flat&logo=Proton&logoColor=black)](/RUP/03-desarrollo/Desarrollo.md)
|:-:|

</div>

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
- [**Iniciar Sesión**](/images/02-diseño/00-Administrador/iniciarSesion/iniciarSesion.svg)
- [**Abrir Espacios**](/images/02-diseño/00-Administrador/abrirEspacios/abrirEspacios.svg)
- [**Abrir Recorridos**](/images/02-diseño/00-Administrador/abrirRecorridos/abrirRecorridos.svg)
- [**Editar Universidad**](/images/02-diseño/00-Administrador/editarUniversidad/editarUniversidad.svg)
- [**Crear Región**](/images/02-diseño/00-Administrador/crearRegion/crearRegion.svg)
- [**Editar Región**](/images/02-diseño/00-Administrador/editarRegion/editarRegion.svg)
- [**Eliminar Región**](/images/02-diseño/00-Administrador/eliminarRegion/eliminarRegion.svg)
- [**Crear Planta**](/images/02-diseño/00-Administrador/crearPlanta/crearPlanta.svg)
- [**Editar Planta**](/images/02-diseño/00-Administrador/editarPlanta/editarPlanta.svg)
- [**Eliminar Planta**](/images/02-diseño/00-Administrador/eliminarPlanta/eliminarPlanta.svg)
- [**Crear Espacio**](/images/02-diseño/00-Administrador/crearEspacio/crearEspacio.svg)
- [**Crear Recorrido**](/images/02-diseño/00-Administrador/crearRecorrido/crearRecorrido.svg)
- [**Editar Espacio**](/images/02-diseño/00-Administrador/editarEspacio/editarEspacio.svg)
- [**Editar Recorrido**](/images/02-diseño/00-Administrador/editarRecorrido/editarRecorrido.svg)
- [**Eliminar Espacio**](/images/02-diseño/00-Administrador/eliminarEspacio/eliminarEspacio.svg)
- [**Eliminar Recorrido**](/images/02-diseño/00-Administrador/eliminarRecorrido/eliminarRecorrido.svg)

### 2.2 Consultas y Navegación (Visitante)
- [**Listar Recorridos**](/images/02-diseño/01-Visitante/listarRecorridos/listarRecorridos.svg)
- [**Seleccionar Recorrido**](/images/02-diseño/01-Visitante/seleccionarRecorrido/seleccionarRecorrido.svg)
- [**Ver Espacio**](/images/02-diseño/01-Visitante/verEspacio/verEspacio.svg)
- [**Ver Detalles**](/images/02-diseño/01-Visitante/verDetalles/verDetalles.svg)
- [**Ver Espacios Cercanos**](/images/02-diseño/01-Visitante/verEspaciosCercanos/verEspaciosCercanos.svg)
- [**Ver Espacios por Planta**](/images/02-diseño/01-Visitante/verEspaciosPlanta/verEspaciosPlanta.svg)
- [**Buscar Espacio**](/images/02-diseño/01-Visitante/buscarEspacio/buscarEspacio.svg)
- [**Cambiar de Espacio**](/images/02-diseño/01-Visitante/cambiarDeEspacio/cambiarDeEspacio.svg)