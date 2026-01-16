package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.models.Position;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis Hash:
    // Key   -> "game:positions"
    // Field -> username
    // Value -> Position
    private static final String KEY_POSITIONS = "game:positions";

    /**
     * Updates or creates the current position for a player.
     * The username acts as the canonical identity in Redis.
     */
    public void updatePosition(String username, Position position) {

        redisTemplate.opsForHash().put(
                KEY_POSITIONS,
                username,
                position
        );

        log.debug("Updated position for user {}", username);
    }

    /**
     * Retrieves the current position of a player by username.
     */
    public Position getPosition(String username) {

        Object obj = redisTemplate.opsForHash().get(KEY_POSITIONS, username);

        if (obj instanceof Position position) {
            return position;
        }

        return null;
    }

    /**
     * Returns the positions of all connected players.
     */
    public List<Position> getAllPositions() {

        return redisTemplate.opsForHash()
                .values(KEY_POSITIONS)
                .stream()
                .filter(Position.class::isInstance)
                .map(Position.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * Removes a player's position from Redis
     * (e.g. on disconnect).
     */
    public void deletePosition(String username) {

        redisTemplate.opsForHash().delete(KEY_POSITIONS, username);

        log.info(
                "Deleted position for user '{}' from Redis key '{}'",
                username,
                KEY_POSITIONS
        );
    }
}
