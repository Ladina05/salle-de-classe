package com.gestion.salles.desktop.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gestion.salles.desktop.model.Occupant;
import com.gestion.salles.desktop.model.Prof;
import com.gestion.salles.desktop.model.Salle;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class ApiClient {

    private final String baseUrl;
    private final HttpClient http;
    private final ObjectMapper mapper;

    public ApiClient() {
        this("http://localhost:8080/api");
    }

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public List<Prof> listerProfs() throws Exception {
        return get("/profs", new TypeReference<>() {
        });
    }

    public List<Prof> rechercherProfs(String terme) throws Exception {
        String encoded = URLEncoder.encode(terme, StandardCharsets.UTF_8);
        return get("/profs/recherche?terme=" + encoded, new TypeReference<>() {
        });
    }

    public Prof creerProf(Prof prof) throws Exception {
        return send("POST", "/profs", prof, Prof.class, 201);
    }

    public Prof modifierProf(String code, Prof prof) throws Exception {
        return send("PUT", "/profs/" + enc(code), prof, Prof.class, 200);
    }

    public void supprimerProf(String code) throws Exception {
        send("DELETE", "/profs/" + enc(code), null, Void.class, 204);
    }

    public List<Salle> listerSalles() throws Exception {
        return get("/salles", new TypeReference<>() {
        });
    }

    public Salle creerSalle(Salle salle) throws Exception {
        return send("POST", "/salles", salle, Salle.class, 201);
    }

    public Salle modifierSalle(String code, Salle salle) throws Exception {
        return send("PUT", "/salles/" + enc(code), salle, Salle.class, 200);
    }

    public void supprimerSalle(String code) throws Exception {
        send("DELETE", "/salles/" + enc(code), null, Void.class, 204);
    }

    public List<Occupant> listerOccupations() throws Exception {
        return get("/occuper", new TypeReference<>() {
        });
    }

    public Occupant creerOccupation(Occupant occupant) throws Exception {
        return send("POST", "/occuper", occupant, Occupant.class, 201);
    }

    public Occupant modifierOccupation(Long id, Occupant occupant) throws Exception {
        return send("PUT", "/occuper/" + id, occupant, Occupant.class, 200);
    }

    public void supprimerOccupation(Long id) throws Exception {
        send("DELETE", "/occuper/" + id, null, Void.class, 204);
    }

    private <T> T get(String path, TypeReference<T> type) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response, 200);
        return mapper.readValue(response.body(), type);
    }

    private <T> T send(String method, String path, Object body, Class<T> type, int expected) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json");

        if (body != null) {
            String json = mapper.writeValueAsString(body);
            builder.header("Content-Type", "application/json");
            builder.method(method, HttpRequest.BodyPublishers.ofString(json));
        } else {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        }

        HttpResponse<String> response = http.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response, expected);
        if (type == Void.class || response.body() == null || response.body().isBlank()) {
            return null;
        }
        return mapper.readValue(response.body(), type);
    }

    private void ensureSuccess(HttpResponse<String> response, int expected) throws ApiException {
        int code = response.statusCode();
        if (code == expected) {
            return;
        }
        throw new ApiException(code, extractMessage(response.body(), code));
    }

    private String extractMessage(String body, int code) {
        if (body == null || body.isBlank()) {
            return "Erreur HTTP " + code;
        }
        try {
            JsonNode node = mapper.readTree(body);
            if (node.has("message")) {
                return node.get("message").asText();
            }
        } catch (Exception ignored) {
            // corps non JSON
        }
        return body;
    }

    private String enc(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
