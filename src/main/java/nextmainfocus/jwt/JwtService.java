package nextmainfocus.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
// @ConfigurationProperties("jwt")
public class JwtService {
	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

	private long expiration = 18000000; // 5 hours in milliseconds 5 * 60 * 60 * 1000
	private String secret = "3778217A25432A462D4A614E645267556B58703273357638792F423F4428472B";

	public String authorizationHeader = "Authorization";
	private String tokenPrefix = "Bearer ";

	public Map<String, String> extractTokenFromHeader(String authHeader) {
		Map<String, String> tokenMap = new HashMap<>();
		if (authHeader == null || !authHeader.startsWith(tokenPrefix)) {
			tokenMap.put("token", null);
			tokenMap.put("refreshToken", null);
			return tokenMap;
		}
		tokenMap.put("token", authHeader.substring(7));
		tokenMap.put("refreshToken", authHeader.substring(7));
		tokenMap.put("username", extractUsername(authHeader.substring(7)));

		return tokenMap;
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpirationDate(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims;
		try {
			claims = extractAllClaims(token);
		} catch (Exception e) {
			logger.error("There was an error while extracting claims from token: {}", e.getMessage());
			return null;
		}
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("authorities", userDetails.getAuthorities());
		return generateToken(claims, userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	// refresh token
	public String refreshToken(String token) {
		final Claims claims = extractAllClaims(token);
		claims.setIssuedAt(new Date(System.currentTimeMillis()));
		claims.setExpiration(new Date(System.currentTimeMillis() + expiration));
		return Jwts.builder().setClaims(claims).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	// remove token
	public String removeToken(String token) {
		final Claims claims = extractAllClaims(token);
		claims.setIssuedAt(new Date(System.currentTimeMillis()));
		claims.setExpiration(new Date(System.currentTimeMillis()));
		return Jwts.builder().setClaims(claims).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpirationDate(token).before(new Date());
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
