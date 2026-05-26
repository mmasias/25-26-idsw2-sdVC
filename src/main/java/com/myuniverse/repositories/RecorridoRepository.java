package com.myuniverse.repositories;

import com.myuniverse.models.Recorrido;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecorridoRepository implements IRecorridoRepository {
    private static final String FILE_NAME = "recorridos.json";
    private static final Type LIST_TYPE = new TypeToken<List<Recorrido>>() {}.getType();

    @Override
    public List<Recorrido> obtenerTodos() {
        List<Recorrido> list = JsonUtil.read(FILE_NAME, LIST_TYPE, true);
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public Recorrido obtenerPorId(String id) {
        return obtenerTodos().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Recorrido guardar(Recorrido recorrido) {
        List<Recorrido> list = obtenerTodos();
        list.add(recorrido);
        JsonUtil.write(FILE_NAME, list, LIST_TYPE);
        return recorrido;
    }

    @Override
    public boolean actualizar(Recorrido recorrido) {
        List<Recorrido> list = obtenerTodos();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(recorrido.getId())) {
                list.set(i, recorrido);
                JsonUtil.write(FILE_NAME, list, LIST_TYPE);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminarPorId(String id) {
        List<Recorrido> list = obtenerTodos();
        boolean removed = list.removeIf(r -> r.getId().equals(id));
        if (removed) {
            JsonUtil.write(FILE_NAME, list, LIST_TYPE);
        }
        return removed;
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return obtenerTodos().stream()
                .anyMatch(r -> r.getNombre().equalsIgnoreCase(nombre));
    }

    @Override
    public List<Recorrido> filtrarPorCriterio(String criterio) {
        String lower = criterio.toLowerCase();
        return obtenerTodos().stream()
                .filter(r -> r.getNombre().toLowerCase().contains(lower)
                        || (r.getDescripcion() != null && r.getDescripcion().toLowerCase().contains(lower)))
                .collect(Collectors.toList());
    }
}