package rw.dsacco.clat.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "GJ0sX9YyT6rCpz1NdJ3qFtkT9NfO3ZojapL9zpKXf7Q="; // 🔐 Use a strong secret
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)); // ✅ Base64-decoded key

    // ✅ Generate Token (User ID included)
    public String generateToken(String email, Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10-hour expiration
                .signWith(SIGNING_KEY, io.jsonwebtoken.SignatureAlgorithm.HS256) // ✅ FIXED: Uses correct signing method for JJWT 0.11.5
                .compact();
    }

    // ✅ Extract Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY) // ✅ Uses correct parsing method
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Extract User ID from Token
    public int extractUserId(String token) {
        Claims claims = extractAllClaims(token);

        // ✅ Ensure userId exists before extracting it
        Object userIdObj = claims.get("userId");
        if (userIdObj == null) {
            throw new RuntimeException("Invalid token: userId is missing");
        }

        return Integer.parseInt(userIdObj.toString());
    }

    // ✅ Validate Token
    public boolean validateToken(String token, String userEmail) {
        return extractEmail(token).equals(userEmail) && !isTokenExpired(token);
    }

    // ✅ Extract Email from Token
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ✅ Check if Token is Expired
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
