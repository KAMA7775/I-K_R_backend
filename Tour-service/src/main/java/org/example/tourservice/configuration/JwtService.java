package org.example.tourservice.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Map;

@Service
public class JwtService {
        private static final String SECRET_KEY = "67877788989898989098989898909hhjvhdddddddddjjfdus8594";
        private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        public String extractUsername(String token) {
            return extractClaim(token, "sub");
        }

        public String extractRole(String token) {
            return extractClaim(token, "role");
        }

        public String extractUserId(String token) {
            return extractClaim(token, "userIdStr");
        }

        public boolean isTokenValid(String token) {
            try {
                Jwts.parser()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        private String extractClaim(String token, String claimKey) {
            try {
                return Jwts.parser()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .get(claimKey, String.class);
            } catch (Exception e) {
                throw new RuntimeException("Не удалось извлечь claim: " + claimKey);
            }
        }
    }
