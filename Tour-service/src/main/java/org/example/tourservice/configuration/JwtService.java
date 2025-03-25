package org.example.tourservice.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Map;

@Service
public class JwtService {
    private static final String SECRET_KEY = "YourVerySecretKeyForJwtTokenThatIsVerySecureAndLong";
    public String extractUsername(String token) {
        return extractClaim(token, "sub");
    }

    public String extractRole(String token) {
        return extractClaim(token, "role");
    }


    public boolean isTokenValid(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];

            String expectedSignature = generateSignature(header, payload);
            return signature.equals(expectedSignature);
        } catch (Exception e) {
            return false;
        }
    }

    private String extractClaim(String token, String claimKey) {
        try {
            String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            Map<String, Object> claims = new ObjectMapper().readValue(payload, new TypeReference<>() {});
            return claims.getOrDefault(claimKey, "").toString();
        } catch (Exception e) {
            return null;
        }
    }
    private String generateSignature(String header, String payload) throws Exception {
        String data = header + "." + payload;
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hmac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}
