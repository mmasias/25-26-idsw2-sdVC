package com.myuniverse.repositories;

import com.google.gson.reflect.TypeToken;
import com.myuniverse.models.Administrador;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdministradorRepository implements IRepository<Administrador> {
    private static final String ARCHIVO = "administradores.json";
    private static final Type TIPO_LISTA = new TypeToken<List<Administrador>>() {}.getType();

    @Override
    public List<Administrador> findAll() {
        List<Administrador> lista = JsonUtil.leer(ARCHIVO, TIPO_LISTA);
        return lista != null ? lista : new ArrayList<>();
    }

    @Override
    public Administrador findById(String usuario) {
        return findAll().stream()
                .filter(a -> a.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Administrador save(Administrador admin) {
        List<Administrador> lista = findAll();
        lista.add(admin);
        JsonUtil.escribir(ARCHIVO, lista, TIPO_LISTA);
        return admin;
    }

    @Override
    public boolean update(Administrador admin) {
        List<Administrador> lista = findAll();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getUsuario().equals(admin.getUsuario())) {
                lista.set(i, admin);
                JsonUtil.escribir(ARCHIVO, lista, TIPO_LISTA);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(String usuario) {
        List<Administrador> lista = findAll();
        boolean eliminado = lista.removeIf(a -> a.getUsuario().equals(usuario));
        if (eliminado) JsonUtil.escribir(ARCHIVO, lista, TIPO_LISTA);
        return eliminado;
    }
}
