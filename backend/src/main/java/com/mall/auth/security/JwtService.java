package com.mall.auth.security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration-ms}")
	private long expirationMs;

	@Value("${jwt.issuer}")
	private String issuer;

	private Key signingKey;

	@PostConstruct
	void init() {
		this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
	}

	public String generateToken(UserDetails userDetails) {
		Instant now = Instant.now();
		Instant expiresAt = now.plusMillis(expirationMs);
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuer(issuer)
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(expiresAt))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		return extractUsername(token).equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token);
	}

	public long getExpirationMs() {
		return expirationMs;
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}
}