
# Análisis de Casos de Uso

<div align=right>

| [![](https://img.shields.io/badge/-Inicio-FFF?style=flat&logo=Emlakjet&logoColor=black)](/README.md) [![](https://img.shields.io/badge/-Especificación_Técnica-282c34?style=flat&logo=markdown&logoColor=white)](/documents/spec.md) [![](https://img.shields.io/badge/-myUniverse_IDSW1-FFF?style=flat&logo=github&logoColor=black)](https://github.com/Camila-Lesly/25-26-idsw1-sdr) [![](https://img.shields.io/badge/-Conversation_Log-FFF?style=flat&logo=LiveChat&logoColor=black)](/conversation-log.md) [![](https://img.shields.io/badge/-Requisitado-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/00-requisitado//auditoria.md) [![](https://img.shields.io/badge/-Análisis-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/01-analisis/Análisis.md) [![](https://img.shields.io/badge/-Diseño-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/02-diseño/Diseño.md) [![](https://img.shields.io/badge/-Desarrollo_&_Ejecución-FFF?style=flat&logo=Proton&logoColor=black)](/RUP/03-desarrollo/Desarrollo.md)
|:-:|

</div>

Esta carpeta contiene el análisis MVC (Model-View-Controller) de cada caso de uso especificado, incluyendo diagramas de colaboración y secuencia.

## Casos de uso analizados

### Gestión del sistema
- [iniciarSesion](/images/01-análisis/casos-uso/00-Administrador/iniciarSesion/iniciarSesion.svg) - Análisis MVC de autenticación de administrador

### Apertura de entidades
- [abrirEspacios](/images/01-análisis/casos-uso/00-Administrador/abrirEspacios/abrirEspacios.svg) - Gestión de vista de listado de espacios
- [abrirRecorridos](/images/01-análisis/casos-uso/00-Administrador/abrirRecorridos/abrirRecorridos.svg) - Gestión de vista de listado de recorridos

### CRUD de Espacios
- [crearEspacio](/images/01-análisis/casos-uso/00-Administrador/crearEspacio/crearEspacio.svg) - Análisis MVC de creación de espacio
- [editarEspacio](/images/01-análisis/casos-uso/00-Administrador/editarEspacio/editarEspacio.svg) - Análisis MVC de edición de espacio
- [eliminarEspacio](/images/01-análisis/casos-uso/00-Administrador/eliminarEspacio/eliminarEspacio.svg) - Análisis MVC de eliminación de espacio

### CRUD de Recorridos
- [crearRecorrido](/images/01-análisis/casos-uso/00-Administrador/crearRecorrido/crearRecorrido.svg) - Análisis MVC de creación de recorrido
- [editarRecorrido](/images/01-análisis/casos-uso/00-Administrador/editarRecorrido/editarRecorrido.svg) - Análisis MVC de edición de recorrido
- [eliminarRecorrido](/images/01-análisis/casos-uso/00-Administrador/eliminarRecorrido/eliminarRecorrido.svg) - Análisis MVC de eliminación de recorrido

### Consultas de Visitante
- [buscarEspacio](/images/01-análisis/casos-uso/01-Visitante/buscarEspacio/buscarEspacio.svg) - Análisis MVC de búsqueda de espacios
- [cambiarDeEspacio](/images/01-análisis/casos-uso/01-Visitante/cambiarDeEspacio/cambiarDeEspacio.svg) - Análisis MVC de navegación entre espacios
- [listarRecorridos](/images/01-análisis/casos-uso/01-Visitante/listarRecorridos/listarRecorridos.svg) - Análisis MVC de listado de recorridos disponibles
- [seleccionarRecorrido](/images/01-análisis/casos-uso/01-Visitante/selecccionarRecorrido/seleccionarRecorrido.svg) - Análisis MVC de selección de un recorrido específico
- [verDetalles](/images/01-análisis/casos-uso/01-Visitante/verDetalles/verDetalles.svg) - Análisis MVC de visualización de detalles de un espacio
- [verEspacio](/images/01-análisis/casos-uso/01-Visitante/verEspacio/verEspacio.svg) - Análisis MVC de visualización de la información de un espacio
- [verEspaciosCercanos](/images/01-análisis/casos-uso/01-Visitante/verEspaciosCercanos/verEspaciosCercanos.svg) - Análisis MVC de visualización de espacios próximos
- [verEspaciosPlanta](/images/01-análisis/casos-uso/01-Visitante/verEspaciosPlanta/verEspaciosPlanta.svg) - Análisis MVC de visualización de espacios organizados por planta


## Resumen de MVC

- [Modelos](/images/01-análisis/MVC/modelos.svg) - Análisis MVC de visualización de espacios organizados por planta

- [Vistas](/images/01-análisis/MVC/vistas.svg) - Análisis MVC de visualización de espacios organizados por planta

- [Controladores](/images/01-análisis/MVC/controladores.svg) - Análisis MVC de visualización de espacios organizados por planta