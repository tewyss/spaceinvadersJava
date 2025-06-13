package spaceinvaders.storage;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.InputStream;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class HallOfFameManager {
    private static final String BASE   = "http://localhost:8080/api";
    private final HttpClient  client  = HttpClient.newHttpClient();
    private final ObjectMapper mapper;

    public HallOfFameManager() {
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<PlayerScore> loadScores() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/scores"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<InputStream> res = client.send(req, HttpResponse.BodyHandlers.ofInputStream());
        return List.of(mapper.readValue(res.body(), PlayerScore[].class));
    }

    public void deleteScore(long scoreId) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/scores/" + scoreId))
                .DELETE()
                .build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    public void saveSingleScore(String playerName, int score) throws Exception {
        String playerJson = mapper.writeValueAsString(Map.of("name", playerName));
        HttpRequest reqP = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/players"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(playerJson))
                .build();
        HttpResponse<InputStream> resP = client.send(reqP, HttpResponse.BodyHandlers.ofInputStream());
        Player player = mapper.readValue(resP.body(), Player.class);
        long playerId = player.id();

        LocalDateTime now = LocalDateTime.now();
        String sessJson = mapper.writeValueAsString(Map.of(
                "playerId",  playerId,
                "startTime", now.minusMinutes(1).toString(),
                "endTime",   now.toString()
        ));
        HttpRequest reqS = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/sessions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(sessJson))
                .build();
        HttpResponse<InputStream> resS = client.send(reqS, HttpResponse.BodyHandlers.ofInputStream());
        GameSession session = mapper.readValue(resS.body(), GameSession.class);
        long sessionId = session.id();

        String scoreJson = mapper.writeValueAsString(Map.of(
                "sessionId", sessionId,
                "score",     score
        ));
        HttpRequest reqSc = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/scores"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(scoreJson))
                .build();
        client.send(reqSc, HttpResponse.BodyHandlers.discarding());
    }

    public void updatePlayerName(long playerId, String newName) throws Exception {
        String body = mapper.writeValueAsString(Map.of("name", newName));
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/players/" + playerId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    public void updateScore(long scoreId, int newScore) throws Exception {
        String body = mapper.writeValueAsString(Map.of("score", newScore));
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/scores/" + scoreId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    public static record Player(Long id, String name) {}
    public static record GameSession(Long id) {}
}
