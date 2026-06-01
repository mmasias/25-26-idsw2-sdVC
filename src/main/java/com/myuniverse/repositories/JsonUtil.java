package com.myuniverse.repositories;

import com.myuniverse.models.TipoEspacio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.myuniverse.exceptions.ExcepcionPersistencia;

public class JsonUtil {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(TipoEspacio.class, new TipoEspacioAdapter())
            .create();

    private JsonUtil() {
    }

    public static <T> T read(String fileName, Type tipo) {
        return read(fileName, tipo, false);
    }

    public static <T> T read(String fileName, Type tipo, boolean isList) {
        try {
            Path path = getResourcePath(fileName);
            if (!Files.exists(path)) {
                return null;
            }
            String content = Files.readString(path, StandardCharsets.UTF_8);
            return GSON.fromJson(content, tipo);
        } catch (IOException e) {
            throw new ExcepcionPersistencia("Failed to read data from: " + fileName, e);
        }
    }

    public static <T> void write(String fileName, T object, Type tipo) {
        try {
            Path path = getResourcePath(fileName);
            Files.createDirectories(path.getParent());
            String json = GSON.toJson(object, tipo);
            Files.writeString(path, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ExcepcionPersistencia("Failed to guardar data to disk.", e);
        }
    }

    public static void write(String fileName, Object object) {
        try {
            Path path = getResourcePath(fileName);
            Files.createDirectories(path.getParent());
            String json = GSON.toJson(object);
            Files.writeString(path, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ExcepcionPersistencia("Failed to guardar data to disk.", e);
        }
    }

    public static String getBasePath() {
        String path = System.getProperty("myuniverse.data.dir");
        if (path != null) {
            return path;
        }
        return "src/main/resources/data";
    }

    private static Path getResourcePath(String fileName) {
        return Paths.get(getBasePath(), fileName);
    }

    private static class TipoEspacioAdapter extends TypeAdapter<TipoEspacio> {
        @Override
        public void write(JsonWriter out, TipoEspacio value) throws IOException {
            out.value(value != null ? value.getPersistedName() : TipoEspacio.OTHER.getPersistedName());
        }

        @Override
        public TipoEspacio read(JsonReader in) throws IOException {
            String value = in.nextString();
            return TipoEspacio.fromPersistedName(value);
        }
    }
}