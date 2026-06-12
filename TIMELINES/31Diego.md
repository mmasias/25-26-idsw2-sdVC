# Timeline - 31Diego

> Repo: [31Diego/25-26-idsw2-sdVC](https://github.com/31Diego/25-26-idsw2-sdVC)
> Commits: 28 | Días activos: 17 | Sesiones log: 0

## Patrón observado

<div align="center">

| Métrica | Valor |
|---|---|
| Commits propios | 28 (15 feat / 5 fix / 8 other) |
| Ratio fix/feat | 0.33 |
| Días activos | 17 |
| Sesiones documentadas | 0 |

</div>

```mermaid
gantt
    title Progresion de artefactos
    dateFormat YYYY-MM-DD
    axisFormat %d/%m
    section Artefactos
        Inicio       :milestone, 2026-05-19, 0d
        UML          :done, 2026-05-22, 2026-06-12
        Analisis     :done, 2026-05-22, 2026-06-12
        Diseno       :done, 2026-05-29, 2026-06-12
        Codigo       :done, 2026-05-30, 2026-06-12
```

<!-- trazabilidad: manual -->
## Trazabilidad por caso de uso

<div align="center">

> D7: "fix: split casos de uso con múltiples entradas por comportamiento" — creó 3 CUs nuevos por refinamiento

| Caso de uso | D4 | D5 | D7 | D11 | D12 | D14 | D15 | D16 | D18 |
|---|:---:|:---:|:---:|:---:|:---:|:---: | :---: | :---: | :---: |
| `abrirConvocatoria` | A |   |   | D | d | | | |     |
| `abrirConvocatorias` | A |   |   | D | d | | | |     |
| `abrirEntregable` |   | A |   |   |   | | Dd | |     |
| `abrirEntregables` |   | A |   |   |   | | Dd | |     |
| `abrirInvestigador` |   | A |   |   |   | | | Dd |     |
| `abrirInvestigadores` |   | A |   |   |   | | | Dd |     |
| `abrirMiPublicacion` |   | A |   |   |   | | | |     |
| `abrirMisPublicaciones` |   | A |   |   |   | | | |     |
| `abrirOpcionesCargaTrabajo` |   | A |   |   | d | | | |     |
| `abrirOpcionesPerfil` |   | A |   |   | Dd | | | |     |
| `abrirPanelPrincipal` |   | A |   |   | Dd | | | |     |
| `abrirProyecto` |   | A |   |   |   | Dd | | |     |
| `abrirProyectos` |   | A |   |   |   | Dd | | |     |
| `abrirPublicacion` |   | A |   |   |   | | | |     |
| `abrirPublicaciones` |   | A |   |   |   | | | |     |
| `abrirRecompensa` |   | A |   |   |   | | | |     |
| `abrirRecompensas` |   | A |   |   |   | | | |     |
| `abrirSolicitudEliminacionPerfil` |   | A |   |   |   | | | | Dd |
| `abrirSolicitudesEliminacionPerfil` |   | A |   |   |   | | | | Dd |
| `agregarInvestigador` |   | A |   |   |   | | | Dd |     |
| `cerrarSesion` |   | A |   |   | Dd | | | |     |
| `crearEntregable` |   | A |   |   |   | | Dd | |     |
| `crearInvestigador` |   | A |   |   |   | | | Dd |     |
| `crearProyecto` |   | A |   |   |   | Dd | | |     |
| `crearPublicacion` |   | A |   |   |   | | | |     |
| `crearRecompensa` |   | A |   |   |   | | | |     |
| `editarCargaTrabajo` |   | A |   |   | d | | | |     |
| `editarEntregable` |   | A |   |   |   | | Dd | |     |
| `editarMiPublicacion` |   | A |   |   |   | | | |     |
| `editarPerfil` |   | A |   |   | Dd | | | |     |
| `editarProyecto` |   | A |   |   |   | Dd | | |     |
| `editarPublicacion` |   | A |   |   |   | | | |     |
| `editarRecompensa` |   | A |   |   |   | | | |     |
| `eliminarEntregable` |   | A |   |   |   | | Dd | |     |
| `eliminarInvestigador` |   | A |   |   |   | | | Dd |     |
| `eliminarMiPublicacion` |   | A |   |   |   | | | |     |
| `eliminarProyecto` |   | A |   |   |   | Dd | | |     |
| `eliminarPublicacion` |   | A |   |   |   | | | |     |
| `eliminarRecompensa` |   | A |   |   |   | | | |     |
| `importarConvocatoria` |   | A |   |   |   | | | |     |
| `iniciarSesion` |   | A |   |   | Dd | | | |     |
| `responderPublicacion` |   | A |   |   |   | | | |     |
| `solicitarEliminacionPerfil` |   | A |   |   |   | | | | Dd |
| `abrirInvestigadoresDeProyecto` *(split D7)* |   |   | A |   |   | | | | Dd |
| `abrirOpcionesPerfilInvestigador` *(split D7)* |   |   | A |   |   | | | |     |
| `abrirProyectosDeInvestigador` *(split D7)* |   |   | A |   |   | | | |     |

</div>

---

## Día 3 · 2026-05-21

### Commits (1: 1 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 16:50 | [feat: QUE_HACE.md](https://github.com/31Diego/25-26-idsw2-sdVC/commit/f034ff8f486f3bb665493c6e2d1e8b8ffc9bfa6c) |

> ⚠️ Commits sin entrada en log

---

## Día 4 · 2026-05-22

### Commits (2: 2 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 22:44 | [feat: análisis de abrirConvocatorias y corrección de estructura de carpetas](https://github.com/31Diego/25-26-idsw2-sdVC/commit/5dcc693f105aa41de1f884d7934c3780c7b38dfa) |
| 22:10 | [feat: CLAUDE.md, análisis abrirConvocatoria y registro de sesión](https://github.com/31Diego/25-26-idsw2-sdVC/commit/eeaebde9532bcc546ee2a47610e6559a2fcd0ee3) |

**Artefactos nuevos:** 📐 🔍 

> ⚠️ Commits sin entrada en log

---

## Día 5 · 2026-05-23

### Commits (1: 1 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 22:53 | [feat: análisis completo del coordinador — 43 casos de uso](https://github.com/31Diego/25-26-idsw2-sdVC/commit/4fee52742993c2e7c23ec774cc234803d1b3ebab) |

> ⚠️ Commits sin entrada en log

---

## Día 7 · 2026-05-25

### Commits (3: 1 feat / 2 fix)

| Hora | Mensaje |
|---|---|
| 21:49 | [fix: entradas secundarias faltantes en diagramas de coordinador](https://github.com/31Diego/25-26-idsw2-sdVC/commit/d80b4c93400859bf6ba37aa0f3f7d38c5f59ddbc) |
| 21:29 | [fix: split casos de uso con múltiples entradas por comportamiento](https://github.com/31Diego/25-26-idsw2-sdVC/commit/388167c7a3e6affad42cd5f3243ef5f91e66b9a0) |
| 20:22 | [feat: plantilla de navegación estados vs colaboraciones](https://github.com/31Diego/25-26-idsw2-sdVC/commit/033f41402ef0232dafa4226cc439e81b30e45b32) |

> ⚠️ Commits sin entrada en log

---

## Día 10 · 2026-05-28

### Commits (1: 0 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 22:26 | [docs: log de sesión — exploración de stack tecnológico](https://github.com/31Diego/25-26-idsw2-sdVC/commit/a968208408a156bc5bbe86fbf5f85f8c60c8bb68) |

> ⚠️ Commits sin entrada en log

---

## Día 11 · 2026-05-29

### Commits (1: 0 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 18:56 | [docs: log de sesión — decisiones de stack y diseño abrirConvocatoria/abrirConvocatorias](https://github.com/31Diego/25-26-idsw2-sdVC/commit/44fb88600a005923a0ebb4921e0f810a51b60c75) |

**Artefactos nuevos:** 🧩 

> ⚠️ Commits sin entrada en log

---

## Día 12 · 2026-05-30

### Commits (1: 1 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 19:43 | [feat: diseño y desarrollo P0 coordinador — iniciarSesion, cerrarSesion, panelPrincipal, perfil, cargaTrabajo](https://github.com/31Diego/25-26-idsw2-sdVC/commit/778956cf2b13d0546ecd118a68d0bcb5d75e60d7) |

**Artefactos nuevos:** 🔌 

> ⚠️ Commits sin entrada en log

---

## Día 13 · 2026-05-31

### Commits (1: 0 feat / 1 fix)

| Hora | Mensaje |
|---|---|
| 21:18 | [fix: corregir análisis iniciarSesion y cerrarSesion; borrar artefactos de diseño, desarrollo y código](https://github.com/31Diego/25-26-idsw2-sdVC/commit/2044578a515b7c5f27dac3f346fc86ac6265a7c0) |

> ⚠️ Commits sin entrada en log

---

## Día 14 · 2026-06-01

### Commits (1: 1 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 19:41 | [feat: corregir análisis coordinador y añadir diseño e implementación de proyectos](https://github.com/31Diego/25-26-idsw2-sdVC/commit/cf475b0efe5fc5b19c64bad88f30dab7b34ace92) |

> ⚠️ Commits sin entrada en log

---

## Día 15 · 2026-06-02

### Commits (3: 2 feat / 1 fix)

| Hora | Mensaje |
|---|---|
| 21:45 | [feat: diseño e implementación de entregables con subida y descarga de archivos](https://github.com/31Diego/25-26-idsw2-sdVC/commit/65e8d73b21f1023900b634b4a583b5e8913cd0e2) |
| 18:46 | [feat: archivo de revision de casos de analisis de ayer](https://github.com/31Diego/25-26-idsw2-sdVC/commit/1625a209848b0c99edd09aec0edbad2580ef481f) |
| 18:41 | [fix: corregir diagramas de secuencia de diseño — añadir navegador antes de la petición HTTP](https://github.com/31Diego/25-26-idsw2-sdVC/commit/8248ee788929582477e4ef09d939b4039b840d8e) |

> ⚠️ Commits sin entrada en log

---

## Día 16 · 2026-06-03

### Commits (1: 1 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 21:47 | [feat: diseño e implementación de investigadores y proyectos del investigador || cambio de rutas de archivos md](https://github.com/31Diego/25-26-idsw2-sdVC/commit/6884dba8dbe79df9285ea2712a04c9bfcef6cd33) |

> ⚠️ Commits sin entrada en log

---

## Día 17 · 2026-06-04

### Commits (1: 0 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 21:15 | [docs: reflexiones sobre diagramas de secuencia de diseño](https://github.com/31Diego/25-26-idsw2-sdVC/commit/09f3b2c81361bcdd77213d0bb55e87e13fdf491b) |

> ⚠️ Commits sin entrada en log

---

## Día 18 · 2026-06-05

### Commits (1: 1 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 19:09 | [feat: análisis investigador + diseño e implementación de investigadoresDeProyecto y solicitudes de eliminación de perfil](https://github.com/31Diego/25-26-idsw2-sdVC/commit/346503cfb1241b46d1c9ea03b959353f6574a67c) |

> ⚠️ Commits sin entrada en log

---

## Día 20 · 2026-06-07

### Commits (3: 2 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 20:24 | [refactor: aplicar SRP y OCP — ArchivoService, enum Rol, guards de acceso en ProyectoService](https://github.com/31Diego/25-26-idsw2-sdVC/commit/4a7ee26f3e82b02a591d8d0d8186e27b55ca3a7d) |
| 19:12 | [feat: consolidar controllers por entidad + fix flujo eliminación y bug FK entregables](https://github.com/31Diego/25-26-idsw2-sdVC/commit/546047c25061a5c4b31fb9e4e7c3cc6bc3b1ffe1) |
| 14:30 | [feat: consolidar controllers por entidad + fix flujo eliminación y bug FK entregables](https://github.com/31Diego/25-26-idsw2-sdVC/commit/f4aada305e9aae1dc0cb77c40de88fdbf37623fc) |

> ⚠️ Commits sin entrada en log

---

## Día 21 · 2026-06-08

### Commits (3: 0 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 21:13 | [refactor: revisión SOLID y sincronización de diagramas de diseño](https://github.com/31Diego/25-26-idsw2-sdVC/commit/d1b190a3869137f6c0b666fda94a5751b2fc2c42) |
| 20:23 | [refactor: mover lógica de negocio de controllers a services (SRP)](https://github.com/31Diego/25-26-idsw2-sdVC/commit/8bd9b6459c15e1d136c90e13e440707dbf70f0d5) |
| 18:44 | [refactor: SRP con InvestigadorUserDetails y OCP con Strategy en ProyectoService](https://github.com/31Diego/25-26-idsw2-sdVC/commit/cecaa9b5cb97d7bee7ab0177d284aaba109766ce) |

> ⚠️ Commits sin entrada en log

---

## Día 23 · 2026-06-10

### Commits (2: 1 feat / 1 fix)

| Hora | Mensaje |
|---|---|
| 19:21 | [fix: corregir OCP y code smells en CargaTrabajo + diseño e implementación de publicaciones](https://github.com/31Diego/25-26-idsw2-sdVC/commit/1034219982747f9c3ce3f011d61bd7913d418ded) |
| 17:14 | [feat: diseño e implementación de carga de trabajo + límite en agregarInvestigador](https://github.com/31Diego/25-26-idsw2-sdVC/commit/76b113b5e54f7f5a3bf658d5d0d67f3888a2e775) |

> ⚠️ Commits sin entrada en log

---

## Día 24 · 2026-06-11

### Commits (2: 1 feat / 0 fix)

| Hora | Mensaje |
|---|---|
| 18:59 | [refactor: aplicar SOLID y sincronizar diagramas de diseño](https://github.com/31Diego/25-26-idsw2-sdVC/commit/4fbe6e846cc04b6fc9bceb043035ea19739212fd) |
| 17:56 | [feat: diseño e implementación de publicaciones y mis publicaciones + convocatorias + recompensas](https://github.com/31Diego/25-26-idsw2-sdVC/commit/dfcc4875c02fe778dce9fc326f67fbffc84ec030) |

> ⚠️ Commits sin entrada en log

---

