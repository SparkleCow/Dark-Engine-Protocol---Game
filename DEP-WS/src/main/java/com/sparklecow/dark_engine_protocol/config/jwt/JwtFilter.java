package com.sparklecow.dark_engine_protocol.config.jwt;

import com.sparklecow.dark_engine_protocol.entities.Player;
import com.sparklecow.dark_engine_protocol.exceptions.BusinessErrorCodes;
import com.sparklecow.dark_engine_protocol.exceptions.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * JWT filter that runs before controllers.
 * Since this runs before reaching controllers, exceptions here
 * can not be handled by ControllerAdvice that is why it send
 * custom JSON error responses directly from the filter.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = bearerToken.substring(7);

        String username = null;

        try {
            username = jwtUtils.extractUsername(token);

            if (username == null || username.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            Player player= (Player) userDetailsService.loadUserByUsername(username);

            if (!jwtUtils.validateToken(token, player)) {
                sendErrorResponse(response, BusinessErrorCodes.TOKEN_INVALID, "Token validation failed",
                        Map.of("token", "Invalid or malformed"));
                return;
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(player, null, player.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (UsernameNotFoundException e) {
            sendErrorResponse(response, BusinessErrorCodes.USER_NOT_FOUND, "User not found",
                    Map.of("username", username));
            return;
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, BusinessErrorCodes.TOKEN_EXPIRED, "JWT expired", Map.of("token", e.getMessage()));
            return;
        } catch (MalformedJwtException | SignatureException e) {
            sendErrorResponse(response, BusinessErrorCodes.TOKEN_INVALID, "JWT signature or format invalid",
                    Map.of("token", e.getMessage()));
            return;
        } catch (Exception e) {
            log.error("Unexpected JWT filter error: {}", e.getMessage());
            sendErrorResponse(response, BusinessErrorCodes.INTERNAL_SERVER_ERROR,
                    "Unexpected JWT processing error", Map.of("details", e.getClass().getSimpleName()));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, BusinessErrorCodes code, String message,
                                   Map<String, String> details) throws IOException {
        response.setStatus(code.getHttpStatus().value());
        response.setContentType("application/json");

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(message)
                .businessErrorCode(code.getErrorCode())
                .businessErrorDescription(code.getMessage())
                .errorDetails(details)
                .build();

        objectMapper.writeValue(response.getWriter(), exceptionResponse);
    }
}