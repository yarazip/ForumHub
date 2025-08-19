package br.com.yarazip.forumhub.security.utils;

import br.com.yarazip.forumhub.modules.exception.usecase.TokenExpiredCustomException;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	@Value("${jwt.issuer}")
	private String issuer;

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private int expirationDate;

	public String generateToken(UserEntity user) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		return JWT.create().withIssuer(issuer).withSubject(user.getId().toString())
				.withClaim("username", user.getUsername()).withExpiresAt(generateExpirationDate()).sign(algorithm);
	}

	public String generateRefreshToken(UserEntity user) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		return JWT.create().withIssuer(issuer).withSubject(user.getUsername())
				.withExpiresAt(Date.from(Instant.now().plus(Duration.ofDays(30))))
				.withClaim("type", "refresh").sign(algorithm);
	}

	public String extractUsername(String token) {
		return decodeToken(token).getClaim("username").asString();
	}

	public boolean isTokenExpired(String token) {
		return decodeToken(token).getExpiresAt().before(new Date());
	}

	private DecodedJWT decodeToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secretKey);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
			return verifier.verify(token);
		} catch (TokenExpiredException e) {
			logger.warn("Token expired: {}", token);
			throw new TokenExpiredCustomException("Token has expired. Please refresh.");
		} catch (Exception e) {
			logger.error("Invalid token: {}", token, e);
			throw new IllegalArgumentException("Invalid token.");
		}
	}

	private Date generateExpirationDate() {
		return Date.from(Instant.now().plus(Duration.ofDays(expirationDate)));
	}

	public String extractUserId(String token) {
		return decodeToken(token).getSubject();
	}
}
