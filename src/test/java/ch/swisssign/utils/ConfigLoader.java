package ch.swisssign.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ConfigLoader {

    private static final String ENV_CONFIG_PATH = "src/main/resources/environments.json";
    private static final String USERS_PATH = "src/test/resources/testdata/users.json";
    private static final String DEFAULT_ENV = "preprod";

    public static String getBaseUrl() {
        String env = System.getProperty("env", DEFAULT_ENV); // mvn clean test -Denv=prod
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(new File(ENV_CONFIG_PATH));
            return root.path(env).path("baseUrl").asText();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load environment config for env=" + env, e);
        }
    }

    public static JsonNode getUsers() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(Paths.get(USERS_PATH).toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load users.json", e);
        }
    }
}