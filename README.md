# myUniverse: Campus Explorer CLI

<div align="center">
  <p><b>Una experiencia interactiva en terminal para la gestión y exploración de campus universitarios.</b></p>
</div>

<div align=right>

| [![](https://img.shields.io/badge/-Inicio-FFF?style=flat&logo=Emlakjet&logoColor=black)](/README.md) [![](https://img.shields.io/badge/-Especificación_Técnica-282c34?style=flat&logo=markdown&logoColor=white)](/documents/spec.md) [![](https://img.shields.io/badge/-myUniverse_IDSW1-FFF?style=flat&logo=github&logoColor=black)](https://github.com/Camila-Lesly/25-26-idsw1-sdr) [![](https://img.shields.io/badge/-QUE_HACE-FFF?style=flat&logo=Proton&logoColor=black)](/QUE_HACE.md) [![](https://img.shields.io/badge/-Conversation_Log-FFF?style=flat&logo=LiveChat&logoColor=black)](/conversation-log.md) [![](https://img.shields.io/badge/-Requisitado-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/00-requisitado//auditoria.md) [![](https://img.shields.io/badge/-Análisis-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/01-analisis/Análisis.md) [![](https://img.shields.io/badge/-Diseño-FFF?style=flat&logo=openstreetmap&logoColor=black)](/RUP/02-diseño/Diseño.md) [![](https://img.shields.io/badge/-Desarrollo_&_Ejecución-FFF?style=flat&logo=Proton&logoColor=black)](/RUP/03-desarrollo/Desarrollo.md)
|:-:|


<p align="center">
  <img src="images/capturas/Captura%20desde%202026-05-27%2012-18-11.png" alt="Portada de myUniverse" width="800">
    <br>
  <i>Visualización de la rejilla y gestión de espacios del campus.</i>
</p>


---

## ¿Qué es myUniverse?

**myUniverse** transforma la gestión de infraestructuras universitarias en una experiencia visual y técnica única directamente desde tu terminal. Utilizando una interfaz basada en rejillas ASCII, permite a administradores y visitantes interactuar con el campus de la **Universidad Europea del Atlántico** de forma dinámica.

### Datos Interesantes del Producto Final

*   **Interacción en Tiempo Real:** Navega, mueve y redimensiona espacios del campus usando solo el teclado, con retroalimentación visual instantánea.
*   **Motor Geométrico Inteligente:** Incluye un sistema de detección de colisiones que evita que los espacios se solapen, garantizando un mapa coherente.
*   **Tours Dinámicos:** Crea y sigue recorridos personalizados a través de los puntos de interés más importantes del campus.


---

## Stack Tecnológico

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Lanterna](https://img.shields.io/badge/Lanterna_TUI-3.1.x-blue?style=for-the-badge)
![Gson](https://img.shields.io/badge/Google_Gson-2.8.x-green?style=for-the-badge)
![Maven](https://img.shields.io/badge/Apache_Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

</div>

*   **Motor TUI:** [Lanterna](https://github.com/mabe02/lanterna) para la gestión avanzada de buffers y eventos de teclado en terminal.
*   **Arquitectura:** Basada en **N-Capas (MVC)** y el **Patrón de Estados (State Pattern)** para una navegación fluida entre modos (Editar, Mover, Navegar).
*   **Persistencia:** Gestión atómica de datos mediante archivos **JSON** estructurados.

---

## Metodología de Desarrollo

Este proyecto sigue rigurosamente el proceso **RUP (Rational Unified Process)**, dividiendo el ciclo de vida en entregables claros que puedes explorar en este repositorio:

1.  **[Requisitado](/RUP/00-requisitado/auditoria.md):** Definición de necesidades y diagramas de contexto.
2.  **[Análisis](/RUP/01-analisis/Análisis.md):** Modelado MVC de casos de uso y diagramas de colaboración.
3.  **[Diseño](/RUP/02-diseño/Diseño.md):** Arquitectura técnica detallada y diagramas de secuencia.
4.  **[Desarrollo](/RUP/03-desarrollo/Desarrollo.md):** Guía de implementación, ejecución y pruebas.

---

## Controles Rápidos

| Tecla | Acción |
| :--- | :--- |
| `Flechas` | Moverse por el mapa / Navegar cursor |
| `Enter` | Ver detalles o confirmar cambios |
| `M / R` | Activar modos de Movimiento o Redimensión |
| `T` | Gestionar recorridos (Tours) |
| `Esc` | Cancelar acción o Salir |

---
