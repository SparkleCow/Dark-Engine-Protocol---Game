package com.sparklecow.dark_engine_protocol.services;

import com.sparklecow.dark_engine_protocol.entities.LastPosition;
import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.entities.Position;
import com.sparklecow.dark_engine_protocol.repositories.PlayerRepository;
import com.sparklecow.dark_engine_protocol.repositories.PositionRepository;
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
    private final PositionRepository positionRepository;
    private final PlayerRepository playerRepository;

    private static final String KEY_POSITIONS = "game:positions";

    public void updatePosition(Position position) {
        redisTemplate.opsForHash().put(
                KEY_POSITIONS,
                position.getPlayerId().toString(),
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

    public LastPosition findLastPosition(Long playerId) {
        return positionRepository.findById(playerId)
                .orElse(null);
    }

    public List<Position> getAllPositions() {

        return redisTemplate.opsForHash().values(KEY_POSITIONS)
                .stream()
                .filter(obj -> obj instanceof Position)
                .map(obj -> (Position) obj)
                .collect(Collectors.toList());
    }

    public LastPosition saveLastPosition(Long playerId, Position finalPosition){

        // No need for playerRepository injection or getReferenceById() call!
        LastPosition lastPosition = positionRepository.findById(playerId)
                .orElseGet(() -> {
                    LastPosition newPosition = new LastPosition();
                    newPosition.setPlayerId(playerId); // Set the PK directly
                    return newPosition;
                });

        lastPosition.setX(finalPosition.getX());
        lastPosition.setY(finalPosition.getY());
        lastPosition.setMapId(finalPosition.getMapId());
        lastPosition.setAngle(finalPosition.getAngle());

        return positionRepository.save(lastPosition);
    }

    public void deletePositionFromRedis(String playerId){
        redisTemplate.opsForHash().delete(KEY_POSITIONS, playerId);
        log.info("Deleted player {}'s position from Redis Hash '{}'.", playerId, KEY_POSITIONS);
    }
}