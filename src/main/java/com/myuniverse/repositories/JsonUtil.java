package com.myuniverse.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtil() {
    }

    public static <T> T leer(String rutaArchivo, Type tipo) {
        try {
            Path ruta = obtenerRutaRecurso(rutaArchivo);
            if (!Files.exists(ruta))
                return null;
            String contenido = Files.readString(ruta, StandardCharsets.UTF_8);
            return GSON.fromJson(contenido, tipo);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer " + rutaArchivo + ": " + e.getMessage(), e);
        }
    }

    public static <T> void escribir(String rutaArchivo, T objeto, Type tipo) {
        try {
            Path ruta = obtenerRutaRecurso(rutaArchivo);
            Files.createDirectories(ruta.getParent());
            String json = GSON.toJson(objeto, tipo);
            Files.writeString(ruta, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir " + rutaArchivo + ": " + e.getMessage(), e);
        }
    }

    public static String obtenerRutaBase() {
        String ruta = System.getProperty("myuniverse.data.dir");
        if (ruta != null)
            return ruta;
        return "src/main/resources/data";
    }

    private static Path obtenerRutaRecurso(String nombreArchivo) {
        return Paths.get(obtenerRutaBase(), nombreArchivo);
    }
}
