package com.myuniverse.repositories;

import com.google.gson.reflect.TypeToken;
import com.myuniverse.models.Recorrido;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecorridoRepository implements IRepository<Recorrido> {
    private static final String ARCHIVO = "recorridos.json";
    private static final Type TIPO_LISTA = new TypeToken<List<Recorrido>>() {}.getType();

    @Override
    public List<Recorrido> findAll() {
        List<Recorrido> lista = JsonUtil.leer(ARCHIVO, TIPO_LISTA);
        return lista != null ? lista : new ArrayList<>();
    }

    @Override
    public Recorrido findById(String id) {
        return findAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Recorrido save(Recorrido recorrido) {
        List<Recorrido> lista = findAll();
        lista.add(recorrido);
        JsonUtil.escribir(ARCHIVO, lista, TIPO_LISTA);
        return recorrido;
    }

    @Override
    public boolean update(Recorrido recorrido) {
        List<Recorrido> lista = findAll();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(recorrido.getId())) {
                lista.set(i, recorrido);
                JsonUtil.escribir(ARCHIVO, lista, TIPO_LISTA);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        List<Recorrido> lista = findAll();
        boolean eliminado = lista.removeIf(r -> r.getId().equals(id));
        if (eliminado) JsonUtil.escribir(ARCHIVO, lista, TIPO_LISTA);
        return eliminado;
    }

    public List<Recorrido> filtrarPorCriterio(String criterio) {
        String lower = criterio.toLowerCase();
        return findAll().stream()
                .filter(r -> r.getNombre().toLowerCase().contains(lower)
                        || (r.getDescripcion() != null && r.getDescripcion().toLowerCase().contains(lower)))
                .collect(Collectors.toList());
    }

    public boolean existeNombre(String nombre) {
        return findAll().stream()
                .anyMatch(r -> r.getNombre().equalsIgnoreCase(nombre));
    }
}
