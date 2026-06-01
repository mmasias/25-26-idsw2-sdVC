package com.myuniverse.services;

import com.myuniverse.exceptions.ExcepcionReglaNegocio;
import com.myuniverse.models.Planta;
import com.myuniverse.models.Region;
import com.myuniverse.models.Recorrido;
import com.myuniverse.models.Espacio;
import com.myuniverse.models.TipoEspacio;
import com.myuniverse.models.Universidad;
import com.myuniverse.repositories.IEspacioRepository;
import com.myuniverse.repositories.IRecorridoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GestionEspacioService implements IGestionEspacioService {
    private final IEspacioRepository repositorioEspacio;
    private final IRecorridoRepository repositorioRecorrido;

    public GestionEspacioService(IEspacioRepository repositorioEspacio, IRecorridoRepository repositorioRecorrido) {
        this.repositorioEspacio = repositorioEspacio;
        this.repositorioRecorrido = repositorioRecorrido;
    }

    @Override
    public Universidad obtenerUniversidad() {
        return repositorioEspacio.cargarUniversidad();
    }

    @Override
    public void guardarUniversidad(Universidad universidad) {
        repositorioEspacio.guardarUniversidad(universidad);
    }

    @Override
    public List<Espacio> obtenerTodosLosEspacios() {
        return repositorioEspacio.obtenerTodosLosEspacios();
    }

    @Override
    public Espacio obtenerEspacioPorId(String id) {
        return repositorioEspacio.buscarEspacioPorId(id);
    }

    @Override
    public List<Espacio> filtrarEspacios(String criterio) {
        return repositorioEspacio.obtenerTodosLosEspacios().stream()
                .filter(s -> s.getNombre().toLowerCase().contains(criterio.toLowerCase())
                        || s.getTipo().getPersistedName().toLowerCase().contains(criterio.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Planta> obtenerTodasLasPlantas() {
        return repositorioEspacio.obtenerTodasLasPlantas();
    }

    @Override
    public boolean esNombreEspacioUnicoEnPlanta(String nombre, String idPlanta) {
        return !repositorioEspacio.existeNombreEspacioEnPlanta(nombre, idPlanta);
    }

    private Planta findPlantaForEspacio(String idEspacio) {
        return repositorioEspacio.obtenerTodasLasPlantas().stream()
                .filter(f -> f.contieneEspacio(idEspacio))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Espacio crearEspacio(String nombre, TipoEspacio tipo, String descripcion,
                            int coordenadaX, int coordenadaY, int ancho, int alto, String idPlanta) {
        if (nombre == null || nombre.isBlank()) {
            throw new ExcepcionReglaNegocio("BR-02", "El nombre del espacio no puede estar vacío.");
        }
        if (!esNombreEspacioUnicoEnPlanta(nombre, idPlanta)) {
            throw new ExcepcionReglaNegocio("BR-02",
                    "Un espacio con el nombre '" + nombre + "' ya existe en esta planta.");
        }
        List<Espacio> floorEspacios = repositorioEspacio.obtenerEspaciosPorIdPlanta(idPlanta);
        if (tieneSolapamiento(floorEspacios, coordenadaX, coordenadaY, ancho, alto, null)) {
            throw new ExcepcionReglaNegocio("BR-05", "No se puede crear el espacio: se solapa con uno existente.");
        }
        String id = "espacio-" + UUID.randomUUID().toString().substring(0, 8);
        Espacio espacio = new Espacio(id, nombre, tipo, descripcion, coordenadaX, coordenadaY, ancho, alto);
        return repositorioEspacio.guardarEspacio(espacio, idPlanta);
    }

    @Override
    public boolean actualizarEspacioCompleto(String id, String nombre, TipoEspacio tipo, String descripcion,
                                  int coordenadaX, int coordenadaY, int ancho, int alto) {
        Espacio espacio = repositorioEspacio.buscarEspacioPorId(id);
        if (espacio == null) {
            return false;
        }
        Planta planta = findPlantaForEspacio(id);
        if (planta != null && tieneSolapamiento(planta.getEspacios(), coordenadaX, coordenadaY, ancho, alto, id)) {
            throw new ExcepcionReglaNegocio("BR-05", "No se puede actualizar el espacio: se solapa con uno existente.");
        }
        espacio.setNombre(nombre);
        espacio.setTipo(tipo);
        espacio.setDescripcion(descripcion);
        espacio.setCoordenadaX(coordenadaX);
        espacio.setCoordenadaY(coordenadaY);
        espacio.setAncho(ancho);
        espacio.setAlto(alto);
        return repositorioEspacio.actualizarEspacio(espacio);
    }

    @Override
    public boolean moverEspacio(String id, int newX, int newY) {
        Espacio espacio = repositorioEspacio.buscarEspacioPorId(id);
        if (espacio == null) {
            return false;
        }
        Planta planta = findPlantaForEspacio(id);
        if (planta != null && tieneSolapamiento(planta.getEspacios(), newX, newY, espacio.getAncho(), espacio.getAlto(), id)) {
            throw new ExcepcionReglaNegocio("BR-05", "No se puede mover el espacio: se solapa con uno existente.");
        }
        espacio.setCoordenadaX(newX);
        espacio.setCoordenadaY(newY);
        return repositorioEspacio.actualizarEspacio(espacio);
    }

    @Override
    public boolean eliminarEspacio(String id) {
        if (estaEspacioReferenciadoEnAlgunRecorrido(id)) {
            throw new ExcepcionReglaNegocio("BR-04",
                    "No se puede eliminar el espacio: está referenciado en un recorrido. Elimínalo del recorrido primero.");
        }
        return repositorioEspacio.eliminarEspacioPorId(id);
    }

    @Override
    public boolean tieneSolapamiento(List<Espacio> espacios, int x, int y, int ancho, int alto, String excludeId) {
        for (Espacio espacio : espacios) {
            if (espacio.seSolapaCon(x, y, ancho, alto, excludeId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean estaEspacioReferenciadoEnAlgunRecorrido(String idEspacio) {
        List<Recorrido> routes = repositorioRecorrido.obtenerTodos();
        for (Recorrido recorrido : routes) {
            if (recorrido.getEspacioIds().contains(idEspacio)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actualizarNombreUniversidad(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ExcepcionReglaNegocio("BR-06", "El nombre de la universidad no puede estar vacío.");
        }
        Universidad universidad = repositorioEspacio.cargarUniversidad();
        universidad.setNombre(nombre);
        repositorioEspacio.guardarUniversidad(universidad);
    }

    @Override
    public void actualizarNombreRegion(String idRegion, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ExcepcionReglaNegocio("BR-06", "El nombre de la región no puede estar vacío.");
        }
        Universidad universidad = repositorioEspacio.cargarUniversidad();
        for (Region region : universidad.getRegiones()) {
            if (region.getId().equals(idRegion)) {
                region.setNombre(nombre);
                break;
            }
        }
        repositorioEspacio.guardarUniversidad(universidad);
    }

    @Override
    public Region agregarRegion(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ExcepcionReglaNegocio("BR-06", "El nombre de la región no puede estar vacío.");
        }
        Universidad universidad = repositorioEspacio.cargarUniversidad();
        Region region = new Region();
        region.setId("region-" + UUID.randomUUID().toString().substring(0, 8));
        region.setNombre(nombre);
        region.setPlantas(new ArrayList<>());
        universidad.addRegion(region);
        repositorioEspacio.guardarUniversidad(universidad);
        return region;
    }
}