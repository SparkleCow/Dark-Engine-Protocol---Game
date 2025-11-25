package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.entities.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_POSITIONS = "game:positions";

    public void updatePosition(Position position) {
        redisTemplate.opsForHash().put(
                KEY_POSITIONS,
                position.getPlayerId(),
                position
        );
    }

    public Position getPosition(String playerId) {

        Object obj = redisTemplate.opsForHash().get(KEY_POSITIONS, playerId);

        if (obj instanceof Position) {
            return (Position) obj;
        }
        return null;
    }

    public List<Position> getAllPositions() {

        return redisTemplate.opsForHash().values(KEY_POSITIONS)
                .stream()
                .filter(obj -> obj instanceof Position)
                .map(obj -> (Position) obj)
                .collect(Collectors.toList());
    }
}