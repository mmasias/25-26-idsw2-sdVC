package com.myuniverse.repositories;

import com.myuniverse.models.Administrador;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdministradorRepository implements IRepository<Administrador> {
    private static final String FILE_NAME = "administradores.json";
    private static final Type LIST_TYPE = new TypeToken<List<Administrador>>() {}.getType();

    @Override
    public List<Administrador> obtenerTodos() {
        List<Administrador> list = JsonUtil.read(FILE_NAME, LIST_TYPE, true);
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public Administrador obtenerPorId(String id) {
        return obtenerTodos().stream()
                .filter(a -> a.getNombreUsuario().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Administrador guardar(Administrador admin) {
        List<Administrador> list = obtenerTodos();
        list.add(admin);
        JsonUtil.write(FILE_NAME, list, LIST_TYPE);
        return admin;
    }

    @Override
    public boolean actualizar(Administrador admin) {
        List<Administrador> list = obtenerTodos();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNombreUsuario().equals(admin.getNombreUsuario())) {
                list.set(i, admin);
                JsonUtil.write(FILE_NAME, list, LIST_TYPE);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminarPorId(String id) {
        List<Administrador> list = obtenerTodos();
        boolean removed = list.removeIf(a -> a.getNombreUsuario().equals(id));
        if (removed) {
            JsonUtil.write(FILE_NAME, list, LIST_TYPE);
        }
        return removed;
    }
}